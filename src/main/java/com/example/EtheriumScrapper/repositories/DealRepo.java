package com.example.EtheriumScrapper.repositories;

import com.example.EtheriumScrapper.entities.Deal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DealRepo extends JpaRepository<Deal, Long> {
    Page<Deal> findAll(Pageable pageable);

    // Чтобы на фронте можно было посмотреть результаты конкретного запуска
    List<Deal> findBySessionId(UUID sessionId);
}
