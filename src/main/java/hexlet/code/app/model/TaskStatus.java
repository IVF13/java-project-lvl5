package hexlet.code.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

import static javax.persistence.TemporalType.TIMESTAMP;

@Data
@Entity
@Table(name = "task_statuses")
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1)
    private String name;

    @OneToMany(mappedBy = "task_status", fetch = FetchType.EAGER)
    private List<Task> tasks;

    @CreationTimestamp
    @Temporal(TIMESTAMP)
    private Date createdAt;

    public TaskStatus(String taskStatusName) {
        this.name = taskStatusName;
    }

}
