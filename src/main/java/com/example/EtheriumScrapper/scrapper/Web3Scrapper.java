package com.example.EtheriumScrapper.scrapper;


import com.example.EtheriumScrapper.entities.Deal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class Web3Scrapper {

    private final Web3j web3j;

    public Web3Scrapper(@Value("${blockchain.node.url}") String nodeUrl) {
        this.web3j = Web3j.build(new HttpService(nodeUrl));
    }


    public List<Deal> scanAndFilterTransactions(Long startBlock, Long endBlock,
                                                List<String> poolAddresses,
                                                Map<String, String> dictionary) {
        List<Deal> foundDeals = new ArrayList<>();

        try {

            EthFilter filter = new EthFilter(
                    DefaultBlockParameter.valueOf(BigInteger.valueOf(startBlock)),
                    DefaultBlockParameter.valueOf(BigInteger.valueOf(endBlock)),
                    poolAddresses
            );

            filter.addSingleTopic("0xd78ad95fa46c994b6551d0da85fc275fe613ce37657fb8d5e3d130840159d822");

            List<EthLog.LogResult> logs = web3j.ethGetLogs(filter).send().getLogs();

            for (EthLog.LogResult logResult : logs) {
                Log logData = (Log) logResult.get();
                String txHash = logData.getTransactionHash();


                Optional<Transaction> txOptional = web3j.ethGetTransactionByHash(txHash).send().getTransaction();

                if (txOptional.isPresent()) {
                    Transaction tx = txOptional.get();
                    String traderAddress = tx.getFrom().toLowerCase();


                    if (dictionary.containsKey(traderAddress)) {
                        String label = dictionary.get(traderAddress);

                        Deal deal = new Deal();
                        deal.setTxId(tx.getHash());
                        deal.setTraderAddress(traderAddress);
                        deal.setTraderLabel(label);
                        deal.setPoolAddress(logData.getAddress());
                        deal.setBlockNumber(tx.getBlockNumber().longValue());
                        deal.setBlockTime(LocalDateTime.now());
                        foundDeals.add(deal);
                        log.info("Found match! Trader: {}, Label: {}", traderAddress, label);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error communicating with Blockchain", e);
        }

        return foundDeals;
    }
}
