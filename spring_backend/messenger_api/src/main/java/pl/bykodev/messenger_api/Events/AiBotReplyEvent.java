package pl.bykodev.messenger_api.Events;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AiBotReplyEvent
{
    private String message;
    private String messageEntityId;
}