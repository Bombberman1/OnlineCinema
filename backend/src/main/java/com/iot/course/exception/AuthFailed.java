package com.iot.course.exception;

public class AuthFailed extends RuntimeException {
    public AuthFailed(String message) {
        super(message);
    }
}
