package com.example.EtheriumScrapper.service;


import com.example.EtheriumScrapper.entities.AddressLabel;
import com.example.EtheriumScrapper.repositories.AddressLabelRepo;
import com.example.EtheriumScrapper.repositories.DealRepo;
import com.example.EtheriumScrapper.scrapper.Web3Scrapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArbitrageService {

    private final Web3Scrapper web3Scrapper;
    private final AddressLabelRepo labelRepo;
    private final DealRepo dealRepo;

    @Transactional
    public void executeScrapingTask(Long startBlock, Long endBlock, List<String> pools) {
        log.info("Starting scraping business logic for blocks {} to {}", startBlock, endBlock);


        Map<String, String> targetDictionary = labelRepo.findAll().stream()
                .collect(Collectors.toMap(
                        label -> label.getAddress().toLowerCase(),
                        AddressLabel::getLabel
                ));



    }
}
