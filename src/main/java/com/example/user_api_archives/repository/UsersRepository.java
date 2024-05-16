package com.example.user_api_archives.repository;

import com.example.user_api_archives.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
    boolean existsUserByName(String name);
    boolean existsUserByEmail(String email);
    boolean existsUserByLogin(String login);
}