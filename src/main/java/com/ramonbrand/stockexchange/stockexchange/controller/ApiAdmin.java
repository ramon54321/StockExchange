package com.ramonbrand.stockexchange.stockexchange.controller;

import com.ramonbrand.stockexchange.stockexchange.model.data.Commodity;
import com.ramonbrand.stockexchange.stockexchange.model.data.CommodityRepository;
import com.ramonbrand.stockexchange.stockexchange.model.data.ExchangeRepository;
import com.ramonbrand.stockexchange.stockexchange.model.data.TradeRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ApiAdmin {

    @Autowired
    private CommodityRepository commodityRepository;
    @Autowired
    private ExchangeRepository exchangeRepository;
    @Autowired
    private TradeRequestRepository tradeRequestRepository;

    @RequestMapping("/api/admin/init")
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

        return "Initialized";
    }
}
