package hexlet.code.app.controller;

import hexlet.code.app.model.entity.Label;
import hexlet.code.app.service.LabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get label by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label found",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Label with that id not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping(path = LABEL_ID)
    public ResponseEntity<Label> getLabelById(@Parameter(description = "Id of label to be found")
                                              @PathVariable String id) {
        Label label = labelService.getLabelById(id);
        return ResponseEntity.ok().body(label);
    }

    @Operation(summary = "Get list of all labels")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all labels",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping(path = "")
    public ResponseEntity<List<Label>> getAllLabels() {
        List<Label> labels = labelService.getAllLabels();
        return ResponseEntity.ok().body(labels);
    }

    @Operation(summary = "Create new label")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Label created",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Label already exists"),
            @ApiResponse(responseCode = "422", description = "Not valid label data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping(path = "")
    public ResponseEntity<Label> createLabel(@Parameter(description = "Label data to save")
                                             @RequestBody @Valid Label label) {
        return ResponseEntity.status(HttpStatus.CREATED).body(labelService.createLabel(label));
    }

    @Operation(summary = "Update user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Label updated",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Label with that id not found"),
            @ApiResponse(responseCode = "422", description = "Not valid label data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping(path = LABEL_ID)
    public ResponseEntity<Label> updateLabel(@Parameter(description = "Id of label to be updated")
                                             @PathVariable String id,
                                             @Parameter(description = "Label data to update")
                                             @RequestBody @Valid Label label) {
        Label updatedLabel = labelService.updateLabel(id, label);
        return ResponseEntity.ok().body(updatedLabel);
    }

    @Operation(summary = "Delete user by his id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label deleted",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Label with that id not found"),
            @ApiResponse(responseCode = "304", description = "Unable to delete label because he has related tasks"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping(path = LABEL_ID)
    public String deleteLabel(@Parameter(description = "Id of label to be deleted")
                              @PathVariable String id) throws RelationException {
        return labelService.deleteLabel(id);
    }
}
