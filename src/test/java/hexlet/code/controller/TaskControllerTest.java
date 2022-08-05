package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.configuration.SecurityConfiguration;
import hexlet.code.configuration.SpringConfigTests;
import hexlet.code.DTO.TaskRequestDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
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
import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;
import static hexlet.code.controller.TaskController.TASK_ID;
import static hexlet.code.utils.TestUtils.*;
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
    public void getAllTasksWthFiltrationOptionTest() throws Exception {
        utils.regDefaultUser();
        utils.createDefaultTaskStatus();
        utils.createDefaultLabel();
        utils.regUser(new User("fname", "lname", TEST_USERNAME_2, "pwddd"));
        utils.createTaskStatus(new TaskStatus("anotherTaskStatus"));
        utils.createLabel(new Label("anotherLabel"));


        User existsUser = userRepository.findByEmail(TEST_USERNAME).get();
        TaskStatus existsTaskStatus = taskStatusRepository.findByName(TEST_TASK_STATUS_NAME).get();
        Label existsLabel = labelRepository.findByName(TEST_LABEL_NAME).get();
        User anotherExistsUser = userRepository.findByEmail(TEST_USERNAME_2).get();
        TaskStatus anotherExistsTaskStatus = taskStatusRepository.findByName("anotherTaskStatus").get();
        Label anotherExistsLabel = labelRepository.findByName("anotherLabel").get();


        utils.createTask(new TaskRequestDTO("taskName1", "taskDescription1",
                existsUser.getId(), List.of(existsLabel.getId()), existsTaskStatus.getId()), existsUser);
        utils.createTask(new TaskRequestDTO("taskName2", "taskDescription2",
                anotherExistsUser.getId(), List.of(anotherExistsLabel.getId()),
                anotherExistsTaskStatus.getId()), anotherExistsUser);
        utils.createTask(new TaskRequestDTO("taskName3", "taskDescription3",
                existsUser.getId(), existsTaskStatus.getId()), anotherExistsUser);


        final var response1 = utils.perform(
                        get(TASK_CONTROLLER_PATH)
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<Task> tasks1 = fromJson(response1.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(tasks1.size(), 3);
        assertEquals(tasks1.get(0).getName(), "taskName1");
        assertEquals(tasks1.get(1).getName(), "taskName2");
        assertEquals(tasks1.get(2).getName(), "taskName3");


        final var response2 = utils.perform(
                        get(TASK_CONTROLLER_PATH + "?taskStatus=" + existsTaskStatus.getId() + "&executorId="
                                + existsUser.getId() + "&labels=" + existsLabel.getId()
                                + "&authorId=" + existsUser.getId())
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<Task> tasks2 = fromJson(response2.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(tasks2.size(), 1);
        assertEquals(tasks2.get(0).getName(), "taskName1");


        final var response3 = utils.perform(
                        get(TASK_CONTROLLER_PATH + "?taskStatus=" + anotherExistsTaskStatus.getId()
                                + "&executorId=" + anotherExistsUser.getId()
                                + "&labels=" + anotherExistsLabel.getId()
                                + "&authorId=" + anotherExistsUser.getId())
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<Task> tasks3 = fromJson(response3.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(tasks3.size(), 1);
        assertEquals(tasks3.get(0).getName(), "taskName2");
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
