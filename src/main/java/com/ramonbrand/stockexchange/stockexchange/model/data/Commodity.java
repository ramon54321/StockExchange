package com.ramonbrand.stockexchange.stockexchange.model.data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Commodity {
    @Id
    public long id;
    public String name;
    public double value;
}
