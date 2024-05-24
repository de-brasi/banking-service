package my.solution.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.solution.api.exceptions.*;
import my.solution.dto.ClientDTO;
import my.solution.dto.RegisterClientRequest;
import my.solution.dto.SearchClientsRequest;
import my.solution.repository.BankAccountRepository;
import my.solution.repository.ClientRepository;
import my.solution.repository.entity.BankAccount;
import my.solution.repository.entity.Client;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final BankAccountRepository bankAccountRepository;

    @Transactional
    public void addNewClient(RegisterClientRequest clientInfo) {
        BankAccount accountEntity = new BankAccount();
        accountEntity.setDeposit(clientInfo.getInitialDeposit());

        bankAccountRepository.save(accountEntity);

        Client client = getClient(clientInfo, accountEntity);

        try {
            clientRepository.save(client);
        } catch (DataIntegrityViolationException e) {
            log.warn("DataIntegrityViolationException when try to save entity {}", clientInfo);
            throw new ValueAlreadyExistsException(e.getMostSpecificCause());
        }

        log.info("Registered client: {}", clientInfo);
    }

    @Transactional
    public void updatePhoneNumberForClient(String clientLogin, String newPhoneNumber) {
        log.info("Client service update number. Login={}, number={}", clientLogin, newPhoneNumber);
        try {
            var ownerWrapper = clientRepository.findClientByLogin(clientLogin);
            var owner = ownerWrapper.orElseThrow(ClientNotExistsException::new);
            owner.setPhoneNumber(newPhoneNumber);
            clientRepository.save(owner);
        } catch (DataIntegrityViolationException e) {
            log.warn("DataIntegrityViolationException when try to update phone number for client with login={}",
                    clientLogin);
            throw new ValueAlreadyExistsException(e.getMostSpecificCause());
        }
    }

    @Transactional
    public void updateEmailForClient(String clientLogin, String newEmail) {
        log.info("Client service update email. Login={}, number={}", clientLogin, newEmail);
        try {
            var ownerWrapper = clientRepository.findClientByLogin(clientLogin);
            var owner = ownerWrapper.orElseThrow(ClientNotExistsException::new);
            owner.setEmailAddress(newEmail);
            clientRepository.save(owner);
        } catch (DataIntegrityViolationException e) {
            log.warn("DataIntegrityViolationException when try to update email for client with login={}",
                    clientLogin);
            throw new ValueAlreadyExistsException(e.getMostSpecificCause());
        }
    }

    @Transactional
    public void deletePhoneNumberForClient(String clientLogin) {
        log.info("Client service delete number. Login={}", clientLogin);
        var ownerWrapper = clientRepository.findClientByLogin(clientLogin);
        var owner = ownerWrapper.orElseThrow(ClientNotExistsException::new);
        if (owner.getEmailAddress() != null) {
            owner.setPhoneNumber(null);
            clientRepository.save(owner);
        } else {
            throw new LastContactRemoveException("phone number");
        }
    }

    @Transactional
    public void deleteEmailForClient(String clientLogin) {
        log.info("Client service delete email. Login={}", clientLogin);
        var ownerWrapper = clientRepository.findClientByLogin(clientLogin);
        var owner = ownerWrapper.orElseThrow(ClientNotExistsException::new);
        if (owner.getPhoneNumber() != null) {
            owner.setEmailAddress(null);
            clientRepository.save(owner);
        } else {
            throw new LastContactRemoveException("email address");
        }
    }

    @Transactional(readOnly = true)
    public Collection<ClientDTO> searchClients(SearchClientsRequest restrictionsInfo) {
        return switch (restrictionsInfo.getFieldName()) {
            case ("birth_date") -> {

                var searchResult = new java.util.ArrayList<>(
                        clientRepository.findAllByBirthDateAfter(restrictionsInfo.getPivotDate())
                                .stream()
                                .limit(restrictionsInfo.getResponseLimit())
                                .map(ClientService::getClientDto)
                                .toList()
                );
                sort(restrictionsInfo, searchResult, Comparator.comparing(ClientDTO::getBirthDate));
                yield searchResult;

            } case ("phone_number") -> {

                var searchResult = new java.util.ArrayList<>(
                        clientRepository.findAllByPhoneNumberEquals(restrictionsInfo.getPhoneSample())
                                .stream()
                                .limit(restrictionsInfo.getResponseLimit())
                                .map(ClientService::getClientDto)
                                .toList()
                );
                sort(restrictionsInfo, searchResult, Comparator.comparing(ClientDTO::getPhone));
                yield searchResult;

            } case ("email_address") -> {

                var searchResult = new java.util.ArrayList<>(
                        clientRepository.findAllByEmailAddressEquals(restrictionsInfo.getEmailSample())
                                .stream()
                                .limit(restrictionsInfo.getResponseLimit())
                                .map(ClientService::getClientDto)
                                .toList()
                );
                sort(restrictionsInfo, searchResult, Comparator.comparing(ClientDTO::getPhone));
                yield searchResult;

            } case ("full_name") -> {

                var searchResult = new java.util.ArrayList<>(
                        clientRepository.findAllByPhoneNumberEquals(restrictionsInfo.getFullNameSample())
                                .stream()
                                .limit(restrictionsInfo.getResponseLimit())
                                .map(ClientService::getClientDto)
                                .toList()
                );
                sort(restrictionsInfo, searchResult, Comparator.comparing(ClientDTO::getPhone));
                yield searchResult;

            } default -> throw new RuntimeException("Unexpected field to search by!");
        };
    }

    @Transactional
    public void transferMoney(String fromClientLogin, String clientPassword, String toClientLogin, BigDecimal amount) {
        var fromClientWrapper = clientRepository.findClientByLogin(fromClientLogin);
        var toClientWrapper = clientRepository.findClientByLogin(toClientLogin);
        var fromClient = fromClientWrapper.orElseThrow(ClientNotExistsException::new);
        var toClient = toClientWrapper.orElseThrow(ClientNotExistsException::new);

        if (!fromClient.getPassword().equals(clientPassword)) {
            throw new InvalidPasswordException();
        }

        if (fromClient.getAccount().getDeposit().compareTo(amount) < 0) {
            throw new NotEnoughMoneyException();
        }

        fromClient.getAccount().setDeposit(
                fromClient.getAccount().getDeposit().subtract(amount)
        );
        toClient.getAccount().setDeposit(
                toClient.getAccount().getDeposit().add(amount)
        );
    }

    private static @NotNull Client getClient(RegisterClientRequest clientInfo, BankAccount accountEntity) {
        Client client = new Client();
        client.setLogin(clientInfo.getLogin());
        client.setPassword(clientInfo.getPassword());
        client.setAccount(accountEntity);
        client.setFirstName(clientInfo.getFirstName());
        client.setLastName(clientInfo.getLastName());
        client.setPatronymic(clientInfo.getPatronymic());
        client.setPhoneNumber(clientInfo.getPhone());
        client.setEmailAddress(clientInfo.getEmail());
        client.setBirthDate(clientInfo.getBirthDate());
        return client;
    }

    private static @NotNull ClientDTO getClientDto(Client source) {
        return new ClientDTO(
                source.getLogin(),
                source.getFirstName(),
                source.getLastName(),
                source.getPatronymic(),
                source.getPhoneNumber(),
                source.getEmailAddress(),
                source.getBirthDate()
        );
    }

    private static void sort(SearchClientsRequest restrictionsInfo,
                             ArrayList<ClientDTO> searchResult,
                             Comparator<ClientDTO> comparing) {
        if (restrictionsInfo.isSorted()) {
            if (restrictionsInfo.getSortingOrder().equals("asc")) {
                searchResult.sort(comparing);
            } else if (restrictionsInfo.getSortingOrder().equals("desc")) {
                searchResult.sort(comparing.reversed());
            } else {
                throw new RuntimeException("Unexpected sorting order!");
            }
        }
    }

}
