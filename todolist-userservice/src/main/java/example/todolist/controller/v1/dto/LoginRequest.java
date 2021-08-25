package example.todolist.controller.v1.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String emailAddress;
    private String password;
}
