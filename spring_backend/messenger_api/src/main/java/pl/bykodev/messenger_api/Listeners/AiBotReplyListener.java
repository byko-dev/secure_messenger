package pl.bykodev.messenger_api.Listeners;

import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.bykodev.messenger_api.Events.AiBotReplyEvent;
import pl.bykodev.messenger_api.database.MessageEntity;
import pl.bykodev.messenger_api.database.repository.MessageEntityRepository;
import pl.bykodev.messenger_api.services.AIBotService;

@Component
@AllArgsConstructor
public class AiBotReplyListener {

    private AIBotService aiBotService;
    private MessageEntityRepository repository;

    @Async
    @EventListener
    @Transactional
    public void handlerAiBotReplyEvent(AiBotReplyEvent event)
    {
        MessageEntity message = repository.findById(event.getMessageEntityId())
                .orElseThrow();

        aiBotService.reply(message, event.getMessage());
    }
}