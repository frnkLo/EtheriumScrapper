package com.example.EtheriumScrapper.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;



@Entity
@Data
public class AddressLabel {
    @Id
    private String address;
    private String label;
}