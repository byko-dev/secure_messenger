package pl.bykodev.messenger_api.services.impl;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import pl.bykodev.messenger_api.database.MessageContentEntity;
import pl.bykodev.messenger_api.database.MessageEntity;
import pl.bykodev.messenger_api.database.UserEntity;
import pl.bykodev.messenger_api.database.UserRoleEnum;
import pl.bykodev.messenger_api.encryption.RSA;
import pl.bykodev.messenger_api.encryption.RsaKeyUtils;
import pl.bykodev.messenger_api.exceptions.AiException;
import pl.bykodev.messenger_api.pojos.SendMessageRequestDTO;
import pl.bykodev.messenger_api.services.AIBotService;
import pl.bykodev.messenger_api.services.MessagingService;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;

@Service
public class AIBotServiceImpl implements AIBotService {

    private final ChatClient chatClient;
    private final RSA rsa;
    private final MessagingService messagingService;

    public AIBotServiceImpl(ChatClient.Builder chatClient, RSA rsa, MessagingService messagingService)
    {
        this.chatClient = chatClient.build();
        this.rsa = rsa;
        this.messagingService = messagingService;
    }

    @Override
    public void reply(MessageEntity entity, String message) {
        try {

            MessageContentEntity messageForAgent = getBotReceiver(entity, entity.getAuthor());

            if (messageForAgent == null || !messageForAgent.getOwner().getRole().equals(UserRoleEnum.BOT))
            {
                return;
            }

            UserEntity friendEntity = getUserReceiver(entity, entity.getAuthor());

            if (friendEntity == null)
            {
                return;
            }

            Prompt prompt = (new Prompt())
                    .mutate()
                    .content("Required short answer - max 200 chars: " + message)
                    .messages().build();

            ChatClient.ChatClientRequestSpec requestSpec = chatClient.prompt(prompt);
            String response = requestSpec.call().content();

            System.out.println(response);

            response = formatResponse(response);

            SendMessageRequestDTO messageRequestDTO = new SendMessageRequestDTO();
            messageRequestDTO.setAuthor(messageForAgent.getOwner().getUsername());

            messageRequestDTO.setMessageForOwnerStr(response);
            messageRequestDTO.setMessageForFriendStr(response);

            messagingService.send(
                    entity.getConversation().getId(),
                    getSendMessageDTO(messageForAgent.getOwner(), friendEntity, response)
            );

        }
        catch (Exception e)
        {
            throw new AiException(e.getMessage());
        }
    }

    private MessageContentEntity getBotReceiver(MessageEntity messageEntity, String author)
    {
        for (MessageContentEntity messageContent : messageEntity.getMessageContent() )
        {
            if (!messageContent.getOwner().getUsername().equals(author) && messageContent.getOwner().getRole() == UserRoleEnum.BOT)
            {
                return messageContent;
            }
        }

        return null;
    }

    private UserEntity getUserReceiver(MessageEntity message, String author)
    {
        for (MessageContentEntity messageContent : message.getMessageContent() )
        {
            if (messageContent.getOwner().getUsername().equals(author))
            {
                return messageContent.getOwner();
            }
        }

        return null;
    }

    private SendMessageRequestDTO getSendMessageDTO(UserEntity owner, UserEntity friend, String content) throws Exception
    {
        SendMessageRequestDTO messageRequestDTO = new SendMessageRequestDTO();
        messageRequestDTO.setAuthor(owner.getUsername());

        PublicKey ownerPublicKey = RsaKeyUtils.convertStringToPublicKey(owner.getPublicKey());
        String decryptedContentForOwner = rsa.encrypt(content, ownerPublicKey);
        messageRequestDTO.setMessageForOwnerStr(decryptedContentForOwner);

        PublicKey friendPublicKey = RsaKeyUtils.convertStringToPublicKey(friend.getPublicKey());
        String decryptedContentForFriend = rsa.encrypt(content, friendPublicKey);
        messageRequestDTO.setMessageForFriendStr(decryptedContentForFriend);

        return messageRequestDTO;
    }

    private String formatResponse(String botResponse)
    {
        byte[] data = botResponse.getBytes(StandardCharsets.UTF_8);
        if (data.length > 245)
        {
            return new String(data, 0, 245, StandardCharsets.UTF_8);
        }

        return botResponse;
    }
}
