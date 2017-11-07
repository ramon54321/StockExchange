package com.ramonbrand.stockexchange.stockexchange.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Exchange {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    public long commodityId;
    public long quantity;
    public double price;
    public long ownerIdSell;
    public long ownerIdBuy;

    @Override
    public String toString() {
        return "ID: " + id + " CommodityID: " + commodityId + " Quantity: " + quantity + " Price: " + price + " SellID: " + ownerIdSell + " BuyID: " + ownerIdBuy;
    }
}
