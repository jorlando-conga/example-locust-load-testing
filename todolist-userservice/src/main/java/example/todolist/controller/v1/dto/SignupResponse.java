package example.todolist.controller.v1.dto;

import lombok.Data;

@Data
public class SignupResponse {
    private boolean success;
    private String message;
    private String redirectUrl;
}
