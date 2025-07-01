package pl.bykodev.messenger_api.pojos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.bykodev.messenger_api.database.UserRoleEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotNull
    @Pattern(regexp = "^.[^\s]{5,32}$", message = "Username miss requirements of 6-32 characters")
    private String username;
    @NotNull
    @Pattern(regexp = "^.[^\s]{5,32}$", message = "Password miss requirements of 6-32 characters")
    private String password;
    @NotNull
    @Size(max = 64)
    private String secureRandom;

    @NotNull
    private UserRoleEnum roleEnum;
}
