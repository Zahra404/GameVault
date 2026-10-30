package com.example.GameVaultSpring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "game_vault")
public class Gamevault {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL-friendly
    private Integer game_id;

    private String gamename;
    private Integer gamedescription;
    private String gamepath;
    private Integer created_at;


}
