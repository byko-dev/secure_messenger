package pl.bykodev.messenger_api.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bykodev.messenger_api.database.MessageContentEntity;
import pl.bykodev.messenger_api.database.UserEntity;
import pl.bykodev.messenger_api.database.repository.MessageContentEntityRepository;
import pl.bykodev.messenger_api.services.MessageContentService;

@Service
@AllArgsConstructor
public class MessageContentServiceImpl implements MessageContentService {

    private final MessageContentEntityRepository messageContentRepository;

    public MessageContentEntity save(String content, UserEntity user){
        MessageContentEntity entity = new MessageContentEntity();
        entity.setMessage(content);
        entity.setOwner(user);

        return messageContentRepository.save(entity);
    }

}
