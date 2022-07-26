package hexlet.code.app.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.app.configuration.SecurityConfiguration;
import hexlet.code.app.configuration.SpringConfigTests;
import hexlet.code.app.model.AuthRequest;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static hexlet.code.app.configuration.SpringConfigTests.TEST_PROFILE;
import static hexlet.code.app.controller.UserController.USER_ID_IN_CONTROLLER;
import static hexlet.code.app.controller.UserController.USER_CONTROLLER_PATH;
import static hexlet.code.app.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = {SpringConfigTests.class, SecurityConfiguration.class})
public class UserControllerTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestUtils utils;

    @AfterEach
    public void clear() {
        utils.tearDown();
    }

    @Test
    public void registrationTest() throws Exception {
        assertEquals(0, userRepository.count());
        utils.regDefaultUser().andExpect(status().isCreated());
        assertEquals(1, userRepository.count());
    }

    @Test
    public void getUserByIdTest() throws Exception {
        utils.regDefaultUser();
        final User expectedUser = userRepository.findAll().get(0);
        final var response = utils.perform(
                        get(USER_CONTROLLER_PATH + USER_ID_IN_CONTROLLER, expectedUser.getId()),
                        expectedUser
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final User user = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expectedUser.getId(), user.getId());
        assertEquals(expectedUser.getEmail(), user.getEmail());
        assertEquals(expectedUser.getFirstName(), user.getFirstName());
        assertEquals(expectedUser.getLastName(), user.getLastName());
    }

    @Test
    public void getUserByIdFailsTest() throws Exception {
        utils.regDefaultUser();
        utils.regUser(new User("new name", "new last name", TEST_USERNAME_2, "new pwd"));

        final User expectedUser = userRepository.findAll().get(0);
        final User anotherUser = userRepository.findAll().get(1);

        utils.perform(get(USER_CONTROLLER_PATH + USER_ID_IN_CONTROLLER, expectedUser.getId()), anotherUser)
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getAllUsersTest() throws Exception {
        utils.regDefaultUser();
        final var response = utils.perform(get(USER_CONTROLLER_PATH))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<User> users = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(users).hasSize(1);
    }

    @Test
    public void twiceRegTheSameUserFailTest() throws Exception {
        utils.regDefaultUser().andExpect(status().isCreated());
        utils.regDefaultUser().andExpect(status().isBadRequest());

        assertEquals(1, userRepository.count());
    }

    @Test
    public void loginTest() throws Exception {
        utils.regDefaultUser();
        final AuthRequest loginDto = new AuthRequest(
                utils.getTestRegistrationUser().getEmail(),
                utils.getTestRegistrationUser().getPassword()
        );

        final var loginRequest = post("/login").content(asJson(loginDto))
                .contentType(APPLICATION_JSON);
        utils.perform(loginRequest).andExpect(status().isOk());
    }


    @Test
    public void loginFailTest() throws Exception {
        final AuthRequest loginDto = new AuthRequest(
                utils.getTestRegistrationUser().getEmail(),
                utils.getTestRegistrationUser().getPassword()
        );
        final var loginRequest = post("/login").content(asJson(loginDto))
                .contentType(APPLICATION_JSON);
        utils.perform(loginRequest).andExpect(status().isUnauthorized());
    }

    @Test
    public void updateUserTest() throws Exception {
        utils.regDefaultUser();

        final var existsUser = userRepository.findByEmail(TEST_USERNAME).get();
        final Long userId = existsUser.getId();

        final var user = new User("new name", "new last name", TEST_USERNAME_2, "new pwd");

        final var updateRequest = put(USER_CONTROLLER_PATH + USER_ID_IN_CONTROLLER, userId)
                .content(asJson(user))
                .contentType(APPLICATION_JSON);

        utils.perform(updateRequest, existsUser).andExpect(status().isOk());

        assertTrue(userRepository.existsById(userId));
        assertNull(userRepository.findByEmail(TEST_USERNAME).orElse(null));
        assertNotNull(userRepository.findByEmail(TEST_USERNAME_2).orElse(null));
    }

    @Test
    public void deleteUserTest() throws Exception {
        utils.regDefaultUser();

        final var existsUser = userRepository.findByEmail(TEST_USERNAME).get();
        final Long userId = existsUser.getId();

        utils.perform(delete(USER_CONTROLLER_PATH + USER_ID_IN_CONTROLLER, userId), existsUser)
                .andExpect(status().isOk());

        assertEquals(0, userRepository.count());
    }

    @Test
    public void deleteUserFailsTest() throws Exception {
        utils.regDefaultUser();

        var user = new User(
                "fname",
                "lname",
                TEST_USERNAME_2,
                "pwd"
        );

        utils.regUser(user);

        final Long userId = userRepository.findByEmail(TEST_USERNAME).get().getId();

        utils.perform(delete(USER_CONTROLLER_PATH + USER_ID_IN_CONTROLLER, userId), user)
                .andExpect(status().isUnauthorized());

        assertEquals(2, userRepository.count());
    }

}
