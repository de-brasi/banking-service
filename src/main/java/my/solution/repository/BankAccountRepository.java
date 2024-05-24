package my.solution.repository;

import my.solution.repository.entity.BankAccount;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    @Transactional(isolation = Isolation.SERIALIZABLE)
    default void saveWithSerializable(@NotNull BankAccount entity) {
        save(entity);
    }
}
