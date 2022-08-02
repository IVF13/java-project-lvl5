package hexlet.code.app.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDTO {

    @Size(min = 1)
    private String name;

    private String description;

    private Long executorId;

    private List<Long> labelIds;

    @NotNull
    private Long taskStatusId;

    public TaskRequestDTO(String name, String description, Long executorId, Long taskStatusId) {
        this.name = name;
        this.description = description;
        this.executorId = executorId;
        this.taskStatusId = taskStatusId;
    }

}
