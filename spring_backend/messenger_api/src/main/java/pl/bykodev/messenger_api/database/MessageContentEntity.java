package pl.bykodev.messenger_api.database;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "messagesContent")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
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
