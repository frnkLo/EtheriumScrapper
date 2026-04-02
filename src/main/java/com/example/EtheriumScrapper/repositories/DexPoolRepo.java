package com.example.EtheriumScrapper.repositories;

import com.example.EtheriumScrapper.entities.DexPool;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DexPoolRepo extends JpaRepository<DexPool, String> {
}
