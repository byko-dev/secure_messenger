package pl.bykodev.messenger_api.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;
import pl.bykodev.messenger_api.Events.AiBotReplyEvent;
import pl.bykodev.messenger_api.database.ConversationEntity;
import pl.bykodev.messenger_api.database.MessageEntity;
import pl.bykodev.messenger_api.database.UserEntity;
import pl.bykodev.messenger_api.exceptions.ResourceNotFoundException;
import pl.bykodev.messenger_api.pojos.Message;
import pl.bykodev.messenger_api.pojos.SendMessageRequestDTO;
import pl.bykodev.messenger_api.services.*;
import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*") /* all origins are allowed, only developed purpose */
@RequestMapping("/api/conversations")
public class MessageController {

    private final ConversationService conversationService;
    private final MessageService messageService;
    private final UserService userService;
    private final MessagingService messagingService;
    private final ApplicationEventPublisher publisher;

    @GetMapping("/{conversationId}/messages")
    public List<Message> getMessages(@PathVariable String conversationId,
                                     @RequestHeader("Authorization") String authHeader,
                                     @RequestParam("from") int from)
    {
        UserEntity user = userService.getUser(authHeader);
        ConversationEntity conversation = conversationService.getConversationById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));

        return messageService.getMessages(conversation, from, user.getUsername());
    }

    @PostMapping("/{conversationId}/messages")
    public Message sendMessage(@PathVariable String conversationId, @Valid @ModelAttribute SendMessageRequestDTO model)
    {
        MessageEntity messageEntity = messagingService.send(conversationId, model);

        publisher.publishEvent(new AiBotReplyEvent(model.getMessage(), messageEntity.getId()));

        return messageService.convertMessageEntity(messageEntity, model.getAuthor());
    }
}