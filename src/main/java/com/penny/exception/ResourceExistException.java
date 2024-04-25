package com.penny.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ResourceExistException extends RuntimeException {
    public ResourceExistException(String message) {
        super(message);
    }
}