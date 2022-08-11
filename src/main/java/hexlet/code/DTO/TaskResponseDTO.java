package hexlet.code.DTO;

import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
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
    private User author;
    private User executor;
    private TaskStatus taskStatus;
    private List<Label> labels;
    private String name;
    private String description;
    private Date createdAt;
}
