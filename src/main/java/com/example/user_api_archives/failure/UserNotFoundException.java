package com.example.user_api_archives.failure;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(Long id) {
    super("Could not find user " + id);
  }
}