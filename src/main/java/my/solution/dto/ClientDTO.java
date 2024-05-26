package my.solution.dto;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
