package com.ramonbrand.stockexchange.stockexchange.controller;

import com.ramonbrand.stockexchange.stockexchange.model.Matcher;
import com.ramonbrand.stockexchange.stockexchange.model.TokenVerification;
import com.ramonbrand.stockexchange.stockexchange.model.data.*;
import jdk.nashorn.internal.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
            @PathVariable("qty") long qty,
            @RequestHeader(value="apiToken") String token
    ) {
        // -- Verify token
        if(!TokenVerification.verifyToken(token))
            return "invalid token";

        // -- Create new trade request
        TradeRequest newRequest = new TradeRequest();
        newRequest.tradeRequestType = TradeRequestType.BUY;
        newRequest.commodityId = comId;
        newRequest.quantity = qty;
        newRequest.price = price;
        newRequest.address = "x";
        newRequest.ownerId = TokenVerification.getTokenUserId(token);

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
            @PathVariable("qty") long qty,
            @RequestHeader(value="apiToken") String token
    ) {
        // -- Verify token
        if(!TokenVerification.verifyToken(token))
            return "invalid token";

        // -- Create new trade request
        TradeRequest newRequest = new TradeRequest();
        newRequest.tradeRequestType = TradeRequestType.SELL;
        newRequest.commodityId = comId;
        newRequest.quantity = qty;
        newRequest.price = price;
        newRequest.address = "x";
        newRequest.ownerId = TokenVerification.getTokenUserId(token);;

        // -- Save request to db
        tradeRequestRepository.save(newRequest);

        // -- Run Matcher
        matcher.findMatch(newRequest.id);

        return "Trade request added: SELL " + qty + " @ " + price + " of " + comId + " from " + request.getRemoteAddr() + " + " + request.getRemotePort();
    }
}
