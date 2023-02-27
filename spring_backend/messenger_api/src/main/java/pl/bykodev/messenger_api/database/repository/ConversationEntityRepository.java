package pl.bykodev.messenger_api.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.bykodev.messenger_api.database.ConversationEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationEntityRepository extends JpaRepository<ConversationEntity, String> {
    Optional<ConversationEntity> findById(String id);

    @Query(value = "SELECT * FROM conversations t WHERE t.user1_id = :user_id OR t.user2_id = :user_id", nativeQuery = true)
    List<ConversationEntity> findFriends(@Param("user_id") String userId);

    @Query(value = "SELECT * FROM conversations t WHERE (t.user1_id = :user_id1 AND t.user2_id = :user_id2) OR (t.user2_id = :user_id1 AND t.user1_id = :user_id2)", nativeQuery = true)
    Optional<ConversationEntity> findConversation(@Param("user_id1") String userId1, @Param("user_id2") String userId2);
}
