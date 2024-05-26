package my.solution.repository;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Optional;
import my.solution.repository.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    @Transactional(readOnly = true)
    Optional<Client> findClientByLogin(String login);

    @Transactional(readOnly = true)
    Collection<Client> findAllByBirthDateAfter(OffsetDateTime sample);

    @Transactional(readOnly = true)
    Collection<Client> findAllByPhoneNumberEquals(String sample);

    @Transactional(readOnly = true)
    Collection<Client> findAllByEmailAddressEquals(String sample);

    @Transactional(readOnly = true)
    @Query("SELECT client FROM Client client "
            + "WHERE CONCAT(client.firstName, ' ', client.lastName, ' ', client.patronymic) "
            + "LIKE ?1%")
    Collection<Client> findAllByFullNameLike(String sample);
}
