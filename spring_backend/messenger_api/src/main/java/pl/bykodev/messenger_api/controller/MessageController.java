package pl.bykodev.messenger_api.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.bykodev.messenger_api.database.ConversationEntity;
import pl.bykodev.messenger_api.database.MessageEntity;
import pl.bykodev.messenger_api.database.UserEntity;
import pl.bykodev.messenger_api.exceptions.ResourceNotFoundException;
import pl.bykodev.messenger_api.pojos.AlertDTO;
import pl.bykodev.messenger_api.pojos.Message;
import pl.bykodev.messenger_api.services.ConversationService;
import pl.bykodev.messenger_api.services.MessageService;
import pl.bykodev.messenger_api.services.UserService;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*") /* all origins are allowed, only developed purpose */
@RequestMapping("/messages")
public class MessageController {

    private final ConversationService conversationService;
    private final MessageService messageService;
    private final SimpMessagingTemplate template;
    private final UserService userService;

    public MessageController(ConversationService conversationService, MessageService messageService, SimpMessagingTemplate template, UserService userService)
    {
        this.conversationService = conversationService;
        this.messageService = messageService;
        this.template = template;
        this.userService = userService;
    }

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
    public ResponseEntity<Message> sendMessage(@PathVariable String conversationId,
                                               @Valid @Size(max = 1024, message = "message miss requirements of 1024 characters maximum")
                                               @RequestParam @Nullable String messageForOwnerStr,
                                               @Valid @Size(max = 1024, message = "message miss requirements of 1024 characters maximum")
                                               @RequestParam @Nullable String messageForFriendStr,
                                               @RequestParam("files") @Nullable MultipartFile[] files,
                                               @Valid @Size(min = 4, max = 32, message = "Username miss requirements of 4-32 characters")
                                               @RequestParam String author){

        Optional<ConversationEntity> conversationEntity = conversationService.getConversationById(conversationId);

        if (conversationEntity.isEmpty())
            throw new ResourceNotFoundException("Conversation not exist");

        MessageEntity messageEntity = messageService.saveMessageEntity(messageForOwnerStr, messageForFriendStr, files, conversationEntity.get(), author);

        UserEntity friendEntity = conversationEntity.get().getUser1().getUsername().equals(author) ?
                conversationEntity.get().getUser2() : conversationEntity.get().getUser1();

        template.convertAndSendToUser(friendEntity.getUsername(),
                "/conversation/" + conversationId,
                messageService.convertMessageEntity(messageEntity, friendEntity.getUsername()));

        template.convertAndSendToUser(friendEntity.getUsername(),
                "/alert",
                (new AlertDTO())
                        .setNewMessageAction()
                        .setData(conversationService.convertConversationEntityToFriend(conversationEntity.get(), friendEntity)));

        return ResponseEntity.ok(messageService.convertMessageEntity(messageEntity, author));
    }
}