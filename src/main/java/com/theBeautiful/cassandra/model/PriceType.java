package com.theBeautiful.cassandra.model;

import com.datastax.driver.mapping.annotations.Field;
import com.datastax.driver.mapping.annotations.UDT;
import com.theBeautiful.model.Price;

/**
 * Created by jiaoli on 10/8/17
 */

@UDT(keyspace = "bundles", name = "Price_Type")
public class PriceType {

    /*original price*/
    @Field(name = "originPrice")
    private Double originPrice;

    @Field(name = "salePrice")
    private Double salePrice;

    public PriceType() {
    }

    public PriceType(Price priceObj) {
        this.originPrice = priceObj.getPrice();
        this.salePrice = priceObj.getSalePrice();
    }

    public Price generate() {
        Price priceObj = new Price();
        priceObj.setPrice(this.originPrice);
        priceObj.setSalePrice(this.salePrice);
        return priceObj;
    }

    public Double getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(Double originPrice) {
        this.originPrice = originPrice;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }
}
