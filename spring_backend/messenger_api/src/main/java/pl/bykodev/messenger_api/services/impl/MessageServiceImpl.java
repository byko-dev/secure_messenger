package pl.bykodev.messenger_api.services.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.bykodev.messenger_api.database.ConversationEntity;
import pl.bykodev.messenger_api.database.FileEntity;
import pl.bykodev.messenger_api.database.MessageContentEntity;
import pl.bykodev.messenger_api.database.MessageEntity;
import pl.bykodev.messenger_api.database.repository.MessageEntityRepository;
import pl.bykodev.messenger_api.pojos.File;
import pl.bykodev.messenger_api.pojos.Message;
import pl.bykodev.messenger_api.services.ConversationService;
import pl.bykodev.messenger_api.services.FileService;
import pl.bykodev.messenger_api.services.MessageContentService;
import pl.bykodev.messenger_api.services.MessageService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    private MessageEntityRepository messageRepository;
    private FileService fileService;
    private MessageContentService messageContentService;
    private ConversationService conversationService;

    public MessageEntity saveMessageEntity(String messageForOwner, String messageForFriend, MultipartFile[] files, ConversationEntity conversation, String author){
        MessageEntity messageEntity = new MessageEntity();

        List<MessageContentEntity> messageContentEntities = new ArrayList<>();

        messageContentEntities.add(messageContentService.save(messageForOwner,
                conversation.getUser1().getUsername().equals(author) ?
                        conversation.getUser1() : conversation.getUser2()));

        messageContentEntities.add(messageContentService.save(messageForFriend,
                !conversation.getUser1().getUsername().equals(author) ?
                        conversation.getUser1(): conversation.getUser2()));

        conversationService.updateTimestamp(conversation);

        messageEntity.setMessageContent(messageContentEntities);
        messageEntity.setDate(System.currentTimeMillis());
        messageEntity.setConversation(conversation);
        messageEntity.setAuthor(author);
        try {

            if (files != null){

                List<FileEntity> fileEntityList = new ArrayList<>();

                for (MultipartFile multipartFile : files) {
                    fileEntityList.add(fileService.save(multipartFile));
                }

                messageEntity.setFile(fileEntityList);
            }
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

        if (messageEntity.getFile() != null) {
            List<File> files = new ArrayList<>();

            for(FileEntity entity : messageEntity.getFile()) {
                files.add(new File(entity.getId(), entity.getContentType(), entity.getName()));
            }

            message.setFiles(files);
        }

        message.setDate(messageEntity.getDate());

        return message;
    }

}
