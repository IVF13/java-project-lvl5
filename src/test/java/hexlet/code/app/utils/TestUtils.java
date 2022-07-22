package hexlet.code.app.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;



import static hexlet.code.app.controller.UserController.USER_CONTROLLER_PATH;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Component
public class TestUtils {

    public static final String TEST_USERNAME = "email@email.com";
    public static final String TEST_USERNAME_2 = "email2@email.com";

    private final User testRegistrationUser = new User(
            "fname",
            "lname",
            TEST_USERNAME,
            "pwd"
    );

    public User getTestRegistrationUser() {
        return testRegistrationUser;
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private PostCommentRepository postCommentRepository;
//
//    @Autowired
//    private PostRepository postRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    public void tearDown() {
//        postCommentRepository.deleteAll();
//        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    public User getUserByEmail(final String email) {
        return userRepository.findByEmail(email).get();
    }

    public ResultActions regDefaultUser() throws Exception {
        return regUser(testRegistrationUser);
    }

    public ResultActions regUser(final User user) throws Exception {
        final var request = post(USER_CONTROLLER_PATH)
                .content(asJson(user))
                .contentType(APPLICATION_JSON);
        return perform(request);
    }

    public ResultActions perform(final MockHttpServletRequestBuilder request, final User user) throws Exception {
        final String token = jwtTokenUtil.generateToken(user);
        request.header(AUTHORIZATION, "Bearer " + token);

        return perform(request);
    }

    public ResultActions perform(final MockHttpServletRequestBuilder request) throws Exception {
        return mockMvc.perform(request);
    }

    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    public static String asJson(final Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }

    public static <T> T fromJson(final String json, final TypeReference<T> to) throws JsonProcessingException {
        return MAPPER.readValue(json, to);
    }

}
