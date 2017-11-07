package com.ramonbrand.stockexchange.stockexchange.data;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
@Table(name = "Commodities")
public class Commodity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    public String name;
    public double value;
    public ArrayList<Double> testData = new ArrayList<>();
}
