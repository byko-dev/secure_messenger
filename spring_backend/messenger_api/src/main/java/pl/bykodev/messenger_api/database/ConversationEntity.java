package pl.bykodev.messenger_api.database;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "conversations")
public class ConversationEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @ManyToOne
    @JoinColumn
    private UserEntity user1;

    @ManyToOne
    @JoinColumn
    private UserEntity user2;

    public ConversationEntity(String id, UserEntity user1, UserEntity user2) {
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
    }

    public ConversationEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserEntity getUser1() {
        return user1;
    }

    public void setUser1(UserEntity user1) {
        this.user1 = user1;
    }

    public UserEntity getUser2() {
        return user2;
    }

    public void setUser2(UserEntity user2) {
        this.user2 = user2;
    }
}
