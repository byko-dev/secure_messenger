package pl.bykodev.messenger_api.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.bykodev.messenger_api.database.ConversationEntity;
import pl.bykodev.messenger_api.database.UserEntity;
import pl.bykodev.messenger_api.exceptions.ResourceNotFoundException;
import pl.bykodev.messenger_api.pojos.*;
import pl.bykodev.messenger_api.services.ConversationService;
import pl.bykodev.messenger_api.services.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@Validated
@CrossOrigin(origins = "*") /* all origins are allowed, only developed purpose */
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final ConversationService conversationService;
    private final SimpMessagingTemplate simpMessagingTemplate;


    public UserController(UserService userService, ConversationService conversationService, SimpMessagingTemplate simpMessagingTemplate) {
        this.userService = userService;
        this.conversationService = conversationService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @GetMapping("/valid")
    public ResponseEntity<Status> valid(HttpServletRequest request){
        return ResponseEntity.ok(new Status("OK", request.getServletPath()));
    }

    @GetMapping
    public ResponseEntity<UserData> getUserData(@RequestHeader("Authorization") String authHeader){
        UserEntity user = userService.getUser(authHeader);
        return ResponseEntity.ok(userService.getUserData(user));
    }

    @PostMapping("/keys")
    public ResponseEntity<?> getPrivateKey(@Valid @RequestBody Password password, @RequestHeader("Authorization") String authHeader){
        return ResponseEntity.ok(userService.getUserRsaKeys(password, authHeader));
    }

    @PostMapping("/add/friend")
    public ResponseEntity<Friend> addFriend(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody UserID userID){
        UserEntity user = userService.getUser(authHeader);
        Optional<UserEntity> friend = userService.getUserById(userID.getId());

        if(friend.isEmpty() || conversationService.isConversationExists(user, friend.get()).isPresent())
            throw new ResourceNotFoundException("Friend was not found");

        ConversationEntity conversationEntity = conversationService.saveRelation(user, friend.get());

        simpMessagingTemplate.convertAndSendToUser(friend.get().getUsername(),
                "/alert",
                (new AlertDTO())
                        .setNewFriendAction()
                        .setData(conversationService.convertConversationEntityToFriend(conversationEntity, friend.get())));

        return ResponseEntity.ok(conversationService.convertConversationEntityToFriend(conversationEntity, user));
    }

    @GetMapping("/friends")
    public ResponseEntity<List<Friend>> getAllFriends(@RequestHeader("Authorization") String authHeader) {
        UserEntity user = userService.getUser(authHeader);
        return ResponseEntity.ok(conversationService.getAllFriends(user));
    }

    @PatchMapping("/data")
    public ResponseEntity<Status> updateUserData(@RequestHeader("Authorization") String authHeader,
                                                 @Nullable @RequestParam(required = false) MultipartFile file,
                                                 @Valid @Size(min = 4, max = 32, message = "Custom username miss requirements of 4-32 characters")
                                                 @Nullable @RequestParam(required = false) String customUsername,
                                                 @Valid @Size(max = 256, message = "Description miss requirements of 256 characters maximum")
                                                 @Nullable @RequestParam(required = false) String description,
                                                 HttpServletRequest request){

        userService.saveUserData(authHeader, file, customUsername, description);
        return ResponseEntity.ok(new Status("User data was updated!", request.getServletPath()));
    }
}