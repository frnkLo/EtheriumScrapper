package com.example.EtheriumScrapper.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;



import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class ScrapperSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private Long startBlock;
    private Long endBlock;
    private String status;
}
