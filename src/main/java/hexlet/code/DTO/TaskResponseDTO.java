package hexlet.code.DTO;

import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDTO {
    private Long id;
    private UserDTO author;
    private UserDTO executor;
    private TaskStatus taskStatus;
    private List<Label> labels;
    private String name;
    private String description;
    private Date createdAt;
}
