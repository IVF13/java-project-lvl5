package hexlet.code.app.controller;

import hexlet.code.app.model.entity.Label;
import hexlet.code.app.service.LabelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RelationException;
import javax.validation.Valid;
import java.util.List;

import static hexlet.code.app.controller.LabelController.LABEL_CONTROLLER_PATH;

@Validated
@RestController
@RequestMapping("${base-url}" + LABEL_CONTROLLER_PATH)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LabelController {

    public static final String LABEL_CONTROLLER_PATH = "/labels";

    public static final String LABEL_ID = "/{id}";

    private final LabelService labelService;

    @GetMapping(path = LABEL_ID)
    public ResponseEntity<Label> getLabelById(@PathVariable String id) {
        Label label = labelService.getLabelById(id);
        return ResponseEntity.ok().body(label);
    }

    @GetMapping(path = "")
    public ResponseEntity<List<Label>> getAllLabels() {
        List<Label> labels = labelService.getAllLabels();
        return ResponseEntity.ok().body(labels);
    }

    @PostMapping(path = "")
    public ResponseEntity<Label> createLabel(@RequestBody @Valid Label label) {
        return ResponseEntity.status(HttpStatus.CREATED).body(labelService.createLabel(label));
    }

    @PutMapping(path = LABEL_ID)
    public ResponseEntity<Label> updateLabel(@PathVariable String id,
                                                       @RequestBody @Valid Label label) {
        Label updatedLabel = labelService.updateLabel(id, label);
        return ResponseEntity.ok().body(updatedLabel);
    }

    @DeleteMapping(path = LABEL_ID)
    public String deleteLabel(@PathVariable String id) throws RelationException {
        return labelService.deleteLabel(id);
    }
}
