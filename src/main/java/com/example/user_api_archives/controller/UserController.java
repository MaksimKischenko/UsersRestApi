package com.example.user_api_archives.controller;

import com.example.user_api_archives.failure.UserActionException;
import com.example.user_api_archives.failure.UserNotFoundException;
import com.example.user_api_archives.model.User;
import com.example.user_api_archives.repository.UsersRepository;
import com.example.user_api_archives.utils.UserModelAssembler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Slf4j
@RestController
public class UserController {
    private final UserModelAssembler assembler;
    private final UsersRepository repository;

    public UserController(UserModelAssembler assembler, UsersRepository repository) {
        this.assembler = assembler;
        this.repository = repository;
    }

    @GetMapping("/users")
    @Cacheable("users")
    public CollectionModel<EntityModel<User>> all() {
        List<EntityModel<User>> employees = repository.findAll().stream() //
                .map(assembler::toModel) //
                .collect(Collectors.toList());
        return CollectionModel.of(employees, linkTo(methodOn(UserController.class).all()).withSelfRel());
    }

    @GetMapping("/users/{userId}")
    public CollectionModel<EntityModel<User>> one(@PathVariable Long userId) {
        User user = repository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(userId)
        );
        EntityModel<User> entityModel = assembler.toModel(user);
        List<EntityModel<User>> tempList = new ArrayList<>();
        tempList.add(entityModel);
        return CollectionModel.of(tempList, linkTo(methodOn(UserController.class).one(userId)).withSelfRel());
    }

    @PostMapping("/users")
    public CollectionModel<?> newUser(@RequestBody User newUser) {
        return newUserCheckDuplicatedFieldsAndSave(newUser);
    }

    @PutMapping("/users/{userId}")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.PUT, RequestMethod.OPTIONS})
    public CollectionModel<?> updateUser(@PathVariable Long userId, @RequestBody User newUser) {
        User updatedUser = repository.findById(userId).map(user -> {
            user.setName(newUser.getName());
            user.setLogin(newUser.getLogin());
            user.setEmail(newUser.getEmail());
            user.setPassword(newUser.getPassword());
            user.setBlocked(newUser.isBlocked());
            return repository.save(user);
        }).orElseGet(()->{
            newUser.setUserId(userId);
            return repository.save(newUser);
        });
        EntityModel<User> entityModel = assembler.toModel(updatedUser);
        List<EntityModel<User>> tempList = new ArrayList<>();
        tempList.add(entityModel);
        return CollectionModel.of(tempList, linkTo(methodOn(UserController.class).newUser(newUser)).withSelfRel());
    }

    @DeleteMapping("/users/{userId}")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.DELETE, RequestMethod.OPTIONS})
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        if(repository.existsById(userId))  {
            repository.deleteById(userId);
            return ResponseEntity.ok("User deleted");
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    private CollectionModel<?> newUserCheckDuplicatedFieldsAndSave(User newUser) {
        if(newUser.getEmail() != null && repository.existsUserByEmail(newUser.getEmail())) {
            throw new UserActionException("EMAIL");
        } else if (repository.existsUserByName(newUser.getName())) {
            throw new UserActionException("NAME");
        } else if (repository.existsUserByLogin(newUser.getLogin())) {
            throw new UserActionException("LOGIN");
        } else {
            EntityModel<User> entityModel = assembler.toModel(repository.save(newUser));
            List<EntityModel<User>>  tempList = new ArrayList<>();
            tempList.add(entityModel);
            return CollectionModel.of(tempList, linkTo(methodOn(UserController.class).newUser(newUser)).withSelfRel());

        }
    }

    @CachePut("users")
    public CollectionModel<EntityModel<User>> toCache() {
        List<EntityModel<User>> employees = repository.findAll().stream() //
                .map(assembler::toModel) //
                .collect(Collectors.toList());
        return CollectionModel.of(employees, linkTo(methodOn(UserController.class).all()).withSelfRel());
    }
}

