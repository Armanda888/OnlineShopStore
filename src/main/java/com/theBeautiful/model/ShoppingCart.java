package com.theBeautiful.model;

import com.theBeautiful.cassandra.model.ShoppingCartItemType;

import java.util.List;

/**
 * Created by jiaoli on 10/28/17
 */
public class ShoppingCart {

    private String id;

    private List<ShoppingCartItem> items;

    private String address;

    private String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ShoppingCartItem> getItems() {
        return items;
    }

    public void setItems(List<ShoppingCartItem> items) {
        this.items = items;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
