package com.ramonbrand.stockexchange.stockexchange;

import com.ramonbrand.stockexchange.stockexchange.model.Matcher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StockexchangeApplication {

	public static void main(String[] args) {
		//Matcher.findMatch(34);
		SpringApplication.run(StockexchangeApplication.class, args);
	}
}
