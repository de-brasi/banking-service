package my.solution.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "bank_accounts")
@NoArgsConstructor
@Getter
@Setter
public class BankAccount {
    @Id
    @Column(name = "account_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long accountId;

    @Column(nullable = false)
    BigDecimal deposit = BigDecimal.ZERO;

    @Column(nullable = false, name = "max_deposit")
    BigDecimal maxDeposit = BigDecimal.ZERO;
}
