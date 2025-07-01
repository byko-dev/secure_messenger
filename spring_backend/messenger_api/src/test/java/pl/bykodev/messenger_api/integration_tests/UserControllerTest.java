package pl.bykodev.messenger_api.integration_tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.bykodev.messenger_api.database.UserRoleEnum;
import pl.bykodev.messenger_api.database.repository.UserEntityRepository;
import pl.bykodev.messenger_api.integration_tests.pojos.StatusDeserialization;
import pl.bykodev.messenger_api.pojos.*;
import pl.bykodev.messenger_api.security.JwtUtils;
import pl.bykodev.messenger_api.services.UserService;
import java.util.List;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class UserControllerTest extends PostgresTestContainer {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserEntityRepository userRepository;

    private String jwt = "Bearer ";
    private static String user_id = "";
    private static final String wrong_jwt =
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMTIzIiwiZXhwIjoxNjc3MTA2NDE5LCJpYyjiOjE2NzcwMjAwMTl9.i4Uo8sP01-gDCe9QwEVVRrXj_gJ0iXykohe6l7WYMos";
    private static boolean isAddFriendRequestExecuted = false;

    @Test
    @DisplayName("should return user date in request")
    public void shouldReturnUserDataGet() throws Exception{
        //given
        if(!userRepository.findByUsername("user11").isPresent())
            userService.createUser(new RegisterRequest("user11", "password", "", UserRoleEnum.USER));
        if(jwt.equals("Bearer "))
            jwt += jwtUtils.generateToken("user11");

        //when
        ResultActions resultActions = mockMvc.perform(get("/user").header("Authorization", jwt));

        //then
        UserData userData = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), UserData.class);
        user_id = userData.getId();

        resultActions.andExpect(status().is2xxSuccessful());
        assertEquals("user11", userData.getUsername());
    }

    @Test
    @DisplayName("should throw unauthorized when i pass wrong jwt token in get user data")
    public void shouldThrowUnauthorizedInUserDataGet() throws Exception{
        //when
        ResultActions resultActions = mockMvc.perform(get("/user").header("Authorization", wrong_jwt));

        //then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("should return user keys")
    public void shouldReturnUserKeys() throws Exception{
        //given
        if(jwt.equals("Bearer "))
            shouldReturnUserDataGet();

        //when
        ResultActions resultActions = mockMvc.perform(post("/user/keys")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(new Password("password")))));

        //then
        resultActions.andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("should throw unauthorized when I type wrong password in user keys request")
    public void shouldReturnUnauthorized_wrongPasswordInUserKeysRequest() throws Exception{
        //given
        if(jwt.equals("Bearer "))
            shouldReturnUserDataGet();

        //when
        ResultActions resultActions = mockMvc.perform(post("/user/keys")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(new Password("wrong_password")))));

        //then
        resultActions.andExpect(status().isUnauthorized());
        assertEquals("Unauthorized operation",
                objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(),
                        StatusDeserialization.class).getStatus());
    }

    @Test
    @Before
    @DisplayName("should add friend request")
    public void shouldAddFriend() throws Exception{
        //given
        if(user_id.isBlank())
            shouldReturnUserDataGet();

        userService.createUser(new RegisterRequest("user13", "password", "", UserRoleEnum.USER));
        String user_jwt = "Bearer " + jwtUtils.generateToken("user13");

        //when
        ResultActions resultActions = mockMvc.perform(post("/user/add/friend")
                .header("Authorization", user_jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(new UserID(user_id)))));

        //then
        isAddFriendRequestExecuted = true;
        resultActions.andExpect(status().is2xxSuccessful());
        assertEquals("user11",
                objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(),
                        Friend.class).getUsername());
    }

    @Test
    @DisplayName("should throw not found when i pass wrong id in add friend request")
    public void shouldReturnNotFoundInAddFriendRequest_wrongId() throws Exception{
        //given
        String wrong_id = "e64520de-957e-4a92-9713-fa37c0e5c3f7";

        userService.createUser(new RegisterRequest("user17", "password", "", UserRoleEnum.USER));
        String user_jwt = "Bearer " + jwtUtils.generateToken("user17");

        //when
        ResultActions resultActions = mockMvc.perform(post("/user/add/friend")
                .header("Authorization", user_jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(new UserID(wrong_id)))));

        //then
        resultActions.andExpect(status().isNotFound());
        assertEquals("Friend was not found",
                objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(),
                        StatusDeserialization.class).getStatus());
    }

    @Test
    @DisplayName("should throw bad request when i pass wrong id length in add friend request")
    public void shouldReturnBadRequestInAddFriendRequest_idDontPassValidation() throws Exception{
        //given
        //test 1
        if(jwt.equals("Bearer "))
            shouldReturnUserDataGet();
        String too_long_id = "e64520de-957e-4a92-9713-fa37c0e5c3f7-1337";

        //when
        ResultActions resultActions = mockMvc.perform(post("/user/add/friend")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(new UserID(too_long_id)))));

        //then
        resultActions.andExpect(status().isBadRequest());
        assertEquals("ID miss UUID requirements",
                objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(),
                        StatusDeserialization.class).getStatus());

        //test 2
        //given
        String too_short_id = "e64520de-957e-4a92-9713-fa37c0e5c3f";

        //when
        ResultActions resultActions1 = mockMvc.perform(post("/user/add/friend")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(new UserID(too_short_id)))));

        //then
        resultActions.andExpect(status().isBadRequest());
        assertEquals("ID miss UUID requirements",
                objectMapper.readValue(resultActions1.andReturn().getResponse().getContentAsString(),
                        StatusDeserialization.class).getStatus());
    }

    @Test
    @DisplayName("should throw unauthorized when i pass wrong jwt in add friend request")
    public void shouldReturnUnauthorizedInAddFriendRequest() throws Exception{
        //given
        if(user_id.isBlank())
            shouldReturnUserDataGet();

        //when
        ResultActions resultActions = mockMvc.perform(post("/user/add/friend")
                .header("Authorization", wrong_jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(new UserID(user_id)))));

        //then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("should return friends list")
    public void shouldReturnFriendsList() throws Exception{
        //given
        if(!isAddFriendRequestExecuted)
            shouldAddFriend();

        if(jwt.equals("Bearer "))
            shouldReturnUserDataGet();

        //when
        ResultActions resultActions = mockMvc.perform(get("/user/friends").header("Authorization", jwt));

        //then
        resultActions.andExpect(status().is2xxSuccessful());

        TypeReference<List<Friend>> typeReference = new TypeReference<>() {};
        List<Friend> deserializedList = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), typeReference);
        assertEquals(1, deserializedList.size());
    }

    @Test
    @DisplayName("should throw unauthorized when i pass wrong jwt in friends list request")
    public void shouldReturnUnauthorizedInFriendsListRequest() throws Exception {
        //when
        ResultActions resultActions = mockMvc.perform(get("/user/friends")
                .header("Authorization", wrong_jwt));
        //then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("should update user data")
    public void shouldPatchUserData() throws Exception{
        //given
        if(jwt.equals("Bearer "))
            shouldReturnUserDataGet();
        String customUsername = "customUsername";

        //when
        ResultActions resultActions = mockMvc.perform(patch("/user/data")
                .header("Authorization", jwt)
                .param("description", "simple description of user")
                .param(customUsername, customUsername));

        System.out.println(resultActions.andReturn().getResponse().getContentAsString());
        //then
        resultActions.andExpect(status().is2xxSuccessful());
        assertEquals("User data was updated!",
                objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(),
                        StatusDeserialization.class).getStatus());
        assertEquals("customUsername", userRepository.findById(user_id).get().getCustomUsername());
    }

    @Test
    @DisplayName("should throw unauthorized when i pass wrong jwt in update user data request")
    public void shouldReturnUnauthorizedInPatchUserData() throws Exception{
        //when
        ResultActions resultActions = mockMvc.perform(patch("/user/data")
                .header("Authorized", wrong_jwt)
                .param("description", "simple description of user"));
        //then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("should throw bad request when i pass wrong contentType of file in update user data request")
    public void shouldReturnBadRequestInPatchUserData_invalidFileContentType() throws Exception{
        //given
        if(jwt.equals("Bearer "))
            shouldReturnUserDataGet();

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        //when
        ResultActions resultActions = mockMvc.perform(multipart("/user/data").file(file).with(request -> {
            request.setMethod("PATCH");
            return request;
        }).header("Authorization", jwt));

        //then
        resultActions.andExpect(status().isBadRequest());
        assertEquals("User photo requires content type image/jpeg or image/png",
                objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(),
                        StatusDeserialization.class).getStatus());
    }

    @Test
    @DisplayName("should throw bad request when params dont pass validation in update user data request")
    public void shouldReturnBadRequestInPatchUserData_paramsDontPassValidation() throws Exception{
        //test 1
        //given
        if(jwt.equals("Bearer "))
            shouldReturnUserDataGet();
        String too_long_description ="aatn#7ju246a2d4d5mao9m73t23128s7t6d4sf6vjz23j5583qey76pk7x!gt616 " +
                "aatn#7ju246a2d4d5mao9m73t23128s7t6d4sf6vjz23j5583qey76pk7x!gt616 " +
                "aatn#7ju246a2d4d5mao9m73t23128s7t6d4sf6vjz23j5583qey76pk7x!gt616 " +
                "aatn#7ju246a2d4d5mao9m73t23128s7t6d4sf6vjz23j5583qey76pk7x!gt616";

        //when
        ResultActions resultActions = mockMvc.perform(patch("/user/data")
                .header("Authorization", jwt)
                .param("description", too_long_description));

        //then
        resultActions.andExpect(status().isBadRequest());
        assertEquals("updateUserData.description: Description miss requirements of 256 characters maximum",
                objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(),
                        StatusDeserialization.class).getStatus());

        //test 2
        //given
        String too_short_customUsername = "use";

        //when
        ResultActions resultActions1 = mockMvc.perform(patch("/user/data")
                .header("Authorization", jwt)
                .param("customUsername", too_short_customUsername));

        //then
        resultActions1.andExpect(status().isBadRequest());
        assertEquals("updateUserData.customUsername: Custom username miss requirements of 4-32 characters",
                objectMapper.readValue(resultActions1.andReturn().getResponse().getContentAsString(),
                        StatusDeserialization.class).getStatus());

        //test 3
        //given
        String too_long_customUsername = "aatn#7ju246a2d4d5mao9m73t23128s7t6d4sf6vjz23j5583qey76pk7x!gt616";

        //when
        ResultActions resultActions2 = mockMvc.perform(patch("/user/data")
                .header("Authorization", jwt)
                .param("customUsername", too_long_customUsername));

        //then
        resultActions2.andExpect(status().isBadRequest());
        assertEquals("updateUserData.customUsername: Custom username miss requirements of 4-32 characters",
                objectMapper.readValue(resultActions2.andReturn().getResponse().getContentAsString(),
                        StatusDeserialization.class).getStatus());
    }
}
