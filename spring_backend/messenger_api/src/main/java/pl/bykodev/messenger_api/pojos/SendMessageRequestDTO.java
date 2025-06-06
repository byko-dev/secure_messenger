package pl.bykodev.messenger_api.pojos;


import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class SendMessageRequestDTO {

    @Size(max = 1024, message = "message miss requirements of 1024 characters maximum")
    @Nullable
    private String messageForOwnerStr;

    @Size(max = 1024, message = "message miss requirements of 1024 characters maximum")
    @Nullable
    private String messageForFriendStr;

    @Nullable
    private MultipartFile[] files;

    @Size(min = 4, max = 32, message = "Username miss requirements of 4-32 characters")
    private String author;

    //unencrypted message, optional, required for messaging with bots
    private String message;
}