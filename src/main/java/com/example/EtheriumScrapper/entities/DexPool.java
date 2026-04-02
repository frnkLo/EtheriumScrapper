package com.example.EtheriumScrapper.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class DexPool {

    @Id
    private String poolAddress;
    private String deployerAddress;
    private String tokenA;
    private String tokenB;
    private BigDecimal liquidityUsd;
}
