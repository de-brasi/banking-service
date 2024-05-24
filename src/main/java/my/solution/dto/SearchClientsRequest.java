package my.solution.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchClientsRequest {
    @NotBlank(message = "Field for search is required.")
    @Pattern(regexp = "birth_date|phone_number|email_address|full_name",
            message = "Role must be either birth_date, phone_number, email_address or full_name")
    @JsonProperty("field")
    String fieldName;

    @JsonProperty("date_value")
    OffsetDateTime pivotDate;

    @JsonProperty("phone_value")
    String phoneSample;

    @JsonProperty("full_name_value")
    String fullNameSample;

    @JsonProperty("email_value")
    String emailSample;

    @JsonProperty("limit")
    Integer responseLimit = 1;

    @JsonProperty("sorted")
    boolean sorted = false;

    @Pattern(regexp = "asc|desc",
            message = "Sorting order must be either asc or desc")
    @JsonProperty("sorting_order")
    String sortingOrder = "asc";
}
