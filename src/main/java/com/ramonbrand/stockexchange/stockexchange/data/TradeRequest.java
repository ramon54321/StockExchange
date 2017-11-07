package com.ramonbrand.stockexchange.stockexchange.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class TradeRequest implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    public TradeRequestType tradeRequestType;
    public long commodityId;
    public long quantity;
    public double price;
    public String address;
    public long ownerId;
}
