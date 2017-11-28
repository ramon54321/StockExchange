package com.ramonbrand.stockexchange.stockexchange.model.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Individual {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

}