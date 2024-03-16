package com.penny.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UnknownException extends RuntimeException{
    public UnknownException(String message) {
        super(message);
    }
}
