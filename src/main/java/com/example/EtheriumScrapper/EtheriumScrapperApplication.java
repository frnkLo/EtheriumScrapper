package com.example.EtheriumScrapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EtheriumScrapperApplication {

	public static void main(String[] args) {
		SpringApplication.run(EtheriumScrapperApplication.class, args);
	}

}
