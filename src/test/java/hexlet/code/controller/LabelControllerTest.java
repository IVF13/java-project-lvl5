package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.configuration.SecurityConfiguration;
import hexlet.code.configuration.SpringConfigTests;
import hexlet.code.model.Label;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;
import static hexlet.code.controller.LabelController.LABEL_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles(SpringConfigTests.TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = {SpringConfigTests.class, SecurityConfiguration.class})
public class LabelControllerTest {

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
    public void getLabelByIdTest() throws Exception {
        utils.regDefaultUser();
        utils.createDefaultLabel();

        User existsUser = userRepository.findByEmail(TestUtils.TEST_USERNAME).get();
        Label existsLabel = labelRepository.findByName(TestUtils.TEST_LABEL_NAME).get();

        final var response = utils.perform(
                        get(LABEL_CONTROLLER_PATH + LABEL_ID, existsLabel.getId()),
                        existsUser
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final Label label = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(existsLabel.getId(), label.getId());
        assertEquals("testLabelName", label.getName());
    }

    @Test
    public void getAllLabelsTest() throws Exception {
        utils.regDefaultUser();
        User existsUser = userRepository.findByEmail(TestUtils.TEST_USERNAME).get();

        utils.createLabel(new Label(null, "new", null, null));
        utils.createLabel(new Label(null, "one", null, null));
        utils.createLabel(new Label(null, "label", null, null));

        final var response = utils.perform(
                        MockMvcRequestBuilders.get(TaskStatusController.TASK_STATUS_CONTROLLER_PATH),
                        existsUser
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<Label> labels = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(3, labels.size());
        assertEquals("new", labels.get(0).getName());
        assertEquals("one", labels.get(1).getName());
        assertEquals("label", labels.get(2).getName());
    }

    @Test
    public void createLabelTest() throws Exception {
        assertEquals(0, labelRepository.count());
        utils.createDefaultLabel();
        assertEquals(1, labelRepository.count());
    }

    @Test
    public void updateLabelTest() throws Exception {
        utils.regDefaultUser();
        utils.createDefaultLabel();

        User existsUser = userRepository.findByEmail(TestUtils.TEST_USERNAME).get();
        Label existsLabel = labelRepository.findByName(TestUtils.TEST_LABEL_NAME).get();

        final var response1 = utils.perform(
                        get(LABEL_CONTROLLER_PATH + LABEL_ID, existsLabel.getId()),
                        existsUser
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final Label label1 = TestUtils.fromJson(response1.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(existsLabel.getId(), label1.getId());
        assertEquals(TestUtils.TEST_LABEL_NAME, label1.getName());

        final var response2 = utils.perform(
                        put(LABEL_CONTROLLER_PATH + LABEL_ID, existsLabel.getId())
                                .content(TestUtils.asJson(new Label(null, "newName", null, null)))
                                .contentType(APPLICATION_JSON),
                        existsUser
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final Label label2 = TestUtils.fromJson(response2.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(existsLabel.getId(), label2.getId());
        assertEquals("newName", label2.getName());
    }

    @Test
    public void deleteLabelTest() throws Exception {
        utils.regDefaultUser();
        utils.createDefaultLabel();

        User existsUser = userRepository.findByEmail(TestUtils.TEST_USERNAME).get();
        Label existsLabel = labelRepository.findByName(TestUtils.TEST_LABEL_NAME).get();

        assertEquals(1, labelRepository.count());

        utils.perform(delete(LABEL_CONTROLLER_PATH + LABEL_ID, existsLabel.getId()),
                        existsUser)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertEquals(0, labelRepository.count());
    }
}
