package com.iot.course.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.iot.course.dto.error.ErrorResponseDTO;
import com.iot.course.exception.Exists;
import com.iot.course.exception.InvalidJwtPayload;
import com.iot.course.exception.NotFound;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFound.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(NotFound ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ErrorResponseDTO(ex.getMessage()));
    }

    @ExceptionHandler(Exists.class)
    public ResponseEntity<ErrorResponseDTO> handleExists(Exists ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(new ErrorResponseDTO(ex.getMessage()));
    }

    @ExceptionHandler(InvalidJwtPayload.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidJwtPayload(InvalidJwtPayload ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(new ErrorResponseDTO(ex.getMessage()));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponseDTO> handleExpiredJwt(ExpiredJwtException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(new ErrorResponseDTO("JWT Token Expired"));
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidJwt(JwtException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(new ErrorResponseDTO("Invalid JWT Token passed"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(new ErrorResponseDTO("Not enough permissions"));
    }

    @ExceptionHandler(InternalError.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDenied(InternalError ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ErrorResponseDTO(ex.getMessage()));
    }

    // @ExceptionHandler(Exception.class)
    // public ResponseEntity<ErrorResponseDTO> handleAny(Exception ex) {
    //     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    //                         .body(new ErrorResponseDTO("Internal server error: " + ex.getMessage()));
    // }
}
