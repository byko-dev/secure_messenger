package pl.bykodev.messenger_api.services.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.bykodev.messenger_api.database.UserEntity;
import pl.bykodev.messenger_api.database.repository.UserEntityRepository;
import pl.bykodev.messenger_api.encryption.Aes256;
import pl.bykodev.messenger_api.encryption.MD5;
import pl.bykodev.messenger_api.encryption.RSA;
import pl.bykodev.messenger_api.exceptions.*;
import pl.bykodev.messenger_api.pojos.*;
import pl.bykodev.messenger_api.security.JwtUtils;
import pl.bykodev.messenger_api.services.FileService;
import pl.bykodev.messenger_api.services.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserEntityRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final FileService fileService;
    private final RSA rsa;

    @Transactional
    public boolean userIsPresent(String username){
        return userRepository.findByUsername(username).isPresent();
    }

    public Optional<UserEntity> getUserById(String id){
        return userRepository.findById(id);
    }


    @Transactional
    public void saveUserData(String authToken, UpdateUserData data)
    {
        if(data.getFile() != null && !data.getFile().getContentType().equals("image/jpeg") && !data.getFile().getContentType().equals("image/png"))
            throw new BadRequestException("User photo requires content type image/jpeg or image/png");

        UserEntity userEntity = getUser(authToken);
        try {
            userEntity.setCustomUsername(data.getCustomUsername());
            userEntity.setUserDescription(data.getDescription());
            if (data.getFile() != null) userEntity.setUserPhoto(fileService.save(data.getFile()));
            userRepository.save(userEntity);
        } catch (IOException e) {
            throw new ResourceNotSavedException("User's photo was not saved", e.getCause());
        }
    }

    public UserEntity createUser(RegisterRequest data) {
        UserEntity userEntity = new UserEntity();

        userEntity.setUsername(data.getUsername());
        userEntity.setCustomUsername(data.getUsername());
        userEntity.setUserDescription("");
        userEntity.setPassword(passwordEncoder.encode(data.getPassword()));
        userEntity.setUserPhoto(null);
        userEntity.setLastTimeActivity(System.currentTimeMillis());
        userEntity.setCreatedAt(System.currentTimeMillis());
        userEntity.setRole(data.getRoleEnum());

        RsaKeys keys = rsa.generateRSAKeys(data.getSecureRandom());
        userEntity.setPublicKey(keys.getPublicKey());

        try {
            Aes256.setDefaultPassword(MD5.getSaltedMD5(data.getPassword()));
            userEntity.setPrivateKey(Aes256.encrypt(keys.getPrivateKey()));
        } catch (Exception e) {
            throw new ResourceNotSavedException("User was not saved", e.getCause());
        }
        return userRepository.save(userEntity);
    }

    @Transactional
    public UserData getUserData(UserEntity userEntity){
        UserData userData = new UserData();
        userData.setUsername(userEntity.getUsername());
        userData.setCustomUsername(userEntity.getCustomUsername());
        userData.setPublicKey(userEntity.getPublicKey());
        userData.setId(userEntity.getId());
        userData.setLastTimeActive(userEntity.getLastTimeActivity());
        userData.setPhotoId(userEntity.getUserPhoto() != null ? userEntity.getUserPhoto().getId() : "");
        userData.setUserDescription(userEntity.getUserDescription());
        userData.setAccountCreatedAt(userEntity.getCreatedAt());
        userData.setRole(userEntity.getRole());

        return userData;
    }

    @Transactional
    public UserEntity getUser(String authorizationHeaderContent){
        final String authorizationHeader = authorizationHeaderContent.substring(7);
        final String username = jwtUtils.extractUsername(authorizationHeader);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User doesn't exist!"));
    }

    @Transactional
    public void updateUserActivity(String jwtToken){
        UserEntity user = getUser(jwtToken);
        if(userIsPresent(user.getUsername())){
            user.setLastTimeActivity(System.currentTimeMillis());
            userRepository.save(user);
        }
    }

    @Transactional
    public RsaKeys getUserRsaKeys(Password password, String authToken){
        UserEntity user = getUser(authToken);

        if(!passwordEncoder.matches(password.getPassword(), user.getPassword()))
            throw new UnauthorizedException("Unauthorized operation");

        try {
            Aes256.setDefaultPassword(MD5.getSaltedMD5(password.getPassword()));
            return new RsaKeys(Aes256.decrypt(user.getPrivateKey()), user.getPublicKey());
        } catch (Exception e) {
            throw new EncryptionException("Encryption error", e.getCause());
        }
    }

    public List<UserData> getUsers(String userId, String search){
        List<UserData> userListResponseList = new ArrayList<>();

        for(UserEntity user : userRepository.findAll().stream().filter(e -> e.getUsername().contains(search)).toList()){
            if(userId.equals(user.getId())) continue;
            userListResponseList.add(getUserData(user));
        }
        return userListResponseList;
    }

}
