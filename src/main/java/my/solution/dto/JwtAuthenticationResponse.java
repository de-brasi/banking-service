package my.solution.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "JWT token's response")
public class JwtAuthenticationResponse {
    @Schema(description = "Access token", example = "eyJhbGciO.eyJzdWIiOiIxMjM0NTY3.SflKxwRJSMeKKF2QT4fw")
    private String jwtToken;
}
