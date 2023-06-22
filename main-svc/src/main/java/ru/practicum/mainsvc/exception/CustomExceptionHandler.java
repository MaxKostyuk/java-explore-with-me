package ru.practicum.mainsvc.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ElementNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError elementNotFoundExceptionHandler(ElementNotFoundException e) {
        return getApiError(HttpStatus.NOT_FOUND, "The required object was not found.", e);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError validationExceptionHandler(Exception e) {
        return getApiError(HttpStatus.BAD_REQUEST, "Incorrectly made request.", e);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiError dataIntegrityViolationExceptionHandler(DataIntegrityViolationException e) {
        return getApiError(HttpStatus.CONFLICT, "Integrity constraint has been violated.", e);
    }

    @ExceptionHandler(ActionForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ApiError actionForbiddenExceptionHandler(ActionForbiddenException e) {
        return getApiError(HttpStatus.FORBIDDEN, "For the requested operation the conditions are not met.", e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiError exceptionHandler(Exception e) {
        return getApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server exception.", e);
    }

    private ApiError getApiError(HttpStatus status, String reason, Exception e) {
        return new ApiError(status.getReasonPhrase(), reason, e.getMessage(), LocalDateTime.now());
    }
}
