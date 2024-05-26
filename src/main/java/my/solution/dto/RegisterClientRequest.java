package my.solution.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import my.solution.dto.validators.PhoneOrEmailMustBeProvided;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Api request to register user")
@PhoneOrEmailMustBeProvided
public class RegisterClientRequest {
    @Schema(description = "New client's login", example = "BestLoginYouEverSeen228")
    @NotBlank(message = "The login is required.")
    String login;

    @Schema(description = "Password for created user")
    @NotBlank(message = "The password is required.")
    String password;    // todo: по-моему так не стоит делать

    @Schema(description = "Account's money value")
    @JsonProperty("initial_deposit")
    BigDecimal initialDeposit = BigDecimal.ZERO;

    @Schema(description = "New client's first name", example = "Leslie")
    @NotBlank(message = "The first name is required.")
    @JsonProperty("first_name")
    String firstName;

    @Schema(description = "New client's last name", example = "Nielsen")
    @NotBlank(message = "The last name is required.")
    @JsonProperty("last_name")
    String lastName;

    @Schema(description = "Patronymic (if exists)")
    String patronymic;

    @Schema(description = "Phone number")
    String phone;

    @Schema(description = "Email address")
    String email;

    @Schema(description = "Date of birth")
    @NotNull(message = "The birth date is required.")
    @JsonProperty("birth_date")
    OffsetDateTime birthDate;

    @Override
    public String toString() {
        return "RegisterClientRequest{"
                + "login='" + login + '\''
                + ", password='" + (password != null ? "*".repeat(password.length()) : null) + '\''
                + ", firstName='" + firstName + '\''
                + ", lastName='" + lastName + '\''
                + ", patronymic='" + patronymic + '\''
                + ", phone='" + phone + '\''
                + ", email='" + email + '\''
                + ", birthDate=" + birthDate
                + '}';
    }
}
