package ru.alex9043.productservice.error.mapper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import ru.alex9043.productservice.error.dto.ErrorResponse;
import ru.alex9043.productservice.error.dto.ValidationError;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ErrorMapper {
    public ErrorResponse toResponse(HttpStatus status, String message, String path, List<ValidationError> errors) {
        return new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                errors);
    }

    public ValidationError toValidationError(FieldError fieldError) {
        return new ValidationError(
                fieldError.getField(),
                fieldError.getDefaultMessage(),
                fieldError.getRejectedValue().toString());
    }
}
