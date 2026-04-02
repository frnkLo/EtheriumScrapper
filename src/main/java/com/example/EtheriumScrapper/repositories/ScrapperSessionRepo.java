package com.example.EtheriumScrapper.repositories;

import com.example.EtheriumScrapper.entities.ScrapperSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ScrapperSessionRepo extends JpaRepository<ScrapperSession, UUID> {
}
