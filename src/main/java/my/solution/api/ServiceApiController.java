package my.solution.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.solution.dto.RegisterClientRequest;
import my.solution.dto.SearchClientsRequest;
import my.solution.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/admin")
@RequiredArgsConstructor
@Slf4j
public class ServiceApiController {

    private final ClientService clientService;

    @PostMapping(value = "/register")
    public ResponseEntity<?> handleRegistration(@Validated @RequestBody RegisterClientRequest requestBody) {
        log.info("Got request body: {}", requestBody);
        clientService.addNewClient(requestBody);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<?> handleSearch(@Validated @RequestBody SearchClientsRequest requestBody) {
        log.info("Got search request: {}", requestBody);
        clientService.searchClients(requestBody);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
