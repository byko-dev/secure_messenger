package pl.bykodev.messenger_api.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bykodev.messenger_api.database.MessageContentEntity;

@Repository
public interface MessageContentEntityRepository extends JpaRepository<MessageContentEntity, String> {
}

