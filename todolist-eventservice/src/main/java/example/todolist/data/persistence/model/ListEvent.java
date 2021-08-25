package example.todolist.data.persistence.model;

import lombok.Data;
import org.eclipse.persistence.annotations.UuidGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(indexes = {
    @Index(name = "userAccountId", columnList="userAccountId")
})
@UuidGenerator(name = "ListEventIdGenerator")
public class ListEvent {
    @Id
    @GeneratedValue(generator = "ListEventIdGenerator")
    private String id;
    private String userAccountId;

    @Column(length = 1024)
    private String description;
    private String locationZipCode;
    private boolean completed;
    private LocalDateTime dueDate;
    private LocalDateTime createdOn;
    private LocalDateTime lastModifiedOn;
}
