package com.example.GameVaultSpring.repository;

import com.example.GameVaultSpring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(String email);
}
