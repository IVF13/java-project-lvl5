package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.DTO.UserDTO;
import hexlet.code.configuration.SecurityConfiguration;
import hexlet.code.configuration.SpringConfigTests;
import hexlet.code.DTO.AuthRequestDTO;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static hexlet.code.configuration.SpringConfigTests.TEST_PROFILE;
import static hexlet.code.controller.UserController.USER_ID_IN_CONTROLLER;
import static hexlet.code.controller.UserController.USER_CONTROLLER_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

        final User user = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expectedUser.getId(), user.getId());
        assertEquals(expectedUser.getEmail(), user.getEmail());
        assertEquals(expectedUser.getFirstName(), user.getFirstName());
        assertEquals(expectedUser.getLastName(), user.getLastName());
    }

    @Test
    public void getUserByIdFailsTest() throws Exception {
        utils.regDefaultUser();

        final User expectedUser = userRepository.findAll().get(0);

        utils.perform(get(USER_CONTROLLER_PATH + USER_ID_IN_CONTROLLER, expectedUser.getId() + 100), expectedUser)
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAllUsersTest() throws Exception {
        utils.regDefaultUser();
        final var response = utils.perform(get(USER_CONTROLLER_PATH))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<User> users = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(users).hasSize(1);
    }

    @Test
    public void twiceRegTheSameUserFailTest() throws Exception {
        utils.regDefaultUser().andExpect(status().isCreated());
        utils.regDefaultUser().andExpect(status().isInternalServerError());

        assertEquals(1, userRepository.count());
    }

    @Test
    public void loginTest() throws Exception {
        utils.regDefaultUser();
        final AuthRequestDTO loginDto = new AuthRequestDTO(
                utils.getTestRegistrationUserDTO().getEmail(),
                utils.getTestRegistrationUserDTO().getPassword()
        );

        final var loginRequest = post("/login").content(TestUtils.asJson(loginDto))
                .contentType(APPLICATION_JSON);
        utils.perform(loginRequest).andExpect(status().isOk());
    }


    @Test
    public void loginFailTest() throws Exception {
        final AuthRequestDTO loginDto = new AuthRequestDTO(
                utils.getTestRegistrationUserDTO().getEmail(),
                utils.getTestRegistrationUserDTO().getPassword()
        );
        final var loginRequest = post("/login").content(TestUtils.asJson(loginDto))
                .contentType(APPLICATION_JSON);
        utils.perform(loginRequest).andExpect(status().isUnauthorized());
    }

    @Test
    public void updateUserTest() throws Exception {
        utils.regDefaultUser();

        final var existsUser = userRepository.findByEmail(TestUtils.TEST_USERNAME).get();
        final Long userId = existsUser.getId();

        final var userDTO = new UserDTO(TestUtils.TEST_USERNAME_2, "new name", "new last name", "new pwd");

        final var updateRequest = put(USER_CONTROLLER_PATH + USER_ID_IN_CONTROLLER, userId)
                .content(TestUtils.asJson(userDTO))
                .contentType(APPLICATION_JSON);

        utils.perform(updateRequest, existsUser).andExpect(status().isOk());

        assertTrue(userRepository.existsById(userId));
        assertNull(userRepository.findByEmail(TestUtils.TEST_USERNAME).orElse(null));
        assertNotNull(userRepository.findByEmail(TestUtils.TEST_USERNAME_2).orElse(null));
    }

    @Test
    public void deleteUserTest() throws Exception {
        utils.regDefaultUser();

        final var existsUser = userRepository.findByEmail(TestUtils.TEST_USERNAME).get();
        final Long userId = existsUser.getId();

        utils.perform(delete(USER_CONTROLLER_PATH + USER_ID_IN_CONTROLLER, userId), existsUser)
                .andExpect(status().isOk());

        assertEquals(0, userRepository.count());
    }

    @Test
    public void deleteUserFailsTest() throws Exception {
        utils.regDefaultUser();

        final var userDTO = new UserDTO(TestUtils.TEST_USERNAME_2, "fname", "lname", "pwd");

        utils.regUser(userDTO);

        final var user = userRepository.findByEmail(TestUtils.TEST_USERNAME_2).get();
        final Long userId = userRepository.findByEmail(TestUtils.TEST_USERNAME).get().getId();

        utils.perform(delete(USER_CONTROLLER_PATH + USER_ID_IN_CONTROLLER, userId), user)
                .andExpect(status().isInternalServerError());

        assertEquals(2, userRepository.count());
    }

}
