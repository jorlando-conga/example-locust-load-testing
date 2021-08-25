package example.todolist.controller.v1.dto;

import lombok.Data;

@Data
public class UserAccountDto {
    private String firstName;
    private String lastName;
    private String emailAddress;
}
