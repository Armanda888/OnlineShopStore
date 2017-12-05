package com.theBeautiful.cassandra.model;

import com.datastax.driver.mapping.annotations.Field;
import com.datastax.driver.mapping.annotations.UDT;
import com.theBeautiful.model.Price;

/**
 * Created by jiaoli on 10/8/17
 */

@UDT(keyspace = "bundles", name = "Price_Type")
public class PriceType {

    @Field(name = "price")
    private float price;

    @Field(name = "salePrice")
    private float salePrice;

    public PriceType(Price priceObj) {
        this.price = priceObj.getPrice();
        this.salePrice = priceObj.getSalePrice();
    }

    public Price generate() {
        Price priceObj = new Price();
        priceObj.setPrice(this.price);
        priceObj.setSalePrice(this.salePrice);
        return priceObj;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(float salePrice) {
        this.salePrice = salePrice;
    }
}
