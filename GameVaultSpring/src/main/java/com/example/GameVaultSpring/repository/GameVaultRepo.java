package com.example.GameVaultSpring.repository;

import com.example.GameVaultSpring.entity.Gamevault;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameVaultRepo extends JpaRepository<Gamevault, Integer> {
}
