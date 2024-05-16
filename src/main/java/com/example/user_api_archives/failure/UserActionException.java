package com.example.user_api_archives.failure;


public class UserActionException extends RuntimeException {
    public UserActionException(String actionField) {
        super("User with that " + actionField + " already exists");
    }
}