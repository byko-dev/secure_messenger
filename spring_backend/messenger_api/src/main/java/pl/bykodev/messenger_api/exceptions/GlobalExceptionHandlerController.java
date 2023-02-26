package pl.bykodev.messenger_api.exceptions;

import jakarta.servlet.http.HttpServletRequest;;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import pl.bykodev.messenger_api.pojos.Status;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Status handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        exception.getBindingResult().getFieldErrors().forEach(e -> {
            log.error(String.format("Request path => %s, validation request failed on => %s, POJO => %s, message => %s",
                    request.getServletPath(), e.getField(), e.getObjectName(), e.getDefaultMessage()));
        });
        return new Status(exception.getBindingResult().getFieldErrors().get(0).getDefaultMessage(), request.getServletPath());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Status handleBadRequestException(BadRequestException exception, HttpServletRequest request){
        log.error(String.format("BadRequestException => %s, request path => %s", exception.getMessage(), request.getServletPath()));
        return new Status(exception.getMessage(), request.getServletPath());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Status resourceNotFoundExceptionHandler(ResourceNotFoundException exception, HttpServletRequest request){
        log.error(String.format("ResourceNotFoundException => %s, request path => %s", exception.getMessage(), request.getServletPath()));
        return new Status(exception.getMessage(), request.getServletPath());
    }

    @ExceptionHandler(ResourceNotSavedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Status resourceNotSavedExceptionHandler(ResourceNotSavedException exception, HttpServletRequest request){
        log.error(String.format("ResourceNotSavedException => %s, caused => %s, request path => %s",
                exception.getMessage(), exception.getCause().getMessage(), request.getServletPath()));
        return new Status(exception.getMessage(), request.getServletPath());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Status unauthorizedActionExceptionHandler(UnauthorizedException exception, HttpServletRequest request){
        log.error(String.format("UnauthorizedException => %s, request path => %s", exception.getMessage(), request.getServletPath()));
        return new Status(exception.getMessage(), request.getServletPath());
    }

    @ExceptionHandler(EncryptionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Status encryptionExceptionHandler(EncryptionException exception, HttpServletRequest request){
        log.error(String.format("EncryptionException => %s, caused => %s, request path => %s",
                exception.getMessage(), exception.getCause().getMessage(), request.getServletPath()));
        return new Status(exception.getMessage(), request.getServletPath());
    }
    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(value = HttpStatus.PAYLOAD_TOO_LARGE)
    public Status handleMultipartException(MultipartException ex, HttpServletRequest request) {
        log.error(String.format("MultipartException => %s, caused => %s, request path => %s",
                ex.getMessage(), ex.getCause().getMessage(), request.getServletPath()));

        return new Status("Requested file is too large", request.getServletPath());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Status handle(ConstraintViolationException exception, HttpServletRequest request) {
        return new Status(exception.getMessage(), request.getServletPath());
    }
}
