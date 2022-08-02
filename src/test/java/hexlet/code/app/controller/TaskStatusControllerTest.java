package hexlet.code.app.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.app.configuration.SecurityConfiguration;
import hexlet.code.app.configuration.SpringConfigTests;
import hexlet.code.app.model.entity.TaskStatus;
import hexlet.code.app.model.entity.User;
import hexlet.code.app.repository.TaskStatusRepository;
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
    private UserRepository userRepository;
    @Autowired
    private TestUtils utils;


    @AfterEach
    public void clear() {
        utils.tearDown();
    }

    @Test
    public void getTaskStatusByIdTest() throws Exception {
        utils.regDefaultUser();
        utils.createDefaultTaskStatus();

        User existsUser = userRepository.findByEmail(TEST_USERNAME).get();
        TaskStatus existsTaskStatus = taskStatusRepository.findByName(TEST_TASK_STATUS_NAME).get();

        final var response = utils.perform(
                        get(TASK_STATUS_CONTROLLER_PATH + TASK_STATUS_ID, existsTaskStatus.getId()),
                        existsUser
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final TaskStatus taskStatus = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(existsTaskStatus.getId(), taskStatus.getId());
        assertEquals("testTaskStatusName", taskStatus.getName());
    }

    @Test
    public void getAllTaskStatusesTest() throws Exception {
        utils.regDefaultUser();
        User existsUser = userRepository.findByEmail(TEST_USERNAME).get();

        utils.createTaskStatus(new TaskStatus("new"));
        utils.createTaskStatus(new TaskStatus("one"));
        utils.createTaskStatus(new TaskStatus("task"));
        utils.createTaskStatus(new TaskStatus("status"));

        final var response = utils.perform(
                        get(TASK_STATUS_CONTROLLER_PATH),
                        existsUser
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<TaskStatus> taskStatuses = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(4, taskStatuses.size());
        assertEquals("new", taskStatuses.get(0).getName());
        assertEquals("one", taskStatuses.get(1).getName());
        assertEquals("task", taskStatuses.get(2).getName());
        assertEquals("status", taskStatuses.get(3).getName());
    }

    @Test
    public void createTaskStatusTest() throws Exception {
        assertEquals(0, taskStatusRepository.count());
        utils.createDefaultTaskStatus();
        assertEquals(1, taskStatusRepository.count());
    }

    @Test
    public void updateTaskStatusTest() throws Exception {
        utils.regDefaultUser();
        utils.createDefaultTaskStatus();

        User existsUser = userRepository.findByEmail(TEST_USERNAME).get();
        TaskStatus existsTaskStatus = taskStatusRepository.findByName(TEST_TASK_STATUS_NAME).get();

        final var response1 = utils.perform(
                        get(TASK_STATUS_CONTROLLER_PATH + TASK_STATUS_ID, existsTaskStatus.getId()),
                        existsUser
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final TaskStatus taskStatus1 = fromJson(response1.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(existsTaskStatus.getId(), taskStatus1.getId());
        assertEquals(TEST_TASK_STATUS_NAME, taskStatus1.getName());

        final var response2 = utils.perform(
                        put(TASK_STATUS_CONTROLLER_PATH + TASK_STATUS_ID, existsTaskStatus.getId())
                                .content(asJson(new TaskStatus("newName")))
                                .contentType(APPLICATION_JSON),
                        existsUser
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final TaskStatus taskStatus2 = fromJson(response2.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(existsTaskStatus.getId(), taskStatus2.getId());
        assertEquals("newName", taskStatus2.getName());
    }

    @Test
    public void deleteTaskStatusTest() throws Exception {
        utils.regDefaultUser();
        utils.createDefaultTaskStatus();

        User existsUser = userRepository.findByEmail(TEST_USERNAME).get();
        TaskStatus existsTaskStatus = taskStatusRepository.findByName(TEST_TASK_STATUS_NAME).get();

        assertEquals(1, taskStatusRepository.count());

        utils.perform(delete(TASK_STATUS_CONTROLLER_PATH + TASK_STATUS_ID, existsTaskStatus.getId()),
                        existsUser)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertEquals(0, taskStatusRepository.count());
    }

}
