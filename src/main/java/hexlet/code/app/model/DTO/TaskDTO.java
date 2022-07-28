package hexlet.code.app.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {

    @Size(min = 1)
    private String name;

    private String description;

    private Long executorId;

    @NotNull
    private Long taskStatusId;

}
