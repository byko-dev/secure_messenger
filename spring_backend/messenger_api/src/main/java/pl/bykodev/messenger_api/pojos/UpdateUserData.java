package pl.bykodev.messenger_api.pojos;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateUserData {

    @Nullable
    private MultipartFile file;

    @Nullable
    @Size(min = 4, max = 32, message = "Custom username miss requirements of 4-32 characters")
    private String customUsername;

    @Nullable
    @Size(max = 256, message = "Description miss requirements of 256 characters maximum")
    private String description;
}
