package pl.bykodev.messenger_api.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bykodev.messenger_api.database.MessageContentEntity;
import pl.bykodev.messenger_api.database.UserEntity;
import pl.bykodev.messenger_api.database.repository.MessageContentEntityRepository;

@Service
@AllArgsConstructor
public class MessageContentService {

    private MessageContentEntityRepository messageContentRepository;

    public MessageContentEntity save(String content, UserEntity user){
        MessageContentEntity entity = new MessageContentEntity();
        entity.setMessage(content);
        entity.setOwner(user);

        return messageContentRepository.save(entity);
    }
}
