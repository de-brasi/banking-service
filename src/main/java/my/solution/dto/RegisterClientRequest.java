package my.solution.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import my.solution.dto.validators.PhoneOrEmailMustBeProvided;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@PhoneOrEmailMustBeProvided
public class RegisterClientRequest {
    @NotBlank(message = "The login is required.")
    String login;

    @NotBlank(message = "The password is required.")
    String password;    // todo: по-моему так не стоит делать

    @NotBlank(message = "The first name is required.")
    @JsonProperty("first_name")
    String firstName;

    @NotBlank(message = "The last name is required.")
    @JsonProperty("last_name")
    String lastName;

    String patronymic;

    String phone;
    String email;

    @NotNull(message = "The birth date is required.")
    @JsonProperty("birth_date")
    OffsetDateTime birthDate;

    @Override
    public String toString() {
        return "RegisterClientRequest{" +
                "login='" + login + '\'' +
                ", password='" + (password != null ? "*".repeat(password.length()) : null) + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}
