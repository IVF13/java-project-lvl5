package hexlet.code.app.model.DTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TaskDTO {

    @Size(min = 1)
    private String name;

    private String description;

    private Long executorId;

    @NotNull
    private Long taskStatusId;

}
