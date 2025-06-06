package pl.bykodev.messenger_api.database;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import pl.bykodev.messenger_api.configurations.LongToTimestampConverter;

@Entity
@Table(name = "users")
@Data
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

    @Convert(converter = LongToTimestampConverter.class)
    private long lastTimeActivity;

    @Convert(converter = LongToTimestampConverter.class)

    private long createdAt;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum role = UserRoleEnum.USER;

}
