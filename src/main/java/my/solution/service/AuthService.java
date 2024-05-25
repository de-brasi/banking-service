package my.solution.service;

import lombok.RequiredArgsConstructor;
import my.solution.dto.ClientSignInRequest;
import my.solution.dto.JwtAuthenticationResponse;
import my.solution.dto.RegisterClientRequest;
import my.solution.repository.BankAccountRepository;
import my.solution.repository.entity.BankAccount;
import my.solution.repository.entity.Client;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final ClientService clientService;
    private final BankAccountRepository bankAccountRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Регистрация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signUp(RegisterClientRequest request) {
        BankAccount accountEntity = new BankAccount();
        accountEntity.setDeposit(request.getInitialDeposit());
        bankAccountRepository.saveWithSerializable(accountEntity);

        var client = Client.builder()
                .login(request.getLogin())
                .password(request.getPassword())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .patronymic(request.getPatronymic())
                .phoneNumber(request.getPhone())
                .emailAddress(request.getEmail())
                .birthDate(request.getBirthDate())
                .account(accountEntity)
                .build();

        clientService.addNewClient(client);

        var jwt = jwtService.generateToken(client);
        return new JwtAuthenticationResponse(jwt);
    }

    /**
     * Аутентификация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signIn(ClientSignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getLogin(),
                request.getPassword()
        ));

        var client = clientService
                .userDetailsService()
                .loadUserByUsername(request.getLogin());

        var jwt = jwtService.generateToken(client);
        return new JwtAuthenticationResponse(jwt);
    }
}
