package pl.bykodev.messenger_api.integration_tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.bykodev.messenger_api.database.ConversationEntity;
import pl.bykodev.messenger_api.database.UserEntity;
import pl.bykodev.messenger_api.integration_tests.pojos.StatusDeserialization;
import pl.bykodev.messenger_api.pojos.Friend;
import pl.bykodev.messenger_api.pojos.Message;
import pl.bykodev.messenger_api.pojos.RegisterRequest;
import pl.bykodev.messenger_api.security.JwtUtils;
import pl.bykodev.messenger_api.services.ConversationService;
import pl.bykodev.messenger_api.services.MessageService;
import pl.bykodev.messenger_api.services.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class MessageControllerTest extends PostgresTestContainer {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private ConversationService conversationService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private ObjectMapper objectMapper;

    //given values
    private static UserEntity user1 = null;
    private static UserEntity user2 = null;
    private static String jwt = "Bearer ";

    private static Friend friendData = null;
    private static Optional<ConversationEntity> conversationEntity = null;
    private static String wrong_jwt =
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMTIzIiwiZXhwIjoxNjc3MTA2NDE5LCJpYyjiOjE2NzcwMjAwMTl9.i4Uo8sP01-gDCe9QwEVVRrXj_gJ0iXykohe6l7WYMos";
    private static String wrong_id = "e64520de-957e-4a92-9713-fa37c0e5c3f7";
    @Test
    @DisplayName("should return all messages")
    public void shouldReturnGetMessages() throws Exception{
        //given
        if(jwt.equals("Bearer ")){
            user1 = userService.createUser(new RegisterRequest("user18", "password", ""));
            user2 = userService.createUser(new RegisterRequest("user19", "password", ""));

            jwt += jwtUtils.generateToken("user18");

            friendData = conversationService.createRelation(user1, user2);

            conversationEntity = conversationService.getConversationById(friendData.getConversationId());
            messageService.saveMessageEntity("messageForOwner", "messageForFriend", null, conversationEntity.get(), "user18");
        }


        //when
        ResultActions resultActions = mockMvc.perform(get("/messages/" + conversationEntity.get().getId())
                .header("Authorization", jwt).param("from", "0"));

        //then
        resultActions.andExpect(status().is2xxSuccessful());

        TypeReference<List<Message>> typeReference = new TypeReference<>() {};
        List<Message> deserializedList = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), typeReference);

        assertEquals("user18", deserializedList.get(0).getAuthor());
    }

    @Test
    @DisplayName("should return empty list when i change from param in get messages request")
    public void shouldReturnEmptyListInGetMessages() throws Exception{
        //given
        if(jwt.equals("Bearer "))
            shouldReturnGetMessages();

        //when
        ResultActions resultActions = mockMvc.perform(get("/messages/" + conversationEntity.get().getId())
                .header("Authorization", jwt).param("from", "1"));

        //then
        resultActions.andExpect(status().is2xxSuccessful());

        TypeReference<List<Message>> typeReference = new TypeReference<>() {};
        List<Message> deserializedList = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), typeReference);

        assertEquals(0, deserializedList.size());
    }

    @Test
    @DisplayName("should trow unauthorized when i pass wrong jwt in get messages request")
    public void shouldReturnUnauthorizedInGetMessages() throws Exception{
        //given
        if(jwt.equals("Bearer "))
            shouldReturnGetMessages();

        //when
        ResultActions resultActions = mockMvc.perform(get("/messages/" + conversationEntity.get().getId())
                .header("Authorization", wrong_jwt).param("from", "0"));

        //then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("should throw not found when i pass nonexistent conversation id in get messages request")
    public void shouldReturnNotFoundInGetMessages_wrongConversationID() throws Exception{
        //given
        if(jwt.equals("Bearer "))
            shouldReturnGetMessages();

        //when
        ResultActions resultActions = mockMvc.perform(get("/messages/" + wrong_id)
                .header("Authorization", jwt).param("from", "0"));
        //then
        resultActions.andExpect(status().isNotFound());
        assertEquals("Conversation not found",
                objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(),
                        StatusDeserialization.class).getStatus());
    }

    @Test
    @DisplayName("should send message successful")
    public void shouldSendMessageSuccessful() throws Exception{
        //given
        if(jwt.equals("Bearer "))
            shouldReturnGetMessages();

        //when
        ResultActions resultActions = mockMvc.perform(post("/messages/" + conversationEntity.get().getId())
                .header("Authorization", jwt)
                .param("messageForOwnerStr", "messageForOwnerStr")
                .param("messageForFriendStr", "messageForFriendStr")
                .param("author", "user18"));

        //then
        resultActions.andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("should throw not found when i pass nonexistent conversation id in send message request")
    public void shouldReturnNotFoundInSendMessageRequest_wrongConversationID() throws Exception{
        //given
        if(jwt.equals("Bearer "))
            shouldReturnGetMessages();

        //when
        ResultActions resultActions = mockMvc.perform(post("/messages/" + wrong_id)
                .header("Authorization", jwt)
                .param("messageForOwnerStr", "messageForOwnerStr")
                .param("messageForFriendStr", "messageForFriendStr")
                .param("author", "user18"));

        //then
        resultActions.andExpect(status().isNotFound());
        assertEquals("Conversation not exist",
                objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(),
                        StatusDeserialization.class).getStatus());
    }

    @Test
    @DisplayName("should throw unauthorized when i pass wrong jwt in send message request")
    public void shouldReturnUnauthorizedInSendMessageRequest() throws Exception{
        //given
        if(jwt.equals("Bearer "))
            shouldReturnGetMessages();

        //when
        ResultActions resultActions = mockMvc.perform(post("/messages/" + conversationEntity.get().getId())
                .header("Authorization", wrong_jwt)
                .param("messageForOwnerStr", "messageForOwnerStr")
                .param("messageForFriendStr", "messageForFriendStr")
                .param("author", "user18"));

        //then
        resultActions.andExpect(status().isUnauthorized());
    }
}
