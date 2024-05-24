package my.solution.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "bank-service", ignoreUnknownFields = false)
public record BankServiceConfig(
        @Bean("depositGrowthStrategy")
        DepositGrowthStrategy depositGrowthStrategy
) {
    public record DepositGrowthStrategy(@NotNull Duration interval, Float percent) {
    }
}
