package com.ramonbrand.stockexchange.stockexchange.model.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeRequestRepository extends CrudRepository<TradeRequest, Long> {

    @Query("SELECT t FROM TradeRequest t WHERE t.tradeRequestType = '1' AND t.price <= ?1")
    List<TradeRequest> getSells(double maxPrice);

    @Query("SELECT t FROM TradeRequest t WHERE t.tradeRequestType = '0' AND t.price >= ?1")
    List<TradeRequest> getBuys(double minPrice);

}
