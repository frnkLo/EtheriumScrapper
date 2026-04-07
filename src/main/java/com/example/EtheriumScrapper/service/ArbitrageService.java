package com.example.EtheriumScrapper.service;

import com.example.EtheriumScrapper.entities.AddressLabel;
import com.example.EtheriumScrapper.entities.Deal;
import com.example.EtheriumScrapper.entities.ScrapperSession;
import com.example.EtheriumScrapper.repositories.AddressLabelRepo;
import com.example.EtheriumScrapper.repositories.DealRepo;
import com.example.EtheriumScrapper.repositories.ScrapperSessionRepo;
import com.example.EtheriumScrapper.scrapper.Web3Scrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final ScrapperSessionRepo sessionRepo;

    @Async
    @Transactional
    public void executeScrappingTask(Long startBlock, Long endBlock, List<String> pools) {
        log.info("Starting background scraping task for blocks {} to {}", startBlock, endBlock);


        ScrapperSession session = new ScrapperSession();
        session.setStartBlock(startBlock);
        session.setEndBlock(endBlock);
        session.setStartedAt(LocalDateTime.now());
        session.setStatus(ScrapperSession.SessionStatus.IN_PROGRESS);
        sessionRepo.save(session);

        try {

            Map<String, String> targetDictionary = labelRepo.findAll().stream()
                    .collect(Collectors.toMap(
                            label -> label.getAddress().toLowerCase(),
                            AddressLabel::getLabel
                    ));


            List<Deal> filteredDeals = web3Scrapper.scanAndFilterTransactions(
                    startBlock,
                    endBlock,
                    pools,
                    targetDictionary
            );

            if (!filteredDeals.isEmpty()) {

                filteredDeals.forEach(deal -> deal.setSession(session));

                dealRepo.saveAll(filteredDeals);
                log.info("Saved {} deals to DB", filteredDeals.size());
            } else {
                log.info("No matching deals found for the given dictionary.");
            }


            session.setStatus(ScrapperSession.SessionStatus.COMPLETED);
            session.setEndedAt(LocalDateTime.now());
            sessionRepo.save(session);

        } catch (Exception e) {
            log.error("Scraping task failed catastrophically!", e);
            session.setStatus(ScrapperSession.SessionStatus.FAILED);
            session.setEndedAt(LocalDateTime.now());
            sessionRepo.save(session);
        }
    }


    public List<Deal> getAllDeals() {
        return dealRepo.findAll();

    }
}