package com.ramonbrand.stockexchange.stockexchange.controller;

import com.ramonbrand.stockexchange.stockexchange.model.PasswordVerification;
import com.ramonbrand.stockexchange.stockexchange.model.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RestController
public class ApiAdmin {

    @Autowired
    private CommodityRepository commodityRepository;
    @Autowired
    private ExchangeRepository exchangeRepository;
    @Autowired
    private TradeRequestRepository tradeRequestRepository;
    @Autowired
    private IndividualRepository individualRepository;

    @CrossOrigin
    @RequestMapping(path = "/api/admin/init")
    public String apiAdminInit(
            HttpServletRequest request
    ) {
        // -- Init Commodities
        commodityRepository.deleteAll();

        Commodity gold = new Commodity();
        gold.id = 0;
        gold.name = "Gold";

        Commodity silver = new Commodity();
        silver.id = 1;
        silver.name = "Silver";

        Commodity oil = new Commodity();
        oil.id = 2;
        oil.name = "Oil";

        commodityRepository.save(gold);
        commodityRepository.save(silver);
        commodityRepository.save(oil);

        // -- Init Individuals
        individualRepository.deleteAll();

        Individual individualA = new Individual();
        individualA.id = 1;

        individualA.username = "james";
        individualA.passwordHash = PasswordVerification.getPasswordHash("james123");

        individualA.addCommodities(0, 7);
        individualA.addCommodities(1, 1);
        individualA.addCommodities(2, 200);
        individualRepository.save(individualA);

        Individual individualB = new Individual();
        individualB.id = 2;

        individualB.username = "laura";
        individualB.passwordHash = PasswordVerification.getPasswordHash("laura123");

        individualB.addCommodities(0, 13);
        individualB.addCommodities(1, 0);
        individualB.addCommodities(2, 611);
        individualRepository.save(individualB);

        // -- Init Exchanges
        exchangeRepository.deleteAll();

        createFakeExchanges();


        // -- Init TradeRequests
        tradeRequestRepository.deleteAll();

        return "Initialized";
    }

    private void createFakeExchanges() {

        Random random = new Random();

        int numberOfExchangesPerCommodity = 500;

        for (int j = 0; j < 3; j++) {
            double previousPrice = 10.0;
            double priceVelocity = 0.0;
            boolean directionIsUp = true;

            for (int i = 0; i < numberOfExchangesPerCommodity; i++) {
                float hoursAgo = (24f / ((float)numberOfExchangesPerCommodity)) * ((float)i);
                //System.out.println("Hours ago: " + hoursAgo);

                // -- Price
                double newPrice = previousPrice;
                double priceFluctuation = random.nextDouble();
                priceFluctuation *= priceFluctuation;

                if(previousPrice < 5.0 || previousPrice > 50.0)
                    priceVelocity = 0.0;

                if(previousPrice < 2.4)
                    newPrice += random.nextDouble();

                if(directionIsUp) {
                    newPrice += priceFluctuation;
                    newPrice += priceVelocity;
                    priceVelocity += priceFluctuation / 4;
                } else {
                    newPrice -= priceFluctuation;
                    newPrice += priceVelocity;
                    priceVelocity -= priceFluctuation / 4;
                }

                if(random.nextDouble() <= 0.4)
                    directionIsUp = !directionIsUp;

                if (newPrice < 0.8)
                    newPrice += random.nextDouble() * 0.5;

                previousPrice = newPrice;
                if(priceVelocity > 0.03)
                    priceVelocity /= 1.3;

                Exchange newExchange = new Exchange();
                newExchange.date = new Date(new Date().getTime() -  ((long)(hoursAgo * 3600 * 1000)));
                newExchange.commodityId = j;
                newExchange.ownerIdBuy = random.nextInt(2);
                newExchange.ownerIdSell = random.nextInt(2);
                newExchange.quantity = random.nextInt(50);
                newExchange.price = newPrice;
                exchangeRepository.save(newExchange);
            }
        }


    }
}
