package my.solution.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Auth request")
public class ClientSignInRequest {
    @Schema(description = "Client's login")
    @NotBlank(message = "Login must be provided!")
    private String login;

    @Schema(description = "Client's password")
    @NotBlank(message = "Password must be provided!")
    private String password;
}
