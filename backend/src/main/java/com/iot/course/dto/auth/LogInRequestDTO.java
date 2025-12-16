package com.iot.course.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogInRequestDTO {
    private String email;
    private String password;
}
