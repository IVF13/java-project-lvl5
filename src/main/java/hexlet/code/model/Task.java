package hexlet.code.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

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

    @NotBlank
    @Size(min = 1, max = 1_000)
    @Column(unique = true)
    private String name;

    private String description;

    @ManyToOne(optional = false, cascade = CascadeType.DETACH)
    @JoinColumn(name = "task_status_id")
    private TaskStatus taskStatus;

    @ManyToOne(optional = false, cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "executor_id")
    private User executor;

    @ManyToMany
    @JoinTable(name = "task_label",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "label_id"))
    private List<Label> labels;

    @CreationTimestamp
    @Temporal(TIMESTAMP)
    private Date createdAt;

}
