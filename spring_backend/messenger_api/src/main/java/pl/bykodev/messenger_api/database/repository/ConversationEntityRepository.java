package pl.bykodev.messenger_api.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationEntityRepository extends JpaRepository<ConversationEntity, String> {
}
