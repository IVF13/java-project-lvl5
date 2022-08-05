package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.configuration.SecurityConfiguration;
import hexlet.code.configuration.SpringConfigTests;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static hexlet.code.configuration.SpringConfigTests.TEST_PROFILE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
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

        User existsUser = userRepository.findByEmail(TestUtils.TEST_USERNAME).get();
        TaskStatus existsTaskStatus = taskStatusRepository.findByName(TestUtils.TEST_TASK_STATUS_NAME).get();

        final var response = utils.perform(
                        MockMvcRequestBuilders.get(TaskStatusController.TASK_STATUS_CONTROLLER_PATH
                                + TaskStatusController.TASK_STATUS_ID, existsTaskStatus.getId()),
                        existsUser
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final TaskStatus taskStatus = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(existsTaskStatus.getId(), taskStatus.getId());
        assertEquals("testTaskStatusName", taskStatus.getName());
    }

    @Test
    public void getAllTaskStatusesTest() throws Exception {
        utils.regDefaultUser();
        User existsUser = userRepository.findByEmail(TestUtils.TEST_USERNAME).get();

        utils.createTaskStatus(new TaskStatus("new"));
        utils.createTaskStatus(new TaskStatus("one"));
        utils.createTaskStatus(new TaskStatus("task"));
        utils.createTaskStatus(new TaskStatus("status"));

        final var response = utils.perform(
                        MockMvcRequestBuilders.get(TaskStatusController.TASK_STATUS_CONTROLLER_PATH),
                        existsUser
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<TaskStatus> taskStatuses = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() {
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

        User existsUser = userRepository.findByEmail(TestUtils.TEST_USERNAME).get();
        TaskStatus existsTaskStatus = taskStatusRepository.findByName(TestUtils.TEST_TASK_STATUS_NAME).get();

        final var response1 = utils.perform(
                        MockMvcRequestBuilders.get(TaskStatusController.TASK_STATUS_CONTROLLER_PATH
                                + TaskStatusController.TASK_STATUS_ID, existsTaskStatus.getId()),
                        existsUser
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final TaskStatus taskStatus1 = TestUtils.fromJson(response1.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(existsTaskStatus.getId(), taskStatus1.getId());
        Assertions.assertEquals(TestUtils.TEST_TASK_STATUS_NAME, taskStatus1.getName());

        final var response2 = utils.perform(
                        MockMvcRequestBuilders.put(TaskStatusController.TASK_STATUS_CONTROLLER_PATH
                                        + TaskStatusController.TASK_STATUS_ID, existsTaskStatus.getId())
                                .content(TestUtils.asJson(new TaskStatus("newName")))
                                .contentType(APPLICATION_JSON),
                        existsUser
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final TaskStatus taskStatus2 = TestUtils.fromJson(response2.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(existsTaskStatus.getId(), taskStatus2.getId());
        assertEquals("newName", taskStatus2.getName());
    }

    @Test
    public void deleteTaskStatusTest() throws Exception {
        utils.regDefaultUser();
        utils.createDefaultTaskStatus();

        User existsUser = userRepository.findByEmail(TestUtils.TEST_USERNAME).get();
        TaskStatus existsTaskStatus = taskStatusRepository.findByName(TestUtils.TEST_TASK_STATUS_NAME).get();

        assertEquals(1, taskStatusRepository.count());

        utils.perform(MockMvcRequestBuilders.delete(TaskStatusController.TASK_STATUS_CONTROLLER_PATH
                                + TaskStatusController.TASK_STATUS_ID, existsTaskStatus.getId()),
                        existsUser)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertEquals(0, taskStatusRepository.count());
    }

}
