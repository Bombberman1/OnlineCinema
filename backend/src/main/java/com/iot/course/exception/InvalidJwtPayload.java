package com.iot.course.exception;

public class InvalidJwtPayload extends RuntimeException {
    public InvalidJwtPayload(String message) {
        super(message);
    }
}
