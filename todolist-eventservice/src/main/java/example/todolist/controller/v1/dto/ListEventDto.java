package example.todolist.controller.v1.dto;

import lombok.Data;

@Data
public class ListEventDto {
    private String id;
    private String description;
    private String dueDate;
    private String zipCode;
    private Boolean completed;
}