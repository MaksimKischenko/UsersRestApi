package com.example.user_api_archives.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Table(name = "user_", schema = "dev_util")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @JsonProperty("user_id")
    private Long userId;

    @Column(name = "name")
    private String name;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "blocked")
    private boolean blocked;

    @Column(name = "date_blocked")
    @JsonProperty("date_blocked")
    private Timestamp dateBlocked;

    @Column(name = "date_add")
    @JsonProperty("date_add")
    private Timestamp dateAdd;

    @Column(name = "type_id")
    @JsonProperty("type_id")
    private Long typeId;

    @Column(name = "phone_number")
    @JsonProperty("phone_number")
    private String phoneNumber;

    @Column(name = "failed_login")
    @JsonProperty("failed_login")
    private Long failedLogin;

    @Column(name = "email")
    private String email;

    @Column(name = "password_expire")
    @JsonProperty("password_expire")
    private Timestamp passwordExpire;

    @Column(name = "date_change_password")
    @JsonProperty("date_change_password")
    private Timestamp dateChangePassword;

}
