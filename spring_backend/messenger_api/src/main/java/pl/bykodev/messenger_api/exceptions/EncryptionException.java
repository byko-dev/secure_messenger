package pl.bykodev.messenger_api.exceptions;

public class EncryptionException extends AppRuntimeException{
    public EncryptionException(String message, Throwable cause){
        super(message, cause);
    }
}
