package com.example.EtheriumScrapper.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Deal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private ScrapperSession session;

    private String txId;
    private String traderAddress;
    private String traderLabel;
    private Long blockNumber;
    private LocalDateTime blockTime;

    private String poolAddress;
    private String tokenA;
    private String tokenB;

    private BigDecimal tradeAmountUsd;
    private BigDecimal amountInBaseTokens;
    private BigDecimal feeInEth;
    private BigDecimal bribeInEth;
    private BigDecimal priorityFee;
}
