package pl.bykodev.messenger_api.exceptions;

public class UnauthorizedException extends AppRuntimeException{
    public UnauthorizedException(String message){
        super(message);
    }
}
