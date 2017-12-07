package com.ramonbrand.stockexchange.stockexchange.model.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IndividualRepository extends CrudRepository<Individual, Long> {

    Individual findByUsername(String username);

}
