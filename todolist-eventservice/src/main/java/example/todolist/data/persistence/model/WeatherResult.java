package example.todolist.data.persistence.model;

import lombok.Data;
import org.eclipse.persistence.annotations.UuidGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
public class WeatherResult {
    @Id
    private String id;
    private String zipCode;
    private String conditions;
    private String temperature;
    private String windSpeed;
    private LocalDateTime eventDate;
    private LocalDateTime createdOn;
    private LocalDateTime lastModifiedOn;
}
