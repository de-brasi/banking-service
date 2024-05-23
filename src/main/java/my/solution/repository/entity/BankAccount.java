package my.solution.repository.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "bank_accounts")
public class BankAccount {
    @Id
    @Column(name = "account_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long accountId;

    @Column(nullable = false)
    BigDecimal deposit = BigDecimal.ZERO;
}
