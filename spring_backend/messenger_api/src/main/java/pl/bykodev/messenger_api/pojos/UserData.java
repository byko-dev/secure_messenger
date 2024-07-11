package pl.bykodev.messenger_api.pojos;

public class UserData {
    private String id;
    private String username;
    private String photoId;
    private long lastTimeActive;
    private String publicKey;
    public String customUsername;
    public String userDescription;
    private long accountCreatedAt;

    public UserData(String id, String username, String photoId, long lastTimeActive, String publicKey, String customUsername, String userDescription, long accountCreatedAt) {
        this.id = id;
        this.username = username;
        this.photoId = photoId;
        this.lastTimeActive = lastTimeActive;
        this.publicKey = publicKey;
        this.customUsername = customUsername;
        this.userDescription = userDescription;
        this.accountCreatedAt = accountCreatedAt;
    }

    public UserData() {
    }

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

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public long getLastTimeActive() {
        return lastTimeActive;
    }

    public void setLastTimeActive(long lastTimeActive) {
        this.lastTimeActive = lastTimeActive;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
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

    public long getAccountCreatedAt() {
        return accountCreatedAt;
    }

    public void setAccountCreatedAt(long accountCreatedAt) {
        this.accountCreatedAt = accountCreatedAt;
    }
}
