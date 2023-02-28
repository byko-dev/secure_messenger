package pl.bykodev.messenger_api.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.bykodev.messenger_api.database.ConversationEntity;
import pl.bykodev.messenger_api.database.MessageEntity;
import pl.bykodev.messenger_api.database.UserEntity;
import pl.bykodev.messenger_api.exceptions.ResourceNotFoundException;
import pl.bykodev.messenger_api.pojos.Status;
import pl.bykodev.messenger_api.services.ConversationService;
import pl.bykodev.messenger_api.services.MessageService;
import pl.bykodev.messenger_api.services.UserService;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*") /* all origins are allowed, only developed purpose */
@AllArgsConstructor
@RequestMapping("/messages")
public class MessageController {

    private ConversationService conversationService;
    private MessageService messageService;
    private SimpMessagingTemplate template;
    private UserService userService;

    @GetMapping("/{conversationId}")
    public ResponseEntity<?> getMessages(@PathVariable String conversationId, @RequestHeader("Authorization") String authHeader,
                                         @RequestParam("from") int from) {
        UserEntity user = userService.getUser(authHeader);

        Optional<ConversationEntity> conversation = conversationService.getConversationById(conversationId);
        if (conversation.isEmpty())
            throw new ResourceNotFoundException("Conversation not found");

        return ResponseEntity.ok(messageService.getMessages(conversation.get(), from, user.getUsername()));
    }

    @PostMapping("/{conversationId}")
    public ResponseEntity<Status> sendMessage(@PathVariable String conversationId,
                                              @Valid @Size(max = 1024, message = "message miss requirements of 1024 characters maximum")
                                              @RequestParam @Nullable String messageForOwnerStr,
                                              @Valid @Size(max = 1024, message = "message miss requirements of 1024 characters maximum")
                                              @RequestParam @Nullable String messageForFriendStr,
                                              @RequestParam @Nullable MultipartFile file,
                                              @Valid @Size(min = 4, max = 32, message = "Username miss requirements of 4-32 characters")
                                              @RequestParam String author,
                                              HttpServletRequest request){

        Optional<ConversationEntity> conversationEntity = conversationService.getConversationById(conversationId);

        if (conversationEntity.isEmpty())
            throw new ResourceNotFoundException("Conversation not exist");

        MessageEntity messageEntity = messageService.saveMessageEntity(messageForOwnerStr, messageForFriendStr, file, conversationEntity.get(), author);

        template.convertAndSendToUser(author,
                "/conversation/" + conversationId,
                messageService.convertMessageEntity(messageEntity, author));

        String friendUsername = conversationEntity.get().getUser1().getUsername().equals(author) ?
                conversationEntity.get().getUser2().getUsername() : conversationEntity.get().getUser1().getUsername();

        template.convertAndSendToUser(friendUsername,
                "/conversation/" + conversationId,
                messageService.convertMessageEntity(messageEntity, friendUsername));

        return ResponseEntity.ok(new Status("OK", request.getServletPath()));
    }
}
