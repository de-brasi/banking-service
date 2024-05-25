package my.solution.moneytransfertest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.DriverManager;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import my.solution.dto.JwtAuthenticationResponse;
import my.solution.repository.ClientRepository;
import my.solution.repository.entity.Client;
import my.solution.service.DepositService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class TransferTest extends IntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ClientRepository clientRepository;

    @MockBean
    private DepositService depositService;

    private RestClient ADMIN;

    @Test
    public void dbConnectionTest() {
        final String jdbcUrl = POSTGRES.getJdbcUrl();
        final String username = POSTGRES.getUsername();
        final String password = POSTGRES.getPassword();

        try (java.sql.Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            assertThat(connection.getCatalog()).isEqualTo("bank_db");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Transactional
    @Rollback
    public void simpleMoneyTransactionTest() {
        ADMIN = RestClient.builder().baseUrl("http://localhost:%d".formatted(port)).build();

        // client 1
        RestClient client1 = RestClient.builder().baseUrl("http://localhost:%d".formatted(port)).build();
        final String loginInfoClient1 = """
                {
                  "login": "firstClient",
                  "password": "firstClientPassword",
                  "patronymic": null,
                  "phone": "phone1",
                  "email": "email1",
                  "initial_deposit": 100,
                  "first_name": "first name",
                  "last_name": "last name",
                  "birth_date": "2024-05-24T14:27:46.929Z"
                }
                """;

        // client 2
        final String loginInfoClient2 = """
                {
                  "login": "secondClient",
                  "password": "secondClientPassword",
                  "patronymic": null,
                  "phone": "phone2",
                  "email": "email2",
                  "initial_deposit": 0,
                  "first_name": "second name",
                  "last_name": "second name",
                  "birth_date": "2025-05-24T14:27:46.929Z"
                }
                """;

        // register clients
        final var firstRegistrationResult = registerClient(ADMIN, loginInfoClient1);
        final var secondRegistrationResult = registerClient(ADMIN, loginInfoClient2);

        assertThat(firstRegistrationResult.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(secondRegistrationResult.getStatusCode()).isEqualTo(HttpStatus.OK);

        final String firstClientJwtToken = firstRegistrationResult.getBody().getJwtToken();

        // transfer
        var transferResult = client1.patch()
                .uri("/client/firstClient/transfer-money")
                .header("Receiver-Login", "secondClient")
                .header("Password", "firstClientPassword")
                .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(firstClientJwtToken))
                .header("Amount", "100")
                .retrieve()
                .toBodilessEntity()
                .getStatusCode();

        assertThat(transferResult).isEqualTo(HttpStatus.OK);

        Client sender = clientRepository.findClientByLogin("firstClient").orElseThrow();
        Client receiver = clientRepository.findClientByLogin("secondClient").orElseThrow();
        assertThat(sender.getAccount().getDeposit())
                .isEqualTo(BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_EVEN));
        assertThat(receiver.getAccount().getDeposit())
                .isEqualTo(BigDecimal.valueOf(100.00).setScale(2, RoundingMode.HALF_EVEN));
    }

    @Test
    @Transactional
    @Rollback
    public void concurrentReadWriteTransactionTest() throws InterruptedException {
        ADMIN = RestClient.builder().baseUrl("http://localhost:%d".formatted(port)).build();

        // sender 1
        RestClient sender = RestClient.builder().baseUrl("http://localhost:%d".formatted(port)).build();
        final String loginInfoClient = """
                {
                  "login": "sender1",
                  "password": "sender1",
                  "patronymic": null,
                  "phone": "phone11",
                  "email": "email11",
                  "initial_deposit": 100,
                  "first_name": "first name",
                  "last_name": "last name",
                  "birth_date": "2024-05-24T14:27:46.929Z"
                }
                """;

        // receiver
        final String receiver = """
                {
                  "login": "receiver",
                  "password": "receiver",
                  "patronymic": null,
                  "phone": "phone33",
                  "email": "email33",
                  "initial_deposit": 0,
                  "first_name": "third name",
                  "last_name": "third name",
                  "birth_date": "2025-05-24T14:27:46.929Z"
                }
                """;

        // register clients
        final var sender1RegistrationResult = registerClient(ADMIN, loginInfoClient);
        final var luckyOneRegistrationResult = registerClient(ADMIN, receiver);

        assertThat(sender1RegistrationResult.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(luckyOneRegistrationResult.getStatusCode()).isEqualTo(HttpStatus.OK);

        final String sender1JwtToken = sender1RegistrationResult.getBody().getJwtToken();
        final String luckyOneJwtToken = luckyOneRegistrationResult.getBody().getJwtToken();

        // concurrent transfer
        var worker1 = new Thread(new TransferRoutine(
                sender, 1, 50,
                "sender1", "sender1", "receiver",
                sender1JwtToken));
        var worker2 = new Thread(new TransferRoutine(
                sender, 1, 50,
                "sender1", "sender1", "receiver",
                luckyOneJwtToken));

        worker1.start();
        worker2.start();
        // await transfer
        worker1.join();
        worker2.join();

        Client sender1Client = clientRepository.findClientByLogin("sender1").orElseThrow();
        Client receiverClient = clientRepository.findClientByLogin("receiver").orElseThrow();

        System.out.println(receiverClient.getAccount().getDeposit());
        System.out.println(sender1Client.getAccount().getDeposit());

        assertThat(receiverClient.getAccount().getDeposit())
                .isEqualTo(BigDecimal.valueOf(100).setScale(2, RoundingMode.HALF_EVEN));
        assertThat(sender1Client.getAccount().getDeposit())
                .isEqualTo(BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_EVEN));
    }

    @Test
    @Transactional
    @Rollback
    public void concurrentTransactionTest() throws InterruptedException {
        ADMIN = RestClient.builder().baseUrl("http://localhost:%d".formatted(port)).build();

        // sender 1
        RestClient sender1 = RestClient.builder().baseUrl("http://localhost:%d".formatted(port)).build();
        final String loginInfoClient1 = """
                {
                  "login": "sender111",
                  "password": "sender111",
                  "patronymic": null,
                  "phone": "phone111",
                  "email": "email111",
                  "initial_deposit": 100,
                  "first_name": "first name",
                  "last_name": "last name",
                  "birth_date": "2024-05-24T14:27:46.929Z"
                }
                """;

        // sender 2
        RestClient sender2 = RestClient.builder().baseUrl("http://localhost:%d".formatted(port)).build();
        final String loginInfoClient2 = """
                {
                  "login": "sender222",
                  "password": "sender222",
                  "patronymic": null,
                  "phone": "phone222",
                  "email": "email222",
                  "initial_deposit": 100,
                  "first_name": "second name",
                  "last_name": "second name",
                  "birth_date": "2025-05-24T14:27:46.929Z"
                }
                """;

        // receiver
        final String luckyOneInfo = """
                {
                  "login": "receiver1",
                  "password": "receiver1",
                  "patronymic": null,
                  "phone": "phone333",
                  "email": "email333",
                  "initial_deposit": 0,
                  "first_name": "third name",
                  "last_name": "third name",
                  "birth_date": "2025-05-24T14:27:46.929Z"
                }
                """;

        // register clients
        final var sender1RegistrationResult = registerClient(ADMIN, loginInfoClient1);
        final var sender2RegistrationResult = registerClient(ADMIN, loginInfoClient2);
        final var luckyOneRegistrationResult = registerClient(ADMIN, luckyOneInfo);

        assertThat(sender1RegistrationResult.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(sender2RegistrationResult.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(luckyOneRegistrationResult.getStatusCode()).isEqualTo(HttpStatus.OK);

        final String sender1JwtToken = sender1RegistrationResult.getBody().getJwtToken();
        final String sender2JwtToken = sender2RegistrationResult.getBody().getJwtToken();
        final String luckyOneJwtToken = luckyOneRegistrationResult.getBody().getJwtToken();

        // concurrent transfer
        var worker1 = new Thread(new TransferRoutine(
                sender1, 1, 100,
                "sender111", "sender111", "receiver1",
                sender1JwtToken));
        var worker2 = new Thread(new TransferRoutine(
                sender2, 1, 100,
                "sender222", "sender222", "receiver1",
                sender2JwtToken));

        worker1.start();
        worker2.start();

        // await transfers
        worker1.join();
        worker2.join();

        Client sender1Client = clientRepository.findClientByLogin("sender111").orElseThrow();
        Client sender2Client = clientRepository.findClientByLogin("sender222").orElseThrow();
        Client receiver = clientRepository.findClientByLogin("receiver1").orElseThrow();

        System.out.println(receiver.getAccount().getDeposit());
        System.out.println(sender1Client.getAccount().getDeposit());
        System.out.println(sender2Client.getAccount().getDeposit());

        assertThat(receiver.getAccount().getDeposit())
                .isEqualTo(BigDecimal.valueOf(200).setScale(2, RoundingMode.HALF_EVEN));
        assertThat(sender1Client.getAccount().getDeposit())
                .isEqualTo(BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_EVEN));
        assertThat(sender2Client.getAccount().getDeposit())
                .isEqualTo(BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_EVEN));
    }

    private record TransferRoutine(RestClient sender, int amount, int iterationCount,
                                   String senderLogin,
                                   String senderPassword,
                                   String receiverLogin,
                                   String jwtToken) implements Runnable {
        @Override
        public void run() {
            int success = 0;
            for (int i = 0; i < iterationCount; i++) {
                var res = sender.patch()
                        .uri("/client/%s/transfer-money".formatted(senderLogin()))
                        .header("Receiver-Login", receiverLogin())
                        .header("Password", senderPassword())
                        .header("Amount", String.valueOf(amount))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtToken))
                        .retrieve()
                        .toBodilessEntity()
                        .getStatusCode();
                if (res.is2xxSuccessful()) {
                    success++;
                }
            }
            log.error("Success rate: {}/{}", success, iterationCount);
        }
    }

    private static ResponseEntity<JwtAuthenticationResponse> registerClient(RestClient register, String body) {
        return register
                .post()
                .uri("/api/admin/register")
                .contentType(APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toEntity(JwtAuthenticationResponse.class);
    }

}
