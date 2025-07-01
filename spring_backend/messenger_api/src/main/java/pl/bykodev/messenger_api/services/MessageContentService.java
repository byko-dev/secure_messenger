package pl.bykodev.messenger_api.services;

import org.springframework.stereotype.Service;
import pl.bykodev.messenger_api.database.MessageContentEntity;
import pl.bykodev.messenger_api.database.UserEntity;

@Service
public interface MessageContentService {
    MessageContentEntity save(String content, UserEntity user);
}
