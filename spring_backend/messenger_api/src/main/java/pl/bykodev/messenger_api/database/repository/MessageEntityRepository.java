package pl.bykodev.messenger_api.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.bykodev.messenger_api.database.MessageEntity;

import java.util.List;

@Repository
public interface MessageEntityRepository extends JpaRepository<MessageEntity, String> {

    @Query(value = "SELECT * FROM messages t WHERE t.conversation_id = :id ORDER BY t.date DESC LIMIT :limit OFFSET :from ", nativeQuery = true)
    List<MessageEntity> findByConversation(@Param("id") String conversation_id, @Param("from") Integer from, @Param("limit") Integer limit);
}
