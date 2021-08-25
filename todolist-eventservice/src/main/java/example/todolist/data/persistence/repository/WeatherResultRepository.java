package example.todolist.data.persistence.repository;

import example.todolist.data.persistence.model.WeatherResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface WeatherResultRepository extends JpaRepository<WeatherResult, String> {
    DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/uuuu");
    default WeatherResult findWeatherResult(String zipCode, LocalDateTime eventDate) {
        return findById(zipCode + "_" + eventDate.format(DATE_FORMAT)).orElse(null);
    }
    default WeatherResult persist(WeatherResult weatherResult) {
        LocalDateTime now = LocalDateTime.now();
        if (weatherResult.getCreatedOn() == null) {
            weatherResult.setCreatedOn(now);
        }
        weatherResult.setLastModifiedOn(now);
        if (weatherResult.getId() == null) {
            weatherResult.setId(weatherResult.getZipCode() + "_" + weatherResult.getEventDate().format(DATE_FORMAT));
        }
        return save(weatherResult);
    }
}
