package com.example.EtheriumScrapper.controller;

import com.example.EtheriumScrapper.entities.Deal;
import com.example.EtheriumScrapper.repositories.DealRepo;
import com.example.EtheriumScrapper.service.ArbitrageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ArbitrageController {

    private final ArbitrageService arbitService;
    private final DealRepo dealRepo;


    @PostMapping("/start-scrapping")
    public ResponseEntity<String> startScrapping(@RequestBody ScrapeRequestDto request) {
        arbitService.executeScrappingTask(
                request.getStartBlock(),
                request.getEndBlock(),
                request.getPoolAddresses()
        );
        return ResponseEntity.ok("Scrapping initiated");
    }

    @GetMapping("/analytics")
    public ResponseEntity<List<Deal>> getAnalyticsData() {
        return ResponseEntity.ok(dealRepo.findAll());
    }
}
