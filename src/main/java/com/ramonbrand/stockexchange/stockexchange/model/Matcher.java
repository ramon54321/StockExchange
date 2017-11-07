package com.ramonbrand.stockexchange.stockexchange.model;

import com.ramonbrand.stockexchange.stockexchange.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Random;

@RestController
public class Matcher {

    public static Random random = new Random(); // Testing random

    @Autowired
    CommodityRepository commodityRepository;

    @RequestMapping("/add")
    public String apiAdd() {
        Commodity commodity = new Commodity();
        commodity.name = "Oil";
        commodity.value = 1.553881;
        commodity.testData.add(3.112);
        commodity.testData.add(3.114);
        commodity.testData.add(3.121);
        commodity.testData.add(3.129);
        commodity.testData.add(3.123);
        commodity.testData.add(3.101);
        commodity.testData.add(3.007);
        commodity.testData.add(3.001);
        commodityRepository.save(commodity);
        return "added.";
    }

    @RequestMapping("/listvalues")
    public String apiListValues() {

        Commodity oil = commodityRepository.findOne(1l);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<ul>");
        for (int i = 0; i < oil.testData.size(); i++) {
            stringBuilder.append("<li>");
            stringBuilder.append(oil.testData.get(i).doubleValue());
            stringBuilder.append("</li>");
        }
        stringBuilder.append("</ul>");

        return stringBuilder.toString();
    }

    @RequestMapping("/match")
    public String apiMatch() {
        //findMatch(3);
        return "Matcher ran.";
    }

    /**
     * Takes id of request to try match. The request is tested against all other requests of the opposite direction.
     * @param idToMatch
     */
    public static void findMatch(long idToMatch){

        TradeRequest requestToMatchTo = new TradeRequest();
        requestToMatchTo.id = random.nextInt(1000000);
        requestToMatchTo.tradeRequestType = TradeRequestType.SELL;
        requestToMatchTo.commodityId = 1;
        requestToMatchTo.quantity = 5;
        requestToMatchTo.price = 1.20;
        requestToMatchTo.address = "laura's client";
        requestToMatchTo.ownerId = 60;
        TradeRequest tradeRequest = requestToMatchTo; // -- This will be the db get with the idToMatch

        if(tradeRequest.tradeRequestType == TradeRequestType.BUY){
            // -- Find a sell to match the buy
            // -- The sell cannot be of higher price than what the offer to buy is
            double maxPrice = tradeRequest.price;

            // -- The commodity must be the same;
            long commodityId = tradeRequest.commodityId;

            ArrayList<TradeRequest> matchedRequests = new ArrayList<TradeRequest>(); // -- This will be search results

            // -- Temp fake data
            TradeRequest match1 = new TradeRequest();
            match1.id = random.nextInt(1000000);
            match1.tradeRequestType = TradeRequestType.SELL;
            match1.commodityId = 1;
            match1.quantity = 1;
            match1.price = 1.25;
            match1.address = "bob's client";
            match1.ownerId = 50;
            matchedRequests.add(match1);
            TradeRequest match2 = new TradeRequest();
            match2.id = random.nextInt(1000000);
            match2.tradeRequestType = TradeRequestType.SELL;
            match2.commodityId = 1;
            match2.quantity = 2;
            match2.price = 1.29;
            match2.address = "bob's client";
            match2.ownerId = 51;
            matchedRequests.add(match2);
            TradeRequest match3 = new TradeRequest();
            match3.id = random.nextInt(1000000);
            match3.tradeRequestType = TradeRequestType.SELL;
            match3.commodityId = 1;
            match3.quantity = 8;
            match3.price = 1.22;
            match3.address = "bob's client";
            match3.ownerId = 56;
            matchedRequests.add(match3);

            int matchIndex = 0;
            while(requestToMatchTo.quantity > 0) {
                TradeRequest match = matchedRequests.get(matchIndex);
                matchIndex++;

                // -- Check if match has enough to fulfill buy
                if (match.quantity >= requestToMatchTo.quantity) {
                    System.out.println(" --- Last Exchange to satisfy match ---");
                    // -- Calculate quantity to transfer
                    long quantity = requestToMatchTo.quantity;

                    // -- Create new exchange data
                    Exchange exchange = new Exchange();
                    exchange.commodityId = requestToMatchTo.commodityId;
                    exchange.quantity = quantity;
                    exchange.price = match.price;
                    exchange.ownerIdSell = match.ownerId;
                    exchange.ownerIdBuy = requestToMatchTo.ownerId;

                    // Log exchange
                    System.out.println("Logging Exchange: " + exchange);

                    // -- Decrement request quantity
                    requestToMatchTo.quantity -= quantity;
                    match.quantity -= quantity;

                    // ------ Transfer
                    // Add commodities to buyer
                    System.out.println("Transfering " + quantity);
                    // Remove commodities from seller
                    // ---------------
                } else {
                    // -- Match does not have enough quantity
                    System.out.println(" --- Intermediate exchange ---");
                    // -- Calculate quantity to transfer
                    long quantity = match.quantity;

                    // -- Create new exchange data
                    Exchange exchange = new Exchange();
                    exchange.commodityId = requestToMatchTo.commodityId;
                    exchange.quantity = quantity;
                    exchange.price = match.price;
                    exchange.ownerIdSell = match.ownerId;
                    exchange.ownerIdBuy = requestToMatchTo.ownerId;

                    // Log exchange
                    System.out.println("Logging Exchange: " + exchange);

                    // -- Decrement request quantity
                    requestToMatchTo.quantity -= quantity;
                    match.quantity -= quantity;

                    // ------ Transfer
                    // Add commodities to buyer
                    System.out.println("Transfering " + quantity);
                    // Remove commodities from seller
                    // ---------------
                }

                if(requestToMatchTo.quantity == 0){
                    // Delete request record
                    System.out.println("Deleting requestToMatchTo");
                } else {
                    // Update request record
                    System.out.println("Updating requestToMatchTo");
                }

                if(match.quantity == 0){
                    // Delete match record
                    System.out.println("Deleting match");
                } else {
                    // Update request record
                    System.out.println("Updating match");
                }
            }

        } else {
            // -- Find a buy to match the sell
            // -- The buy cannot be of lower price than what the offer to sell is
            double minPrice = tradeRequest.price;

            // -- The commodity must be the same;
            long commodityId = tradeRequest.commodityId;

            ArrayList<TradeRequest> matchedRequests = new ArrayList<TradeRequest>(); // -- This will be search results

            // -- Temp fake data
            TradeRequest match1 = new TradeRequest();
            match1.id = random.nextInt(1000000);
            match1.tradeRequestType = TradeRequestType.BUY;
            match1.commodityId = 1;
            match1.quantity = 1;
            match1.price = 1.22;
            match1.address = "bob's client";
            match1.ownerId = 50;
            matchedRequests.add(match1);
            TradeRequest match2 = new TradeRequest();
            match2.id = random.nextInt(1000000);
            match2.tradeRequestType = TradeRequestType.BUY;
            match2.commodityId = 1;
            match2.quantity = 2;
            match2.price = 1.23;
            match2.address = "bob's client";
            match2.ownerId = 51;
            matchedRequests.add(match2);
            TradeRequest match3 = new TradeRequest();
            match3.id = random.nextInt(1000000);
            match3.tradeRequestType = TradeRequestType.BUY;
            match3.commodityId = 1;
            match3.quantity = 8;
            match3.price = 1.26;
            match3.address = "bob's client";
            match3.ownerId = 56;
            matchedRequests.add(match3);

            int matchIndex = 0;
            while(requestToMatchTo.quantity > 0) {
                TradeRequest match = matchedRequests.get(matchIndex);
                matchIndex++;

                // -- Check if match has enough to fulfill buy
                if (match.quantity >= requestToMatchTo.quantity) {
                    System.out.println(" --- Last Exchange to satisfy match ---");
                    // -- Calculate quantity to transfer
                    long quantity = requestToMatchTo.quantity;

                    // -- Create new exchange data
                    Exchange exchange = new Exchange();
                    exchange.commodityId = requestToMatchTo.commodityId;
                    exchange.quantity = quantity;
                    exchange.price = match.price;
                    exchange.ownerIdSell = requestToMatchTo.ownerId;
                    exchange.ownerIdBuy = match.ownerId;

                    // Log exchange
                    System.out.println("Logging Exchange: " + exchange);

                    // -- Decrement request quantity
                    requestToMatchTo.quantity -= quantity;
                    match.quantity -= quantity;

                    // ------ Transfer
                    // Add commodities to buyer
                    System.out.println("Transfering " + quantity);
                    // Remove commodities from seller
                    // ---------------
                } else {
                    // -- Match does not have enough quantity
                    System.out.println(" --- Intermediate exchange ---");
                    // -- Calculate quantity to transfer
                    long quantity = match.quantity;

                    // -- Create new exchange data
                    Exchange exchange = new Exchange();
                    exchange.commodityId = requestToMatchTo.commodityId;
                    exchange.quantity = quantity;
                    exchange.price = match.price;
                    exchange.ownerIdSell = requestToMatchTo.ownerId;
                    exchange.ownerIdBuy = match.ownerId;

                    // Log exchange
                    System.out.println("Logging Exchange: " + exchange);

                    // -- Decrement request quantity
                    requestToMatchTo.quantity -= quantity;
                    match.quantity -= quantity;

                    // ------ Transfer
                    // Add commodities to buyer
                    System.out.println("Transfering " + quantity);
                    // Remove commodities from seller
                    // ---------------
                }

                if(requestToMatchTo.quantity == 0){
                    // Delete request record
                    System.out.println("Deleting requestToMatchTo");
                } else {
                    // Update request record
                    System.out.println("Updating requestToMatchTo");
                }

                if(match.quantity == 0){
                    // Delete match record
                    System.out.println("Deleting match");
                } else {
                    // Update request record
                    System.out.println("Updating match");
                }
            }
        }
    }

}
