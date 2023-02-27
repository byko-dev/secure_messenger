package pl.bykodev.messenger_api.database;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.Nullable;

import java.util.List;

@Entity
@Table(name = "messages")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class MessageEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @ManyToOne
    @JoinColumn
    private ConversationEntity conversation;

    @JoinColumn
    @OneToMany
    private List<MessageContentEntity> messageContent;

    private String author;

    private long date;

    @Nullable
    @OneToOne
    @JoinColumn
    private FileEntity file;
}
