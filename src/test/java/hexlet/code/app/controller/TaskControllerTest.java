package hexlet.code.app.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.app.configuration.SecurityConfiguration;
import hexlet.code.app.configuration.SpringConfigTests;
import hexlet.code.app.model.DTO.TaskRequestDTO;
import hexlet.code.app.model.entity.Label;
import hexlet.code.app.model.entity.Task;
import hexlet.code.app.model.entity.TaskStatus;
import hexlet.code.app.model.entity.User;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.TaskRepository;
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
import static hexlet.code.app.controller.TaskController.TASK_CONTROLLER_PATH;
import static hexlet.code.app.controller.TaskController.TASK_ID;
import static hexlet.code.app.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = {SpringConfigTests.class, SecurityConfiguration.class})
public class TaskControllerTest {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestUtils utils;


    @AfterEach
    public void clear() {
        utils.tearDown();
    }

    @Test
    public void getTaskByIdTest() throws Exception {
        utils.regDefaultUser();
        utils.createDefaultTaskStatus();

        User existsUser = userRepository.findByEmail(TEST_USERNAME).get();
        TaskStatus existsTaskStatus = taskStatusRepository.findByName(TEST_TASK_STATUS_NAME).get();

        utils.createTask(new TaskRequestDTO("taskName", "taskDescription",
                existsUser.getId(), existsTaskStatus.getId()), existsUser);
        Task existsTask = taskRepository.findByName("taskName").get();

        final var response = utils.perform(
                        get(TASK_CONTROLLER_PATH + TASK_ID, existsTask.getId())
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final Task task = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(task.getId(), existsTask.getId());
        assertEquals(task.getName(), "taskName");
        assertEquals(task.getDescription(), "taskDescription");
        assertEquals(task.getAuthor().getId(), existsUser.getId());
        assertEquals(task.getExecutor().getId(), existsUser.getId());
        assertEquals(task.getTaskStatus().getId(), existsTaskStatus.getId());
    }

    @Test
    public void getAllTasksTest() throws Exception {
        utils.regDefaultUser();
        utils.createDefaultTaskStatus();

        User existsUser = userRepository.findByEmail(TEST_USERNAME).get();
        TaskStatus existsTaskStatus = taskStatusRepository.findByName(TEST_TASK_STATUS_NAME).get();

        utils.createTask(new TaskRequestDTO("taskName1", "taskDescription1",
                existsUser.getId(), existsTaskStatus.getId()), existsUser);
        utils.createTask(new TaskRequestDTO("taskName2", "taskDescription2",
                existsUser.getId(), existsTaskStatus.getId()), existsUser);
        utils.createTask(new TaskRequestDTO("taskName3", "taskDescription3",
                existsUser.getId(), existsTaskStatus.getId()), existsUser);

        final var response = utils.perform(
                        get(TASK_CONTROLLER_PATH)
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<Task> tasks = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(tasks.size(), 3);
        assertEquals(tasks.get(0).getName(), "taskName1");
        assertEquals(tasks.get(1).getName(), "taskName2");
        assertEquals(tasks.get(2).getName(), "taskName3");
    }

    @Test
    public void createTaskTest() throws Exception {
        utils.regDefaultUser();
        utils.createDefaultTaskStatus();

        User existsUser = userRepository.findByEmail(TEST_USERNAME).get();
        TaskStatus existsTaskStatus = taskStatusRepository.findByName(TEST_TASK_STATUS_NAME).get();

        assertEquals(0, taskRepository.count());
        utils.createTask(new TaskRequestDTO("taskName1", "taskDescription1",
                existsUser.getId(), existsTaskStatus.getId()), existsUser);
        assertEquals(1, taskRepository.count());
    }

    @Test
    public void updateTaskTest() throws Exception {
        utils.regDefaultUser();
        utils.createDefaultTaskStatus();
        utils.createDefaultLabel();

        User existsUser = userRepository.findByEmail(TEST_USERNAME).get();
        TaskStatus existsTaskStatus = taskStatusRepository.findByName(TEST_TASK_STATUS_NAME).get();
        Label existsLabel = labelRepository.findByName(TEST_LABEL_NAME).get();

        utils.createTask(new TaskRequestDTO("taskName", "taskDescription",
                existsUser.getId(), List.of(existsLabel.getId()), existsTaskStatus.getId()), existsUser);
        Task existsTask = taskRepository.findByName("taskName").get();

        final var response = utils.perform(
                        get(TASK_CONTROLLER_PATH + TASK_ID, existsTask.getId())
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final Task task = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(task.getId(), existsTask.getId());
        assertEquals(task.getName(), "taskName");
        assertEquals(task.getDescription(), "taskDescription");
        assertEquals(task.getExecutor().getId(), existsUser.getId());
        assertEquals(task.getTaskStatus().getId(), existsTaskStatus.getId());
        assertEquals(task.getLabels().get(0).getName(), existsLabel.getName());

        utils.createTaskStatus(new TaskStatus("anotherTaskStatus"));
        TaskStatus anotherExistsTaskStatus = taskStatusRepository.findByName("anotherTaskStatus").get();
        utils.createLabel(new Label("anotherLabel"));
        Label anotherExistsLabel = labelRepository.findByName("anotherLabel").get();

        final var response2 = utils.perform(
                        put(TASK_CONTROLLER_PATH + TASK_ID, task.getId())
                                .content(asJson(
                                        new TaskRequestDTO("taskNameUpdated", "taskDescriptionUpdated",
                                                null, List.of(existsLabel.getId(), anotherExistsLabel.getId()),
                                                anotherExistsTaskStatus.getId())))
                                .contentType(APPLICATION_JSON), existsUser
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final Task task2 = fromJson(response2.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(task2.getId(), existsTask.getId());
        assertEquals(task2.getName(), "taskNameUpdated");
        assertEquals(task2.getDescription(), "taskDescriptionUpdated");
        assertNull(task2.getExecutor());
        assertEquals(task2.getTaskStatus().getId(), anotherExistsTaskStatus.getId());
        assertEquals(task2.getLabels().get(0).getName(), existsLabel.getName());
        assertEquals(task2.getLabels().get(1).getName(), anotherExistsLabel.getName());
    }

    @Test
    public void deleteTaskTest() throws Exception {
        utils.regDefaultUser();
        utils.createDefaultTaskStatus();

        User existsUser = userRepository.findByEmail(TEST_USERNAME).get();
        TaskStatus existsTaskStatus = taskStatusRepository.findByName(TEST_TASK_STATUS_NAME).get();

        utils.createTask(new TaskRequestDTO("taskName", "taskDescription",
                existsUser.getId(), existsTaskStatus.getId()), existsUser);
        Task existsTask = taskRepository.findByName("taskName").get();

        assertEquals(1, taskRepository.count());

        utils.perform(delete(TASK_CONTROLLER_PATH + TASK_ID, existsTask.getId()), existsUser)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertEquals(0, taskRepository.count());
    }

}
