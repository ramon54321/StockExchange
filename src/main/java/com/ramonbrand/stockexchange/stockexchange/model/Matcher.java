package com.ramonbrand.stockexchange.stockexchange.model;

import com.ramonbrand.stockexchange.stockexchange.model.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class Matcher {

    @Autowired
    private CommodityRepository commodityRepository;
    @Autowired
    private TradeRequestRepository tradeRequestRepository;
    @Autowired
    private ExchangeRepository exchangeRepository;

    /**
     * Takes id of request to try match. The request is tested against all other requests of the opposite direction.
     * @param idToMatch
     */
    public void findMatch(long idToMatch){

        TradeRequest tradeRequest = tradeRequestRepository.findOne(idToMatch);

        if(tradeRequest.tradeRequestType == TradeRequestType.BUY){
            // -- Find a sell to match the buy
            // -- The sell cannot be of higher price than what the offer to buy is
            double maxPrice = tradeRequest.price;

            // -- The commodity must be the same;
            long commodityId = tradeRequest.commodityId;

            List<TradeRequest> matchedRequests = tradeRequestRepository.getSells(maxPrice);// new ArrayList<TradeRequest>(); // -- This will be search results

            System.out.println(matchedRequests.size() + " records in TradeRequests.");

            if(matchedRequests.size() == 0) {
                System.out.println("No possible matches. Request remains in pool.");
                return;
            }

            int matchIndex = 0;
            while(tradeRequest.quantity > 0) {
                if(matchedRequests.size() <= matchIndex) {
                    System.out.println("Ran out of possible matches, trade not entirely fulfilled.");
                    break;
                }

                TradeRequest match = matchedRequests.get(matchIndex);
                matchIndex++;

                // -- Check if match has enough to fulfill buy
                if (match.quantity >= tradeRequest.quantity) {
                    System.out.println(" --- Last Exchange to satisfy match ---");
                    // -- Calculate quantity to transfer
                    long quantity = tradeRequest.quantity;

                    // -- Create new exchange data
                    Exchange exchange = new Exchange();
                    exchange.commodityId = tradeRequest.commodityId;
                    exchange.quantity = quantity;
                    exchange.price = tradeRequest.price;
                    exchange.date = new Date();
                    exchange.ownerIdSell = match.ownerId;
                    exchange.ownerIdBuy = tradeRequest.ownerId;

                    // -- Log exchange
                    System.out.println("Logging Exchange: " + exchange);
                    exchangeRepository.save(exchange);

                    // -- Decrement request quantity
                    tradeRequest.quantity -= quantity;
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
                    exchange.commodityId = tradeRequest.commodityId;
                    exchange.quantity = quantity;
                    exchange.price = tradeRequest.price;
                    exchange.date = new Date();
                    exchange.ownerIdSell = match.ownerId;
                    exchange.ownerIdBuy = tradeRequest.ownerId;

                    // -- Log exchange
                    System.out.println("Logging Exchange: " + exchange);
                    exchangeRepository.save(exchange);

                    // -- Decrement request quantity
                    tradeRequest.quantity -= quantity;
                    match.quantity -= quantity;

                    // ------ Transfer
                    // Add commodities to buyer
                    System.out.println("Transfering " + quantity);
                    // Remove commodities from seller
                    // ---------------
                }

                if(tradeRequest.quantity == 0){
                    // Delete request record
                    System.out.println("Deleting requestToMatchTo");
                    tradeRequestRepository.delete(tradeRequest);
                } else {
                    // Update request record
                    System.out.println("Updating requestToMatchTo");
                    tradeRequestRepository.save(tradeRequest);
                }

                if(match.quantity == 0){
                    // Delete match record
                    System.out.println("Deleting match");
                    tradeRequestRepository.delete(match);
                } else {
                    // Update request record
                    System.out.println("Updating match");
                    tradeRequestRepository.save(match);
                }
            }

        } else {
            // -- Find a buy to match the sell
            // -- The buy cannot be of lower price than what the offer to sell is
            double minPrice = tradeRequest.price;

            // -- The commodity must be the same;
            long commodityId = tradeRequest.commodityId;

            List<TradeRequest> matchedRequests = tradeRequestRepository.getBuys(minPrice);// new ArrayList<TradeRequest>(); // -- This will be search results

            System.out.println(matchedRequests.size() + " records in TradeRequests.");

            if(matchedRequests.size() == 0) {
                System.out.println("No possible matches. Request remains in pool.");
                return;
            }

            int matchIndex = 0;
            while(tradeRequest.quantity > 0) {
                if(matchedRequests.size() <= matchIndex) {
                    System.out.println("Ran out of possible matches, trade not entirely fulfilled.");
                    break;
                }

                TradeRequest match = matchedRequests.get(matchIndex);
                matchIndex++;

                // -- Check if match has enough to fulfill buy
                if (match.quantity >= tradeRequest.quantity) {
                    System.out.println(" --- Last Exchange to satisfy match ---");
                    // -- Calculate quantity to transfer
                    long quantity = tradeRequest.quantity;

                    // -- Create new exchange data
                    Exchange exchange = new Exchange();
                    exchange.commodityId = tradeRequest.commodityId;
                    exchange.quantity = quantity;
                    exchange.price = tradeRequest.price;
                    exchange.date = new Date();
                    exchange.ownerIdSell = tradeRequest.ownerId;
                    exchange.ownerIdBuy = match.ownerId;

                    // -- Log exchange
                    System.out.println("Logging Exchange: " + exchange);
                    exchangeRepository.save(exchange);

                    // -- Decrement request quantity
                    tradeRequest.quantity -= quantity;
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
                    exchange.commodityId = tradeRequest.commodityId;
                    exchange.quantity = quantity;
                    exchange.price = tradeRequest.price;
                    exchange.date = new Date();
                    exchange.ownerIdSell = tradeRequest.ownerId;
                    exchange.ownerIdBuy = match.ownerId;

                    // -- Log exchange
                    System.out.println("Logging Exchange: " + exchange);
                    exchangeRepository.save(exchange);

                    // -- Decrement request quantity
                    tradeRequest.quantity -= quantity;
                    match.quantity -= quantity;

                    // ------ Transfer
                    // Add commodities to buyer
                    System.out.println("Transfering " + quantity);
                    // Remove commodities from seller
                    // ---------------
                }

                if(tradeRequest.quantity == 0){
                    // Delete request record
                    System.out.println("Deleting requestToMatchTo");
                    tradeRequestRepository.delete(tradeRequest);
                } else {
                    // Update request record
                    System.out.println("Updating requestToMatchTo");
                    tradeRequestRepository.save(tradeRequest);
                }

                if(match.quantity == 0){
                    // Delete match record
                    System.out.println("Deleting match");
                    tradeRequestRepository.delete(match);
                } else {
                    // Update request record
                    System.out.println("Updating match");
                    tradeRequestRepository.save(match);
                }
            }
        }
    }
}
