package com.example.user_api_archives.failure;


public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("You Unauthorized");
    }
}