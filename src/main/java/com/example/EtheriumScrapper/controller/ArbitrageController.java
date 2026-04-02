package com.example.EtheriumScrapper.controller;

import com.example.EtheriumScrapper.entities.Deal;
import com.example.EtheriumScrapper.repositories.DealRepo;
import com.example.EtheriumScrapper.service.ArbitrageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/arbitrage")
@RequiredArgsConstructor
//@CrossOrigin(origins = "*") // Для работы с Angular
public class ArbitrageController {

    private final ArbitrageService arbitService;
    private final DealRepo dealRepo;


    @PostMapping("/start-scraping")
    public ResponseEntity<String> startScraping(@RequestBody ScrapeRequestDto request) {
        arbitService.executeScrapingTask(
                request.getStartBlock(),
                request.getEndBlock(),
                request.getPoolAddresses()
        );
        return ResponseEntity.ok("Scraping initiated");
    }

    @GetMapping("/analytics")
    public ResponseEntity<List<Deal>> getAnalyticsData() {
        return ResponseEntity.ok(dealRepo.findAll());
    }
}
