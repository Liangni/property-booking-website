package com.penny.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ResourceDuplicateException extends RuntimeException {
    public ResourceDuplicateException(String message) {
        super(message);
    }
}
