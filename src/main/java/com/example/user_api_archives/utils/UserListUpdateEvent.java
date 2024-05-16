package com.example.user_api_archives.utils;

import com.example.user_api_archives.model.User;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class UserListUpdateEvent extends ApplicationEvent {

    public UserListUpdateEvent(Object source) {
        super(source);
    }
}
