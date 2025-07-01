package pl.bykodev.messenger_api.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final ConversationService conversationService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/valid")
    public Status valid(HttpServletRequest request){
        return new Status("OK", request.getServletPath());
    }

    @GetMapping
    public UserData getUserData(@RequestHeader("Authorization") String authHeader){
        UserEntity user = userService.getUser(authHeader);
        return userService.getUserData(user);
    }

    @PostMapping("/keys")
    public RsaKeys getPrivateKey(@Valid @RequestBody Password password, @RequestHeader("Authorization") String authHeader){
        return userService.getUserRsaKeys(password, authHeader);
    }

    @PostMapping("/add/friend")
    public Friend addFriend(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody UserID userID){
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

        return conversationService.convertConversationEntityToFriend(conversationEntity, user);
    }

    @GetMapping("/friends")
    public List<Friend> getAllFriends(@RequestHeader("Authorization") String authHeader) {
        UserEntity user = userService.getUser(authHeader);
        return conversationService.getAllFriends(user);
    }

    @PatchMapping("/data")
    public Status updateUserData(@RequestHeader("Authorization") String authHeader,
                                 @Valid @ModelAttribute UpdateUserData data,
                                 HttpServletRequest request){

        userService.saveUserData(authHeader, data);
        return new Status("User data was updated!", request.getServletPath());
    }
}