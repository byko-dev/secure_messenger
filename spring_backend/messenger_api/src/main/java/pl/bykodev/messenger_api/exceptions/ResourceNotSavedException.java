package pl.bykodev.messenger_api.exceptions;

public class ResourceNotSavedException extends AppRuntimeException{
    public ResourceNotSavedException(String message, Throwable cause) {
        super(message, cause);
    }
}
