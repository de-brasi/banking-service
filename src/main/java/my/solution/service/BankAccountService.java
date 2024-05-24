package my.solution.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.RequiredArgsConstructor;
import my.solution.configuration.BankServiceConfig;
import my.solution.repository.BankAccountRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final BankAccountRepository accountRepository;
    private final BankServiceConfig config;

    public int updateDeposits() {
        final var allAccounts = accountRepository.findAll();
        final var percent = config.depositGrowthStrategy().percent();

        int updatedDeposits = 0;

        for (var account: allAccounts) {
            final BigDecimal oldAmount = account.getDeposit();
            final BigDecimal maxPossibleAmount = account.getMaxDeposit();
            final BigDecimal updatedAmount = oldAmount
                    .multiply(BigDecimal.valueOf(1L + percent / 100))
                    .setScale(2, RoundingMode.HALF_EVEN);
            account.setDeposit(updatedAmount.min(maxPossibleAmount));
            accountRepository.saveWithSerializable(account);
            updatedDeposits += 1;
        }

        return updatedDeposits;
    }
}
