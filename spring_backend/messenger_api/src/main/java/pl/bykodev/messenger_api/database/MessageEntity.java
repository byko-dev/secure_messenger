package pl.bykodev.messenger_api.database;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.Nullable;

import java.util.List;

@Entity
@Table(name = "messages")
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

    public MessageEntity(String id, ConversationEntity conversation, List<MessageContentEntity> messageContent, String author, long date, @Nullable FileEntity file) {
        this.id = id;
        this.conversation = conversation;
        this.messageContent = messageContent;
        this.author = author;
        this.date = date;
        this.file = file;
    }

    public MessageEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ConversationEntity getConversation() {
        return conversation;
    }

    public void setConversation(ConversationEntity conversation) {
        this.conversation = conversation;
    }

    public List<MessageContentEntity> getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(List<MessageContentEntity> messageContent) {
        this.messageContent = messageContent;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Nullable
    public FileEntity getFile() {
        return file;
    }

    public void setFile(@Nullable FileEntity file) {
        this.file = file;
    }
}
