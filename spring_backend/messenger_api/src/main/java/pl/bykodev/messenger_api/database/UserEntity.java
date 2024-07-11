package pl.bykodev.messenger_api.database;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import pl.bykodev.messenger_api.configurations.LongToTimestampConverter;

@Entity
@Table(name = "users")
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

    public UserEntity(String id, String username, String password, String customUsername, String userDescription, String publicKey, String privateKey, FileEntity userPhoto, long lastTimeActivity, long createdAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.customUsername = customUsername;
        this.userDescription = userDescription;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.userPhoto = userPhoto;
        this.lastTimeActivity = lastTimeActivity;
        this.createdAt = createdAt;
    }

    public UserEntity() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCustomUsername() {
        return customUsername;
    }

    public void setCustomUsername(String customUsername) {
        this.customUsername = customUsername;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public FileEntity getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(FileEntity userPhoto) {
        this.userPhoto = userPhoto;
    }

    public long getLastTimeActivity() {
        return lastTimeActivity;
    }

    public void setLastTimeActivity(long lastTimeActivity) {
        this.lastTimeActivity = lastTimeActivity;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
