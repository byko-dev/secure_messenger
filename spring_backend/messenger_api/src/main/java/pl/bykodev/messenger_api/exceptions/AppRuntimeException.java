package pl.bykodev.messenger_api.exceptions;

public class AppRuntimeException extends RuntimeException{

    public AppRuntimeException(String message){
        super(message);
    }
    public AppRuntimeException(String message, Throwable cause){
        super(message, cause);
    }
}
