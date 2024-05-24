package my.solution.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class DepositService {
    private final BankAccountService accountService;

    @Scheduled(fixedDelayString = "#{@depositGrowthStrategy.interval()}")
    public void update() {
        log.info("Deposit updating...");
        var updatedDepositsCount = accountService.updateDeposits();
        log.info("Updated {} deposits", updatedDepositsCount);
    }
}
