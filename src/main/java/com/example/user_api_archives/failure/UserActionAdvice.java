package com.example.user_api_archives.failure;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
class UserActionAdvice {
    @ResponseBody
    @ExceptionHandler(UserActionException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    String userNotFoundHandler(UserActionException ex) {
        return ex.getMessage();
    }
}

