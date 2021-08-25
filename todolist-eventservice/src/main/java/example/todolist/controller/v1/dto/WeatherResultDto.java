package example.todolist.controller.v1.dto;

import lombok.Data;

@Data
public class WeatherResultDto {
    private String id;
    private String zipCode;
    private String conditions;
    private String windSpeed;
    private String temperature;
    private String lastUpdatedAt;
}
