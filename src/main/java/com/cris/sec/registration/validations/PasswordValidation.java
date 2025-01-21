package com.cris.sec.registration.validations;

import com.cris.sec.registration.model.dtos.UserDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidation implements ConstraintValidator<ValidPassword, Object> {
    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        UserDTO user = (UserDTO) object;
        return user.getPassword() != null
                && user.getMatchingPassword() != null
                && user.getPassword().equals(user.getMatchingPassword());
    }
}
