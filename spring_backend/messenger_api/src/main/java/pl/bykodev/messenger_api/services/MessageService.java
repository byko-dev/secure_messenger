package pl.bykodev.messenger_api.services;

import org.springframework.web.multipart.MultipartFile;
import pl.bykodev.messenger_api.database.ConversationEntity;
import pl.bykodev.messenger_api.database.MessageEntity;
import pl.bykodev.messenger_api.pojos.Message;
import java.util.List;


public interface MessageService {

    MessageEntity saveMessageEntity(String messageForOwner, String messageForFriend, MultipartFile[] files, ConversationEntity conversation, String author);
    List<Message> getMessages(ConversationEntity conversation, int from, String username);
    Message convertMessageEntity(MessageEntity messageEntity, String username);
}