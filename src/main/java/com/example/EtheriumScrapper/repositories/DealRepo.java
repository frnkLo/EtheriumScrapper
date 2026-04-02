package com.example.EtheriumScrapper.repositories;

import com.example.EtheriumScrapper.entities.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealRepo extends JpaRepository<Deal, Long> {
}
