package com.ramonbrand.stockexchange.stockexchange.controller;

import com.ramonbrand.stockexchange.stockexchange.model.Matcher;
import com.ramonbrand.stockexchange.stockexchange.model.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ApiTrade {

    @Autowired
    private CommodityRepository commodityRepository;
    @Autowired
    private ExchangeRepository exchangeRepository;
    @Autowired
    private TradeRequestRepository tradeRequestRepository;

    @Autowired
    private Matcher matcher;

    @CrossOrigin
    @RequestMapping("/api/buy/{comId}/{price}/{qty}")
    public String apiTradeRequestBuy(
            HttpServletRequest request,
            @PathVariable("comId") long comId,
            @PathVariable("price") double price,
            @PathVariable("qty") long qty
    ) {

        // -- Create new trade request
        TradeRequest newRequest = new TradeRequest();
        newRequest.tradeRequestType = TradeRequestType.BUY;
        newRequest.commodityId = comId;
        newRequest.quantity = qty;
        newRequest.price = price;
        newRequest.address = "laura's client";
        newRequest.ownerId = 60;

        // -- Save request to db
        tradeRequestRepository.save(newRequest);

        // -- Run Matcher
        matcher.findMatch(newRequest.id);

        return "Trade request added: BUY " + qty + " @ " + price + " of " + comId + " from " + request.getRemoteAddr() + " + " + request.getRemotePort();
    }

    @CrossOrigin
    @RequestMapping("/api/sell/{comId}/{price}/{qty}")
    public String apiTradeRequestSell(
            HttpServletRequest request,
            @PathVariable("comId") long comId,
            @PathVariable("price") double price,
            @PathVariable("qty") long qty
    ) {

        // -- Create new trade request
        TradeRequest newRequest = new TradeRequest();
        newRequest.tradeRequestType = TradeRequestType.SELL;
        newRequest.commodityId = comId;
        newRequest.quantity = qty;
        newRequest.price = price;
        newRequest.address = "laura's client";
        newRequest.ownerId = 60;

        // -- Save request to db
        tradeRequestRepository.save(newRequest);

        // -- Run Matcher
        matcher.findMatch(newRequest.id);

        return "Trade request added: SELL " + qty + " @ " + price + " of " + comId + " from " + request.getRemoteAddr() + " + " + request.getRemotePort();
    }
}
