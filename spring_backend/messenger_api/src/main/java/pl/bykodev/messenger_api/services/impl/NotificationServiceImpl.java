package pl.bykodev.messenger_api.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pl.bykodev.messenger_api.database.MessageEntity;
import pl.bykodev.messenger_api.database.UserEntity;
import pl.bykodev.messenger_api.pojos.AlertDTO;
import pl.bykodev.messenger_api.services.ConversationService;
import pl.bykodev.messenger_api.services.MessageService;
import pl.bykodev.messenger_api.services.NotificationService;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final SimpMessagingTemplate template;
    private final MessageService messageService;
    private final ConversationService conversationService;

    @Override
    public void messageNotify(UserEntity friend, MessageEntity messageEntity)
    {
        template.convertAndSendToUser(friend.getUsername(),
                "/conversation/" + messageEntity.getConversation().getId(),
                messageService.convertMessageEntity(messageEntity, friend.getUsername()));

        template.convertAndSendToUser(friend.getUsername(),
                "/alert",
                (new AlertDTO())
                        .setNewMessageAction()
                        .setData(conversationService.convertConversationEntityToFriend(messageEntity.getConversation(), friend)));
    }
}
