package pl.bykodev.messenger_api.integration_tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.bykodev.messenger_api.database.FileEntity;
import pl.bykodev.messenger_api.database.UserEntity;
import pl.bykodev.messenger_api.database.UserRoleEnum;
import pl.bykodev.messenger_api.database.repository.UserEntityRepository;
import pl.bykodev.messenger_api.integration_tests.pojos.StatusDeserialization;
import pl.bykodev.messenger_api.pojos.JwtToken;
import pl.bykodev.messenger_api.pojos.LoginRequest;
import pl.bykodev.messenger_api.pojos.RegisterRequest;
import pl.bykodev.messenger_api.pojos.UserData;
import pl.bykodev.messenger_api.services.FileService;
import pl.bykodev.messenger_api.services.UserService;
import java.util.List;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class WebControllerTest extends PostgresTestContainer {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;
    @Autowired
    private UserEntityRepository userRepository;
    private static final String OVERFLOW_MAX_OF_SECURE_RANDOM = "dwg6y664k5b4762pad55z57to3w9al#p7y139885snj4_1l1f3rhy5xadh628db21";

    private static String jwt = "Bearer ";

    @Test
    @DisplayName("should register successful")
    public void shouldRegisterSuccessful() throws Exception{
        //given
        RegisterRequest registerPojo = new RegisterRequest();
        registerPojo.setUsername("testuser");
        registerPojo.setPassword("testpassword");
        registerPojo.setSecureRandom("secure_random_string");
        registerPojo.setRoleEnum(UserRoleEnum.USER);

        //when
        ResultActions resultActions = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(registerPojo))));

        //then
        resultActions.andExpect(status().is2xxSuccessful());
        assertEquals("OK", objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(),
                StatusDeserialization.class).getStatus());
    }

    @Test
    @DisplayName("should throw bad request when user already existed in register request")
    public void shouldReturnBadRequestInRegister_userExistsAlready() throws Exception {
        //given
        RegisterRequest registerPojo = new RegisterRequest();
        registerPojo.setUsername("user123");
        registerPojo.setPassword("user123");
        registerPojo.setSecureRandom("");
        registerPojo.setRoleEnum(UserRoleEnum.USER);

        userService.createUser(registerPojo);

        //when
        ResultActions resultActions = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(registerPojo))));
        //then
        resultActions.andExpect(status().isBadRequest());
        assertEquals("User already exists!",
                objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(),
                        StatusDeserialization.class).getStatus());
    }

    @Test
    @DisplayName("should throw bad request when i pass null params in register request")
    public void shouldReturnBadRequest_nullInParams() throws Exception {
        //test 1
        //given
        RegisterRequest registerPojo = new RegisterRequest();
        registerPojo.setUsername("user1234");
        registerPojo.setPassword("password");
        registerPojo.setSecureRandom(null);
        //when
        ResultActions resultActions = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(registerPojo))));
        //then
        resultActions.andExpect(status().isBadRequest());
        assertEquals("must not be null",
                objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(),
                        StatusDeserialization.class).getStatus());

        //test 2
        //given
        registerPojo.setUsername(null);
        registerPojo.setSecureRandom("");
        //when
        ResultActions resultActions1 = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(registerPojo))));
        //then
        resultActions1.andExpect(status().isBadRequest());
        assertEquals("must not be null",
                objectMapper.readValue(resultActions1.andReturn().getResponse().getContentAsString(),
                        StatusDeserialization.class).getStatus());

        //test 3
        //given
        registerPojo.setUsername("user1234");
        registerPojo.setPassword(null);

        //when
        ResultActions resultActions2 = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(registerPojo))));
        //then
        resultActions2.andExpect(status().isBadRequest());
        assertEquals("must not be null",
                objectMapper.readValue(resultActions2.andReturn().getResponse().getContentAsString(),
                        StatusDeserialization.class).getStatus());
    }

    @Test
    @DisplayName("should throw bad request when i params dont pass validation in register request")
    public void shouldReturnBadRequestInRegister_InvalidRegexParams() throws Exception {
        //test 1
        //given
        RegisterRequest registerPojo = new RegisterRequest();
        registerPojo.setUsername("user1234");
        registerPojo.setPassword("");
        registerPojo.setSecureRandom("");
        registerPojo.setRoleEnum(UserRoleEnum.USER);

        //when
        ResultActions resultActions = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(registerPojo))));
        //then
        resultActions.andExpect(status().isBadRequest());
        assertEquals("Password miss requirements of 6-32 characters",
                objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(),
                        StatusDeserialization.class).getStatus());

        //test 2
        //given
        registerPojo.setUsername("user");
        registerPojo.setPassword("password");

        //when
        ResultActions resultActionsTest1 = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(registerPojo))));
        //then
        resultActionsTest1.andExpect(status().isBadRequest());
        assertEquals("Username miss requirements of 6-32 characters",
                objectMapper.readValue(resultActionsTest1.andReturn().getResponse().getContentAsString(),
                        StatusDeserialization.class).getStatus());

        //test 3
        //given
        registerPojo.setUsername("user1234");
        registerPojo.setSecureRandom(OVERFLOW_MAX_OF_SECURE_RANDOM);
        //when
        ResultActions resultActionsTest2 = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(registerPojo))));
        //then
        resultActionsTest2.andExpect(status().isBadRequest());
        assertEquals("size must be between 0 and 64",
                objectMapper.readValue(resultActionsTest2.andReturn().getResponse().getContentAsString(),
                        StatusDeserialization.class).getStatus());
    }

    @Test
    @DisplayName("should login successful")
    public void shouldLoginSuccessful() throws Exception{
        //given
        LoginRequest loginPojo = new LoginRequest();
        loginPojo.setUsername("user2023");
        loginPojo.setPassword("password2023");
        if(jwt.equals("Bearer "))
            userService.createUser(new RegisterRequest(loginPojo.getUsername(), loginPojo.getPassword(), "", UserRoleEnum.USER));

        //when
        ResultActions resultActions = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(loginPojo))));

        //then
        resultActions.andExpect(status().is2xxSuccessful());
        if(jwt.equals("Bearer "))
            jwt += objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), JwtToken.class).getJwt();
    }

    @Test
    @DisplayName("when throw unauthorized when i pass invalid data in login request")
    public void shouldReturnUnauthorizedInLogin_invalidUserPass() throws Exception{
        //given
        LoginRequest loginPojo = new LoginRequest();
        loginPojo.setUsername("user200");
        loginPojo.setPassword("password200");

        //when
        ResultActions resultActions = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(loginPojo))));

        //then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("should return bad request when i data dont pass validation in login request")
    public void shouldReturnBadRequestInLogin_valuesDontPassRegex() throws Exception{
        //test 1
        //given
        LoginRequest loginPojo = new LoginRequest();
        loginPojo.setUsername("user");
        loginPojo.setPassword("password");

        //when
        ResultActions resultActions = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(loginPojo))));
        //then
        resultActions.andExpect(status().isBadRequest());
        assertEquals("Username miss requirements of 6-32 characters",
                objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(),
                        StatusDeserialization.class).getStatus());

        //test 2
        //given
        loginPojo.setUsername("user123");
        loginPojo.setPassword(OVERFLOW_MAX_OF_SECURE_RANDOM);

        //when
        ResultActions resultActions1 = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(loginPojo))));

        //then
        resultActions1.andExpect(status().isBadRequest());
        assertEquals("Password miss requirements of 6-32 characters",
                objectMapper.readValue(resultActions1.andReturn().getResponse().getContentAsString(),
                        StatusDeserialization.class).getStatus());
    }

    @Test
    @DisplayName("should find user in search request")
    public void shouldFindUserInSearchRequest() throws Exception{
        //given
        if(jwt.equals("Bearer "))
            shouldLoginSuccessful();
        userService.createUser(new RegisterRequest("user1234", "password", "", UserRoleEnum.USER));
        userService.createUser(new RegisterRequest("searchedUser", "password", "", UserRoleEnum.USER));
        List<UserEntity> usersEntityList = userRepository.findAll();

        //then
        ResultActions resultActions = mockMvc.perform(get("/users")
                .header("Authorization", jwt)
                .param("userid", usersEntityList.get(0).getId())
                .param("search", usersEntityList.get(1).getUsername().substring(0, 5)));

        //then
        String content = resultActions.andReturn().getResponse().getContentAsString();
        TypeReference<List<UserData>> typeReference = new TypeReference<>() {};
        List<UserData> deserializedList = objectMapper.readValue(content, typeReference);

        resultActions.andExpect(status().is2xxSuccessful());
        assertEquals(usersEntityList.get(1).getUsername(), deserializedList.get(0).getUsername());
    }

    @Test
    @DisplayName("should dont find user when i type owner username in search request")
    public void shouldDontFindUserInSearchRequest() throws Exception{
        //given
        if(jwt.equals("Bearer "))
            shouldLoginSuccessful();

        userService.createUser(new RegisterRequest("user22", "password", "", UserRoleEnum.USER));
        List<UserEntity> usersEntityList = userRepository.findAll();

        //then
        ResultActions resultActions = mockMvc.perform(get("/users")
                .header("Authorization", jwt)
                .param("userid", usersEntityList.get(0).getId()).param("search", usersEntityList.get(0).getUsername()));

        //then
        String content = resultActions.andReturn().getResponse().getContentAsString();
        TypeReference<List<UserData>> typeReference = new TypeReference<>() {};
        List<UserData> deserializedList = objectMapper.readValue(content, typeReference);

        resultActions.andExpect(status().is2xxSuccessful());
        assertTrue(deserializedList.isEmpty());
    }

    @Test
    @DisplayName("should throw bad request when I type the wrong id in search request")
    public void shouldThrowBadRequestInSearchRequest_wrongId() throws Exception{
        //test1
        //given
        if(jwt.equals("Bearer "))
            shouldLoginSuccessful();
        String longer_id = "12cfb01b-75c6-41b6-979a-1bd26fba03591232";

        //when
        ResultActions resultActions = mockMvc.perform(get("/users")
                .header("Authorization", jwt)
                .param("userid", longer_id).param("search", "user"));

        //then
        resultActions.andExpect(status().isBadRequest());
        assertEquals("getAllUsers.userid: ID miss UUID requirements",
                objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(),
                        StatusDeserialization.class).getStatus());

        //test2
        //given
        String too_short_id = "12cfb01b-75c6-41b6-979a-1bd26fba035";

        //when
        ResultActions resultActions1 = mockMvc.perform(get("/users")
                .header("Authorization", jwt)
                .param("userid", too_short_id).param("search", "user"));

        //then
        resultActions1.andExpect(status().isBadRequest());
        assertEquals("getAllUsers.userid: ID miss UUID requirements",
                objectMapper.readValue(resultActions1.andReturn().getResponse().getContentAsString(),
                        StatusDeserialization.class).getStatus());
    }

    @Test
    @DisplayName("should throw bad request when I type the wrong search param length characters in the search request")
    public void shouldThrowBadRequestInSearchRequest_WrongLengthSearchParam() throws Exception{
        //test 1
        //given
        if(jwt.equals("Bearer "))
            shouldLoginSuccessful();
        String id = "12cfb01b-75c6-41b6-979a-1bd26fba0359";
        String too_long_search_param = "qhg131an97v4y78w@56oc9lfw336lw7b4";

        //when
        ResultActions resultActions = mockMvc.perform(get("/users")
                .header("Authorization", jwt)
                .param("userid", id).param("search", too_long_search_param));

        //then
        resultActions.andExpect(status().isBadRequest());
        assertEquals("getAllUsers.search: search param requires 4-32 characters",
                objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(),
                        StatusDeserialization.class).getStatus());

        //test 2
        //given
        String too_short_search_param = "use";
        ResultActions resultActions1 = mockMvc.perform(get("/users")
                .header("Authorization", jwt)
                .param("userid", id).param("search", too_short_search_param));

        //then
        resultActions1.andExpect(status().isBadRequest());
        assertEquals("getAllUsers.search: search param requires 4-32 characters",
                objectMapper.readValue(resultActions1.andReturn().getResponse().getContentAsString(),
                        StatusDeserialization.class).getStatus());
    }

    @Test
    @DisplayName("should find and return file")
    public void shouldFindFileAndReturnFileRequest() throws Exception{
        //given
        if(jwt.equals("Bearer "))
            shouldLoginSuccessful();

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        FileEntity fileEntity = fileService.save(file);

        //when
        ResultActions resultActions = mockMvc.perform(get("/file/" + fileEntity.getId())
                .header("Authorization", jwt));

        //then
        resultActions.andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("should throw not found when I type nonexistent id in file request")
    public void shouldThrowNotFoundInFileRequest_nonexistentId() throws Exception{
        //given
        if(jwt.equals("Bearer "))
            shouldLoginSuccessful();
        String id = "12cfb01b-75c6-41b6-979a-1bd26fba0359";

        //when
        ResultActions resultActions = mockMvc.perform(get("/file/"+ id)
                .header("Authorization", jwt));

        //then
        resultActions.andExpect(status().isNotFound());
        assertEquals("File with id: " + id + " was not found!",
                objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(),
                        StatusDeserialization.class).getStatus());
    }

    @Test
    @DisplayName("should throw bad request when i type wrong id length in file request")
    public void shouldThrowBadRequestInFileRequest_idDontPassValidation() throws Exception{
        //test 1
        //given
        if(jwt.equals("Bearer "))
            shouldLoginSuccessful();
        String too_long_id = "12cfb01b-75c6-41b6-979a-1bd26fba0359-1337";

        //when
        ResultActions resultActions = mockMvc.perform(get("/file/" + too_long_id)
                .header("Authorization", jwt));

        //then
        resultActions.andExpect(status().isBadRequest());
        assertEquals("getPhoto.id: ID miss UUID requirements",
                objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(),
                        StatusDeserialization.class).getStatus());

        //test 2
        //given
        String too_short_id = "12cfb01b-75c6-41b6-979a-1bd26fba035";

        //when
        ResultActions resultActions1 = mockMvc.perform(get("/file/" + too_short_id)
                .header("Authorization", jwt));

        //then
        resultActions1.andExpect(status().isBadRequest());
        assertEquals("getPhoto.id: ID miss UUID requirements",
                objectMapper.readValue(resultActions1.andReturn().getResponse().getContentAsString(),
                        StatusDeserialization.class).getStatus());
    }
}
