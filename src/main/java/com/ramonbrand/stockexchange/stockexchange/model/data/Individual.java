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

    public String username;
    public String passwordHash;

    //@ElementCollection
    @ManyToMany
    public List<Exchange> exchanges = new ArrayList<>();

    @ElementCollection
    public Map<Long, Long> commodityQuantities = new HashMap<>();

    public void addCommodities(long commodityId, long qty) {
        long currentValue = 0;
        if(commodityQuantities.containsKey(commodityId)) {
            currentValue = commodityQuantities.get(commodityId);
            commodityQuantities.replace(commodityId, currentValue + qty);
        } else {
            commodityQuantities.put(commodityId, qty);
        }
    }

    public void removeCommodities(long commodityId, long qty) {
        long currentValue = 0;
        if(commodityQuantities.containsKey(commodityId)) {
            currentValue = commodityQuantities.get(commodityId);
            commodityQuantities.replace(commodityId, Math.max(0 ,currentValue - qty));
        }
    }

}