package com.example.EtheriumScrapper.scrapper;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Component
@Slf4j
public class Web3Scrapper {

    private final Web3j web3j;

    public Web3Scrapper(@Value("${blockchain.node.url}") String nodeUrl) {
        this.web3j = Web3j.build(new HttpService(nodeUrl));
    }
}
