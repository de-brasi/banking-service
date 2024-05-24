package my.solution.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "deposit-growth-strategy", ignoreUnknownFields = false)
public record DepositGrowthStrategyConfig(
        @NotNull Duration interval,
        Float percent
) {
}
