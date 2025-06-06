package pl.bykodev.messenger_api.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Friend {
    private String friendId;
    private String photoId;
    private String publicKey;
    private String conversationId;
    private String username;
    private long userLastTimeActivity;
    private String customUsername;
    private String userDescription;
    private long accountCreatedAt;
}
