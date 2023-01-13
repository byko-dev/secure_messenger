package pl.bykodev.messenger_api.database;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "conversations")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
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
}
