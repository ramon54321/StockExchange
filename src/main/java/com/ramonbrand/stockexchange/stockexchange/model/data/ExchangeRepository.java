package com.ramonbrand.stockexchange.stockexchange.model.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ExchangeRepository extends CrudRepository<Exchange, Long> {

    @Query("select sum(e.id), e.price from Exchange e group by e.price")
    List<Object[]> test();

}
