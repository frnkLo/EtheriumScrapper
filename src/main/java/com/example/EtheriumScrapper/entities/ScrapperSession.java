package com.example.EtheriumScrapper.entities;


import jakarta.persistence.*;
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


    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    public enum SessionStatus {
        IN_PROGRESS,
        COMPLETED,
        FAILED,
        STOPPED
    }
}
