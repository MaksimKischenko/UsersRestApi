package com.example.user_api_archives.utils;

import com.example.user_api_archives.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@Component
public class UserListUpdateListener implements ApplicationListener<UserListUpdateEvent> {

    UserActionListener userActionManager;

    @Autowired
    public UserListUpdateListener(UserActionListener userActionManager) {
        this.userActionManager = userActionManager;
    }

    @Override
    public void onApplicationEvent(UserListUpdateEvent event) {
        List<User> updateUserList = (List<User>) event.getSource();
        userActionManager.updateListenerList(updateUserList);
    }
}
