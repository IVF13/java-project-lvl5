package hexlet.code.app.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

@Data
@Entity
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1)
    private String name;

    private String description;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.DETACH)
    @JoinColumn(name = "task_status_id")
    private TaskStatus taskStatus;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "executor_id")
    private User executor;

    @CreationTimestamp
    @Temporal(TIMESTAMP)
    private Date createdAt;

}
