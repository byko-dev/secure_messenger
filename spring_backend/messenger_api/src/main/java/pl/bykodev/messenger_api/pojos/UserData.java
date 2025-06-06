package pl.bykodev.messenger_api.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserData {
    private String id;
    private String username;
    private String photoId;
    private long lastTimeActive;
    private String publicKey;
    public String customUsername;
    public String userDescription;
    private long accountCreatedAt;
}
