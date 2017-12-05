package com.theBeautiful.cassandra.model;

import com.datastax.driver.mapping.annotations.Frozen;
import com.datastax.driver.mapping.annotations.Table;

import java.util.List;

/**
 * Created by jiaoli on 10/28/17
 */

@Table(keyspace = "bundles", name = "Shopping_Cart")
public class ShoppingCartEntity {
    private String id;

    @Frozen("List<Frozen<ShoppingCartItemType>>")
    private List<ShoppingCartItemType> items;

    private String address;

    private String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ShoppingCartItemType> getItems() {
        return items;
    }

    public void setItems(List<ShoppingCartItemType> items) {
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
