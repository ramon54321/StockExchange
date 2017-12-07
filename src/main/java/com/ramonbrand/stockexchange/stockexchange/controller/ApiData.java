package com.ramonbrand.stockexchange.stockexchange.controller;

import com.ramonbrand.stockexchange.stockexchange.model.Matcher;
import com.ramonbrand.stockexchange.stockexchange.model.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class ApiData {

    @Autowired
    private CommodityRepository commodityRepository;
    @Autowired
    private ExchangeRepository exchangeRepository;
    @Autowired
    private TradeRequestRepository tradeRequestRepository;

    @Autowired
    private EntityManagerFactory emf;

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/api/data/commodities", produces = { MediaType.APPLICATION_JSON_VALUE })
    public List<Commodity> apiDataListCommodities(
            HttpServletRequest request
    ) {
        List<Commodity> commodities = new ArrayList<>();
        commodityRepository.findAll().forEach(commodities::add);

        return commodities;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/api/data/commodities/{comId}/{mingap}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public OHLCPojo apiDataListCommoditiesTest(
            HttpServletRequest request,
            @PathVariable("comId") long comId,
            @PathVariable("mingap") long mingap
    ) {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();

        String queryString = "select e1.timeslot, e1.min_p min_price, e1.max_p max_price, e2.price first_price, e3.price last_price " +
                "from (" +
                "select " +
                "date_trunc('hour', ei.date) + date_part('minute', ei.date)\\:\\:int / " + mingap + " * interval '" + mingap + " min' " +
                " timeslot, min(ei.date) min_date, max(ei.date) max_date, max(ei.price) max_p , min(ei.price) min_p " +
                "from exchange ei where ei.commodity_id = " + comId + " group by timeslot " +
                ") e1, exchange e2, exchange e3 " +
                "where e2.date = e1.min_date " +
                "and e3.date = e1.max_date " +
                ";";

        Query query = entityManager.createNativeQuery(queryString);

        List<Object[]> results = query.getResultList();

        entityManager.getTransaction().commit();
        entityManager.close();

        OHLCPojo ohlcPojo = new OHLCPojo();

        for (int i = 0; i < results.size(); i++) {
            Object[] result = results.get(i);

            Timestamp ts = (Timestamp) result[0];
            Date date = new Date();
            date.setTime(ts.getTime());
            String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);

            ohlcPojo.x.add(formattedDate);
            ohlcPojo.low.add((Double) result[1]);
            ohlcPojo.high.add((Double) result[2]);
            ohlcPojo.open.add((Double) result[3]);
            ohlcPojo.close.add((Double) result[4]);
        }
        
        return ohlcPojo;
    }
}
