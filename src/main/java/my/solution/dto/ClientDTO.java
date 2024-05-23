package my.solution.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {
    String login;
    String firstName;
    String lastName;
    String patronymic;
    String phone;
    String email;
    OffsetDateTime birthDate;
}
