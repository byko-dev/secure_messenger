package pl.bykodev.messenger_api.database;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class UserEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String username;

    /* hashed by bcrypt */
    private String password;

    private String customUsername;
    private String userDescription;

    @Column(name = "publicKey", columnDefinition = "text")
    private String publicKey;

    /* encrypted by RSA256, password to encryption is hashed in database*/
    @Column(name = "privateKey", columnDefinition = "text")
    private String privateKey;

    @OneToOne
    @JoinColumn
    private FileEntity userPhoto;

    private long lastTimeActivity;
    private long createdAt;
}
