package pl.bykodev.messenger_api.services;

import pl.bykodev.messenger_api.database.UserEntity;
import pl.bykodev.messenger_api.pojos.*;

import java.util.List;
import java.util.Optional;

public interface UserService {

    boolean userIsPresent(String username);
    Optional<UserEntity> getUserById(String id);
    void saveUserData(String authToken, UpdateUserData data);
    UserEntity createUser(RegisterRequest data);
    UserData getUserData(UserEntity userEntity);
    UserEntity getUser(String authorizationHeaderContent);
    void updateUserActivity(String jwtToken);
    RsaKeys getUserRsaKeys(Password password, String authToken);
    List<UserData> getUsers(String userId, String search);
}
