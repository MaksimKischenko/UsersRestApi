package com.example.user_api_archives.utils;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


import com.example.user_api_archives.controller.UserController;
import com.example.user_api_archives.model.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {

  @Override
  public @NotNull EntityModel<User> toModel(@NotNull User user) {
    return EntityModel.of(user, //
        linkTo(methodOn(UserController.class).one(user.getUserId())).withSelfRel(),
        linkTo(methodOn(UserController.class).all()).withRel("users"));
  }
}