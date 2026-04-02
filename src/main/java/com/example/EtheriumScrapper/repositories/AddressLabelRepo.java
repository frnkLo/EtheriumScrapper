package com.example.EtheriumScrapper.repositories;

import com.example.EtheriumScrapper.entities.AddressLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface AddressLabelRepo extends JpaRepository<AddressLabel, String> {
    @Query("SELECT a.address FROM AddressLabel a")
    Set<String> findAllAddresses();
}
