package hexlet.code.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.DTO.TaskRequestDTO;
import hexlet.code.model.entity.Label;
import hexlet.code.model.entity.TaskStatus;
import hexlet.code.model.entity.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.JwtTokenUtil;
import hexlet.code.controller.TaskStatusController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;
import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;
import static hexlet.code.controller.UserController.USER_CONTROLLER_PATH;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Component
public class TestUtils {

    public static final String TEST_USERNAME = "email@email.com";
    public static final String TEST_USERNAME_2 = "email2@email.com";
    public static final String TEST_TASK_STATUS_NAME = "testTaskStatusName";
    public static final String TEST_LABEL_NAME = "testLabelName";
    private final User testRegistrationUser = new User(
            "fname",
            "lname",
            TEST_USERNAME,
            "pwd"
    );
    private final TaskStatus testCreationTaskStatus = new TaskStatus(TEST_TASK_STATUS_NAME);
    private final Label testCreationLabel = new Label(TEST_LABEL_NAME);

    public User getTestRegistrationUser() {
        return testRegistrationUser;
    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public void tearDown() {
        taskRepository.deleteAll();
        taskRepository.flush();
        taskStatusRepository.deleteAll();
        taskStatusRepository.flush();
        labelRepository.deleteAll();
        labelRepository.flush();
        userRepository.deleteAll();
        userRepository.flush();
    }

    public User getUserByEmail(final String email) {
        return userRepository.findByEmail(email).get();
    }

    public ResultActions regDefaultUser() throws Exception {
        return regUser(testRegistrationUser);
    }

    public ResultActions createDefaultTaskStatus() throws Exception {
        return createTaskStatus(testCreationTaskStatus);
    }

    public ResultActions createDefaultLabel() throws Exception {
        return createLabel(testCreationLabel);
    }

    public ResultActions regUser(final User user) throws Exception {
        final var request = post(USER_CONTROLLER_PATH)
                .content(asJson(user))
                .contentType(APPLICATION_JSON);
        return perform(request);
    }

    public ResultActions createTaskStatus(final TaskStatus taskStatus) throws Exception {
        final var request = MockMvcRequestBuilders.post(TaskStatusController.TASK_STATUS_CONTROLLER_PATH)
                .content(asJson(taskStatus))
                .contentType(APPLICATION_JSON);
        return perform(request);
    }

    public ResultActions createLabel(final Label label) throws Exception {
        final var request = post(LABEL_CONTROLLER_PATH)
                .content(asJson(label))
                .contentType(APPLICATION_JSON);
        return perform(request);
    }

    public ResultActions createTask(final TaskRequestDTO taskRequestDTO, User author) throws Exception {
        final var request = post(TASK_CONTROLLER_PATH)
                .content(asJson(taskRequestDTO))
                .contentType(APPLICATION_JSON);
        return perform(request, author);
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
