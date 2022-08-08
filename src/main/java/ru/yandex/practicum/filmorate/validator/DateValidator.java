package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateValidator implements ConstraintValidator<DateAfter, LocalDate> {
    private LocalDate minDate;
    public void initialize(DateAfter constraint) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        minDate = LocalDate.parse(constraint.minDate(), formatter);
    }
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return !value.isBefore(minDate);
    }
}
