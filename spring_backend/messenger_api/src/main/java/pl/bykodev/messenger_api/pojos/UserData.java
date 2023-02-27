package pl.bykodev.messenger_api.pojos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
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
