package pl.bykodev.messenger_api.exceptions;

public class ResourceNotFoundException extends AppRuntimeException{
    public ResourceNotFoundException(String message){
        super(message);
    }
}
