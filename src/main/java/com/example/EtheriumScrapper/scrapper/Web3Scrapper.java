package com.example.EtheriumScrapper.scrapper;

import com.example.EtheriumScrapper.entities.Deal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Component
@Slf4j
public class Web3Scrapper {

    private final Web3j web3j;

    private static final String SWAP_EVENT_TOPIC = "0xd78ad95fa46c994b6551d0da85fc275fe613ce37657fb8d5e3d130840159d822";

    public Web3Scrapper(@Value("${blockchain.node.url}") String nodeUrl) {
        this.web3j = Web3j.build(new HttpService(nodeUrl));
    }

    public List<Deal> scanAndFilterTransactions(Long startBlock, Long endBlock,
                                                List<String> poolAddresses,
                                                Map<String, String> dictionary) throws Exception {


        List<Deal> foundDeals = new ArrayList<>();

        if (startBlock == null || endBlock == null) {
            log.warn("Invalid parameters: startBlock or endBlock is null! Aborting scan.");
            return foundDeals;
        }

        Map<BigInteger, LocalDateTime> blockTimeCache = new HashMap<>();

        try {
            log.info("Original request: scan from block {} to {}, pools count: {}",
                    startBlock, endBlock, (poolAddresses != null ? poolAddresses.size() : 0));

            /*
            EthFilter filter = new EthFilter(
                    DefaultBlockParameter.valueOf(BigInteger.valueOf(startBlock)),
                    DefaultBlockParameter.valueOf(BigInteger.valueOf(endBlock)),
                    poolAddresses
            );

            filter.addSingleTopic(SWAP_EVENT_TOPIC);
            */

            log.info("Running TEST filter to bypass 'Internal error'...");

            BigInteger latestBlock = web3j.ethBlockNumber().send().getBlockNumber();
            BigInteger safeStartBlock = latestBlock.subtract(BigInteger.valueOf(100));

            List<String> testPools = Collections.singletonList("0x88e6A0c2dDD26FEEb64F039a2c41296FcB3f5640");

            EthFilter filter = new EthFilter(
                    DefaultBlockParameter.valueOf(safeStartBlock),
                    DefaultBlockParameter.valueOf(latestBlock),
                    testPools
            );
            filter.addSingleTopic(SWAP_EVENT_TOPIC);

            log.info("Test filter created for actual blocks: {} to {}", safeStartBlock, latestBlock);

            EthLog ethLog = web3j.ethGetLogs(filter).send();

            if (ethLog.hasError()) {
                log.error("RPC Node Error during scraping: {}", ethLog.getError().getMessage());
                return foundDeals;
            }

            List<EthLog.LogResult> logs = ethLog.getLogs();

            if (logs == null || logs.isEmpty()) {
                log.info("Found 0 swap logs in the specified test block range");
                return foundDeals;
            }

            log.info("Found {} swap logs in the specified test block range", logs.size());

            for (EthLog.LogResult logResult : logs) {
                Log logData = (Log) logResult.get();
                String txHash = logData.getTransactionHash();
                BigInteger blockNumber = logData.getBlockNumber();

                Optional<Transaction> txOptional = web3j.ethGetTransactionByHash(txHash).send().getTransaction();

                if (txOptional.isPresent()) {
                    Transaction tx = txOptional.get();
                    String traderAddress = tx.getFrom().toLowerCase();


                    if (dictionary.containsKey(traderAddress)) {
                        String label = dictionary.get(traderAddress);

                        LocalDateTime actualBlockTime = getBlockTime(blockNumber, blockTimeCache);

                        Deal deal = new Deal();
                        deal.setTxId(tx.getHash());
                        deal.setTraderAddress(traderAddress);
                        deal.setTraderLabel(label);
                        deal.setPoolAddress(logData.getAddress());
                        deal.setBlockNumber(blockNumber.longValue());
                        deal.setBlockTime(actualBlockTime);

                        foundDeals.add(deal);
                        log.info("Found match! Trader: {}, Label: {}", traderAddress, label);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Critical error communicating with Blockchain during scan", e);
            throw e;
        }

        return foundDeals;
    }

    private LocalDateTime getBlockTime(BigInteger blockNumber, Map<BigInteger, LocalDateTime> cache) throws Exception {
        if (cache.containsKey(blockNumber)) {
            return cache.get(blockNumber);
        }

        EthBlock.Block block = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(blockNumber), false)
                .send()
                .getBlock();

        long timestampSeconds = block.getTimestamp().longValue();
        LocalDateTime blockTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestampSeconds), ZoneId.of("UTC"));

        cache.put(blockNumber, blockTime);
        return blockTime;
    }
}