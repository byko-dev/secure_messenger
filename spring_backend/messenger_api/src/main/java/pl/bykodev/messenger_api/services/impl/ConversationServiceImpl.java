package pl.bykodev.messenger_api.services.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.bykodev.messenger_api.database.ConversationEntity;
import pl.bykodev.messenger_api.database.UserEntity;
import pl.bykodev.messenger_api.database.repository.ConversationEntityRepository;
import pl.bykodev.messenger_api.pojos.Friend;
import pl.bykodev.messenger_api.services.ConversationService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final ConversationEntityRepository conversationRepository;

    public Optional<ConversationEntity> getConversationById(String id){
        return conversationRepository.findById(id);
    }

    public Optional<ConversationEntity> isConversationExists(UserEntity user1, UserEntity user2){
        return conversationRepository.findConversation(user1.getId(), user2.getId());
    }

    public ConversationEntity saveRelation(UserEntity owner, UserEntity friend)
    {
        ConversationEntity conversationEntity = new ConversationEntity();
        conversationEntity.setUser1(owner);
        conversationEntity.setUser2(friend);
        return conversationRepository.save(conversationEntity);
    }

    public Friend createRelation(UserEntity owner, UserEntity friend){
        ConversationEntity conversationEntity = new ConversationEntity();
        conversationEntity.setUser1(owner);
        conversationEntity.setUser2(friend);
        return convertConversationEntityToFriend(conversationRepository.save(conversationEntity), owner);
    }

    @Transactional
    public List<Friend> getAllFriends(UserEntity user){
        List<Friend> friendList = new ArrayList<>();

        conversationRepository.findFriends(user.getId()).forEach((conversationEntity ->
                friendList.add(convertConversationEntityToFriend(conversationEntity, user))));

        return friendList;
    }

    public Friend convertConversationEntityToFriend(ConversationEntity conversationEntity, UserEntity userEntity){
        Friend friend = new Friend();

        UserEntity friendEntity = conversationEntity.getUser1().getUsername().equals(userEntity.getUsername()) ?
                conversationEntity.getUser2() : conversationEntity.getUser1();

        friend.setFriendId(friendEntity.getId());
        friend.setConversationId(conversationEntity.getId());
        friend.setUserLastTimeActivity(friendEntity.getLastTimeActivity());
        friend.setPhotoId(friendEntity.getUserPhoto() != null ?
                friendEntity.getUserPhoto().getId(): "");
        friend.setPublicKey(friendEntity.getPublicKey());
        friend.setUsername(friendEntity.getUsername());
        friend.setCustomUsername(friendEntity.getCustomUsername());
        friend.setUserDescription(friendEntity.getUserDescription());
        friend.setAccountCreatedAt(friendEntity.getCreatedAt());
        friend.setRole(friendEntity.getRole());
        return friend;
    }

    public void updateTimestamp(ConversationEntity conversationEntity)
    {
        conversationEntity.setUpdatedAt(LocalDateTime.now());
        conversationRepository.save(conversationEntity);
    }

}
