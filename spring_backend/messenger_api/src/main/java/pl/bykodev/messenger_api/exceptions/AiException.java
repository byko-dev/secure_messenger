package pl.bykodev.messenger_api.exceptions;

public class AiException extends AppRuntimeException {

    public AiException(String message)
    {
        super(message);
    }

    public AiException()
    {
        super("Failed to get response from AI service");
    }
}