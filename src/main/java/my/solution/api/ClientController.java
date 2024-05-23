package my.solution.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.solution.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping(value = "/client")
@Slf4j
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PatchMapping(value = "/{clientLogin}/phone-number")
    public ResponseEntity<?> updatePhoneNumber(
            @PathVariable String clientLogin,
            @RequestHeader("Phone-Number") String newPhoneNumber) {
        log.info("Request to update number. Client={}. New number={}", clientLogin, newPhoneNumber);
        clientService.updatePhoneNumberForClient(clientLogin, newPhoneNumber);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{clientLogin}/phone-number")
    public ResponseEntity<?> deletePhoneNumber(@PathVariable String clientLogin) {
        log.info("Delete number. Client={}.", clientLogin);
        clientService.deletePhoneNumberForClient(clientLogin);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(value = "/{clientLogin}/email")
    public ResponseEntity<?> updateEmail(
            @PathVariable String clientLogin,
            @RequestHeader("Phone-Number") String newEmail) {
        log.info("Update email. Client={}. New email={}", clientLogin, newEmail);
        clientService.updateEmailForClient(clientLogin, newEmail);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{clientLogin}/email")
    public ResponseEntity<?> deleteEmail(@PathVariable String clientLogin) {
        log.info("Delete email. Client={}.", clientLogin);
        clientService.deleteEmailForClient(clientLogin);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(value = "/{clientLogin}/transfer-money")
    public ResponseEntity<?> transferMoney(
            @PathVariable String clientLogin,
            @RequestHeader("Receiver-Login") String receiverLogin,
            @RequestHeader("Amount") BigDecimal amount) {
        log.info("Transfer money. From={} to={}.", clientLogin, receiverLogin);
        clientService.transferMoney(clientLogin, receiverLogin, amount);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
