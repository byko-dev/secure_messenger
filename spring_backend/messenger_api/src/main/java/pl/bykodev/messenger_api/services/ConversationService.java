package pl.bykodev.messenger_api.services;

import org.springframework.stereotype.Service;
import pl.bykodev.messenger_api.database.ConversationEntity;
import pl.bykodev.messenger_api.database.UserEntity;
import pl.bykodev.messenger_api.pojos.Friend;
import java.util.List;
import java.util.Optional;

@Service
public interface ConversationService {

    Optional<ConversationEntity> getConversationById(String id);
    Optional<ConversationEntity> isConversationExists(UserEntity user1, UserEntity user2);
    ConversationEntity saveRelation(UserEntity owner, UserEntity friend);
    Friend createRelation(UserEntity owner, UserEntity friend);
    List<Friend> getAllFriends(UserEntity user);
    Friend convertConversationEntityToFriend(ConversationEntity conversationEntity, UserEntity userEntity);
    void updateTimestamp(ConversationEntity conversationEntity);
}