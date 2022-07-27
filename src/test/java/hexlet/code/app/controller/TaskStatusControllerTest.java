package hexlet.code.app.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.app.configuration.SecurityConfiguration;
import hexlet.code.app.configuration.SpringConfigTests;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.TaskStatusRepository;
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
import static hexlet.code.app.controller.TaskStatusController.TASK_STATUS_CONTROLLER_PATH;
import static hexlet.code.app.controller.TaskStatusController.TASK_STATUS_ID;
import static hexlet.code.app.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = {SpringConfigTests.class, SecurityConfiguration.class})
public class TaskStatusControllerTest {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TestUtils utils;

    @AfterEach
    public void clear() {
        utils.tearDown();
    }

    @Test
    public void getTaskStatusByIdTest() throws Exception {
        User user = new User("new name", "new last name", TEST_USERNAME_2, "new pwd");
        utils.regUser(user);
        utils.createDefaultTaskStatus();

        TaskStatus existsTaskStatus = taskStatusRepository.findByName("testTaskStatusName").get();

        final var response = utils.perform(
                        get(TASK_STATUS_CONTROLLER_PATH + TASK_STATUS_ID, existsTaskStatus.getId()),
                        user
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final TaskStatus taskStatus = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(taskStatus.getId(), existsTaskStatus.getId());
        assertEquals(taskStatus.getName(), "testTaskStatusName");
    }

    @Test
    public void getAllTaskStatusesTest() throws Exception {
        User user = new User("new name", "new last name", TEST_USERNAME_2, "new pwd");
        utils.regUser(user);

        utils.createTaskStatus(new TaskStatus("new"));
        utils.createTaskStatus(new TaskStatus("one"));
        utils.createTaskStatus(new TaskStatus("task"));
        utils.createTaskStatus(new TaskStatus("status"));

        final var response = utils.perform(
                        get(TASK_STATUS_CONTROLLER_PATH),
                        user
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<TaskStatus> taskStatuses = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(taskStatuses.size(), 4);
        assertEquals(taskStatuses.get(0).getName(), "new");
        assertEquals(taskStatuses.get(1).getName(), "one");
        assertEquals(taskStatuses.get(2).getName(), "task");
        assertEquals(taskStatuses.get(3).getName(), "status");
    }

    @Test
    public void createTaskStatusTest() throws Exception {
        assertEquals(0, taskStatusRepository.count());
        utils.createTaskStatus(new TaskStatus("new"));
        assertEquals(1, taskStatusRepository.count());
    }

    @Test
    public void updateTaskStatusTest() throws Exception {
        User user = new User("new name", "new last name", TEST_USERNAME_2, "new pwd");
        utils.regUser(user);

        utils.createTaskStatus(new TaskStatus("new"));

        final var response1 = utils.perform(
                        get(TASK_STATUS_CONTROLLER_PATH + TASK_STATUS_ID, 1),
                        user
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final TaskStatus taskStatus1 = fromJson(response1.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(taskStatus1.getId(), 1);
        assertEquals(taskStatus1.getName(), "new");

        final var response2 = utils.perform(
                        put(TASK_STATUS_CONTROLLER_PATH + TASK_STATUS_ID, 1)
                                .content(asJson(new TaskStatus("newName")))
                                .contentType(APPLICATION_JSON),
                        user
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final TaskStatus taskStatus2 = fromJson(response2.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(taskStatus2.getId(), 1);
        assertEquals(taskStatus2.getName(), "newName");
    }

    @Test
    public void deleteTaskStatusTest() throws Exception {
        User user = new User("new name", "new last name", TEST_USERNAME_2, "new pwd");
        utils.regUser(user);

        utils.createTaskStatus(new TaskStatus("new"));
        TaskStatus existsTaskStatus = taskStatusRepository.findByName("new").get();

        assertEquals(1, taskStatusRepository.count());

        final var response1 = utils.perform(
                        delete(TASK_STATUS_CONTROLLER_PATH + TASK_STATUS_ID, existsTaskStatus.getId()),
                        user
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertEquals(0, taskStatusRepository.count());
    }

}