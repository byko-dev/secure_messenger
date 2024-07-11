package pl.bykodev.messenger_api.database;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

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


    public MessageContentEntity(String id, String message, UserEntity owner) {
        this.id = id;
        this.message = message;
        this.owner = owner;
    }

    public MessageContentEntity() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }
}
