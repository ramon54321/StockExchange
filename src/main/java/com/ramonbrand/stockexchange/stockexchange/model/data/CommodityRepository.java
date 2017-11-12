package com.ramonbrand.stockexchange.stockexchange.model.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CommodityRepository extends CrudRepository<Commodity, Long> {

    @Query("SELECT c FROM Commodity c WHERE c.name = 'Oil'")
    Commodity getOil();

}
