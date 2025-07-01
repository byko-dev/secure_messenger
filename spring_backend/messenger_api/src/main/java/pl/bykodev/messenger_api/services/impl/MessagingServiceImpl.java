package pl.bykodev.messenger_api.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bykodev.messenger_api.database.ConversationEntity;
import pl.bykodev.messenger_api.database.MessageEntity;
import pl.bykodev.messenger_api.database.UserEntity;
import pl.bykodev.messenger_api.database.UserRoleEnum;
import pl.bykodev.messenger_api.exceptions.ResourceNotFoundException;
import pl.bykodev.messenger_api.pojos.SendMessageRequestDTO;
import pl.bykodev.messenger_api.services.*;

import java.util.Optional;

@Service
@AllArgsConstructor
public class MessagingServiceImpl implements MessagingService
{
    private final MessageService messageService;
    private final ConversationService conversationService;
    private final NotificationService notificationService;

    @Override
    public MessageEntity send(String conversationId, SendMessageRequestDTO request) {

        Optional<ConversationEntity> conversationEntity = conversationService.getConversationById(conversationId);

        if (conversationEntity.isEmpty())
            throw new ResourceNotFoundException("Conversation not exist");

        MessageEntity messageEntity = messageService.saveMessageEntity(
                request.getMessageForOwnerStr(),
                request.getMessageForFriendStr(),
                request.getFiles(),
                conversationEntity.get(),
                request.getAuthor());

        UserEntity friendEntity = conversationEntity.get().getUser1().getUsername().equals(request.getAuthor()) ?
                conversationEntity.get().getUser2() : conversationEntity.get().getUser1();

        //skip notification when message receiver is bot ai
        if (friendEntity.getRole() == UserRoleEnum.BOT)
        {
            return messageEntity;
        }

        notificationService.messageNotify(friendEntity, messageEntity);

        return messageEntity;
    }
}