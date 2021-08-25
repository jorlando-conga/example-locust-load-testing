package example.todolist.data.persistence.model;

import lombok.Data;
import org.eclipse.persistence.annotations.UuidGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(indexes = {
    @Index(name = "emailAddress", columnList="emailaddress")
})
@UuidGenerator(name = "UserAccountIdGenerator")
public class UserAccount {
    @Id
    @GeneratedValue(generator = "UserAccountIdGenerator")
    private String id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String password;
    private LocalDateTime createdOn;
    private LocalDateTime lastModifiedOn;
}
