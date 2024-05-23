package my.solution.dto.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import my.solution.dto.RegisterClientRequest;

public class PhoneOrEmailValidator implements ConstraintValidator<PhoneOrEmailMustBeProvided, RegisterClientRequest> {
    @Override
    public boolean isValid(RegisterClientRequest dto, ConstraintValidatorContext context) {
        return dto.getPhone() != null && !dto.getPhone().isEmpty()
                || dto.getEmail() != null && !dto.getEmail().isEmpty();
    }
}
