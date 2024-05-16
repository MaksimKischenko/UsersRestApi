package com.example.user_api_archives.utils;
import com.example.user_api_archives.model.User;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


@Getter
@Setter
@Component
@Slf4j
@Scope("singleton")
public class UserActionListener {

    private final String USER_NAME_EXISTS = "User with the same name already exists";
    private final String USER_LOGIN_EXISTS = "User with the same login already exists";
    private final String USER_EMAIL_EXISTS = "User with the same email already exists";
    private final String ACTION_FORBIDDEN = "Refresh page to sync with server";
    private List<User> userList = new ArrayList<>();
    AtomicReference<String> message = new AtomicReference<>("");

//    public ResponseEntity<?> checkActionUser(User currentUser) throws UserActionFailure{
//        log.info("LIST:"+userList);
//        try {
//            userList.forEach(user -> {
//                if (user.getName() !=null && Objects.equals(user.getName(), currentUser.getName())) {
//                    message.set(USER_NAME_EXISTS);
//                    throw new UserActionFailure(USER_NAME_EXISTS);
//                } else if (user.getLogin() !=null && Objects.equals(user.getLogin(), currentUser.getLogin())) {
//                    message.set(USER_LOGIN_EXISTS);
//                    throw new UserActionFailure(USER_LOGIN_EXISTS);
//                } else if (user.getEmail()!=null && Objects.equals(user.getEmail(), currentUser.getEmail())) {
//                    message.set(USER_EMAIL_EXISTS);
//                    throw new UserActionFailure(USER_EMAIL_EXISTS);
//                }
//            });
//            return ResponseEntity.ok(currentUser);
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message.get());
//        }
//    }


    public void updateListenerList(List<User> updateUserList) {
        userList = new ArrayList<>(updateUserList);
    }
}

