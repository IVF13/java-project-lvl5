package hexlet.code.controller;

import hexlet.code.model.Label;
import hexlet.code.service.LabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.management.relation.RelationException;
import javax.validation.Valid;
import java.util.List;

import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;

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
    @ResponseStatus(HttpStatus.OK)
    public Label getLabelById(@Parameter(description = "Id of label to be found")
                              @PathVariable Long id) {
        Label label = labelService.getLabelById(id);
        return label;
    }

    @Operation(summary = "Get list of all labels")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all labels",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    public List<Label> getAllLabels() {
        List<Label> labels = labelService.getAllLabels();
        return labels;
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
    @ResponseStatus(HttpStatus.CREATED)
    public Label createLabel(@Parameter(description = "Label data to save")
                                             @RequestBody @Valid Label label) {
        return labelService.createLabel(label);
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
    @ResponseStatus(HttpStatus.OK)
    public Label updateLabel(@Parameter(description = "Id of label to be updated")
                                             @PathVariable Long id,
                             @Parameter(description = "Label data to update")
                                             @RequestBody @Valid Label label) {
        Label updatedLabel = labelService.updateLabel(id, label);
        return updatedLabel;
    }

    @Operation(summary = "Delete user by his id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label deleted",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Label with that id not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping(path = LABEL_ID)
    @ResponseStatus(HttpStatus.OK)
    public void deleteLabel(@Parameter(description = "Id of label to be deleted")
                              @PathVariable Long id) throws RelationException {
        labelService.deleteLabel(id);
    }
}
