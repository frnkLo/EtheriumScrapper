package com.example.EtheriumScrapper.controller;

import lombok.Data;

import java.util.List;

@Data
public class ScrapeRequestDto {

    private Long startBlock;
    private Long endBlock;
    private List<String> poolAddresses;
}
