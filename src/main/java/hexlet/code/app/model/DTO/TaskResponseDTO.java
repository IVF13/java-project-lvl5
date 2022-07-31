package hexlet.code.app.model.DTO;

import hexlet.code.app.model.entity.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDTO {
    private Long id;
    private UserDTO author;
    private UserDTO executor;
    private TaskStatus taskStatus;
    private String name;
    private String description;
    private Date createdAt;
}
