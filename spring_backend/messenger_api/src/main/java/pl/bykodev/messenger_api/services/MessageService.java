package pl.bykodev.messenger_api.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.bykodev.messenger_api.database.ConversationEntity;
import pl.bykodev.messenger_api.database.MessageContentEntity;
import pl.bykodev.messenger_api.database.MessageEntity;
import pl.bykodev.messenger_api.database.repository.MessageEntityRepository;
import pl.bykodev.messenger_api.pojos.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MessageService {

    private MessageEntityRepository messageRepository;
    private FileService fileService;
    private MessageContentService messageContentService;

    public MessageService(MessageEntityRepository messageRepository, FileService fileService, MessageContentService messageContentService) {
        this.messageRepository = messageRepository;
        this.fileService = fileService;
        this.messageContentService = messageContentService;
    }

    public MessageEntity saveMessageEntity(String messageForOwner, String messageForFriend, MultipartFile file, ConversationEntity conversation, String author){
        MessageEntity messageEntity = new MessageEntity();

        List<MessageContentEntity> messageContentEntities = new ArrayList<>();

        messageContentEntities.add(messageContentService.save(messageForOwner,
                conversation.getUser1().getUsername().equals(author) ?
                        conversation.getUser1() : conversation.getUser2()));

        messageContentEntities.add(messageContentService.save(messageForFriend,
                !conversation.getUser1().getUsername().equals(author) ?
                        conversation.getUser1(): conversation.getUser2()));

        messageEntity.setMessageContent(messageContentEntities);
        messageEntity.setDate(System.currentTimeMillis());
        messageEntity.setConversation(conversation);
        messageEntity.setAuthor(author);
        try {
            messageEntity.setFile(file != null ? fileService.save(file): null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return messageRepository.save(messageEntity);
    }

    @Transactional
    public List<Message> getMessages(ConversationEntity conversation, int from, String username){
        List<Message> messageList = new ArrayList<>();

        messageRepository.findByConversation(conversation.getId(), from, 10).forEach((messageEntity ->
                messageList.add(convertMessageEntity(messageEntity, username))));

        Collections.reverse(messageList);

        return messageList;
    }

    public Message convertMessageEntity(MessageEntity messageEntity, String username){
        Message message = new Message();

        message.setAuthor(messageEntity.getAuthor());
        message.setId(messageEntity.getId());
        message.setContent(messageEntity.getMessageContent().stream().parallel()
                .filter(query -> query.getOwner().getUsername().equals(username)).findFirst().get().getMessage());
        message.setFileId(messageEntity.getFile() != null? messageEntity.getFile().getId(): "");
        message.setFileType(messageEntity.getFile() != null? messageEntity.getFile().getContentType(): "");
        message.setFileName(messageEntity.getFile() != null? messageEntity.getFile().getName(): "");
        message.setDate(messageEntity.getDate());

        return message;
    }
}
