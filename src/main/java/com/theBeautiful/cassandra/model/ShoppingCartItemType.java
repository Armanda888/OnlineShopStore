package com.theBeautiful.cassandra.model;

import com.datastax.driver.mapping.annotations.Field;
import com.datastax.driver.mapping.annotations.UDT;
import com.theBeautiful.model.ShoppingCartItem;

/**
 * Created by jiaoli on 10/28/17
 */

@UDT(keyspace = "bundles", name = "ShoppingCart_Item_Type")
public class ShoppingCartItemType {
    @Field(name = "id")
    private String id;

    @Field(name = "productId")
    private String productId;

    @Field(name = "quantity")
    private Integer quantity;

    @Field(name = "price")
    private float price;

    @Field(name = "name")
    private String name;

    @Field(name = "salePrice")
    private float salePrice;

    @Field(name = "description")
    private String description;

    public ShoppingCartItemType(ShoppingCartItem shoppingCartItem) {
        this.id = shoppingCartItem.getId();
        this.name = shoppingCartItem.getName();
        this.productId = shoppingCartItem.getProductId();
        this.quantity = shoppingCartItem.getQuantity();
        this.price = shoppingCartItem.getPrice();
        this.salePrice = shoppingCartItem.getSalePrice();
        this.description = shoppingCartItem.getDescription();
    }

    public ShoppingCartItem generate() {
        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        shoppingCartItem.setId(this.id);
        shoppingCartItem.setName(this.name);
        shoppingCartItem.setProductId(this.productId);
        shoppingCartItem.setQuantity(this.quantity);
        shoppingCartItem.setPrice(this.price);
        shoppingCartItem.setSalePrice(this.salePrice);
        shoppingCartItem.setDescription(this.description);
        return shoppingCartItem;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(float salePrice) {
        this.salePrice = salePrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
