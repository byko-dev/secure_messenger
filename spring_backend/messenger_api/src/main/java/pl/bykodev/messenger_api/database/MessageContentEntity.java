package pl.bykodev.messenger_api.database;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Data
@Entity
@Table(name = "messagesContent")
public class MessageContentEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Lob
    private String message;

    @ManyToOne
    @JoinColumn
    private UserEntity owner;
}
