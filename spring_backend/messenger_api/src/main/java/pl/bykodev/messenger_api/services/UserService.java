package pl.bykodev.messenger_api.services;

import org.springframework.web.multipart.MultipartFile;
import pl.bykodev.messenger_api.database.UserEntity;
import pl.bykodev.messenger_api.pojos.Password;
import pl.bykodev.messenger_api.pojos.RegisterRequest;
import pl.bykodev.messenger_api.pojos.RsaKeys;
import pl.bykodev.messenger_api.pojos.UserData;
import java.util.List;
import java.util.Optional;

public interface UserService {

    boolean userIsPresent(String username);
    Optional<UserEntity> getUserById(String id);
    void saveUserData(String authToken, MultipartFile file, String customUsername, String description);
    UserEntity createUser(RegisterRequest data);
    UserData getUserData(UserEntity userEntity);
    UserEntity getUser(String authorizationHeaderContent);
    void updateUserActivity(String jwtToken);
    RsaKeys getUserRsaKeys(Password password, String authToken);
    List<UserData> getUsers(String userId, String search);
}
