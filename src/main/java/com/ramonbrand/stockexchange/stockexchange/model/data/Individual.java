package com.ramonbrand.stockexchange.stockexchange.model.data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Individual {
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @ElementCollection
    @CollectionTable
    public List<Exchange> exchanges = new ArrayList<>();

    //@ElementCollection
    //@CollectionTable
    //public Map<Long, Long> exchanges = new HashMap<>();

}