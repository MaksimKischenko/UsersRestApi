package com.example.user_api_archives.controller;

import com.example.user_api_archives.model.User;
import com.example.user_api_archives.service.UserRemoteService;
import com.example.user_api_archives.utils.UserModelAssembler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserRemoteController {
    private final UserRemoteService service;
    private final UserModelAssembler assembler;


    @PostMapping("/auth")
    public void auth() throws IOException {
        service.auth();
    }

    @GetMapping("/user_")
    public CollectionModel<EntityModel<User>> all() throws IOException {
        List<EntityModel<User>> employees = service.doGet().stream() //
                .map(assembler::toModel) //
                .collect(Collectors.toList());
        return CollectionModel.of(employees, linkTo(methodOn(UserController.class).all()).withSelfRel());
    }

}
