package com.theBeautiful.cassandra.model;

import com.datastax.driver.mapping.annotations.Frozen;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.google.common.collect.Lists;
import com.theBeautiful.model.Order;
import com.theBeautiful.model.OrderStatus;
import com.theBeautiful.model.ShoppingCartItem;

import java.util.Date;
import java.util.List;

@Table(keyspace = "bundles", name = "Orders")
public class OrderEntity implements DBEntityInterface<Order>{

    @PartitionKey
    private String id;

    /* id of the products for this order */
    @Frozen("list<frozen<ShoppingCartItemType>>")
    private List<ShoppingCartItemType> items;

    /* id of the user */
    private String userId;

    private String address;

    private String phone;

    private Date orderDate;

    private float deliverFee;

    private float payableFee;

    private float totalPrice;

    private float taxFee;

    private String trackingNum;

    private String employeeName;

    private String status;

    private String shippingMethod;

    private String deliverStatus;

    public OrderEntity() {
    }

    public OrderEntity(Order order, String partitionId, String partitionType) {
        this.id = order.getId();
        this.userId = order.getUserId();
        if (order.getItems() != null && !order.getItems().isEmpty()) {
            this.items = Lists.newArrayList();
            for (ShoppingCartItem item : order.getItems()) {
                this.items.add(new ShoppingCartItemType(item));
            }
        }
        this.address = order.getAddress();
        this.phone = order.getPhone();
        this.orderDate = order.getOrderDate();
        this.deliverFee = order.getDeliverFee();
        this.payableFee = order.getPayableFee();
        this.totalPrice = order.getTotalPrice();
        this.taxFee = order.getTaxFee();
        if (order.getTrackingNum() != null) {
            this.trackingNum = order.getTrackingNum();
        }
        if (order.getShippingMethod() != null) {
            this.shippingMethod = String.valueOf(order.getShippingMethod());
        }
        this.employeeName = order.getEmployeeName();
        if (order.getDeliverStatus() != null) {
            this.deliverStatus = String.valueOf(order.getDeliverStatus());
        }

        if (order.getStatus() != null) {
            this.status = order.getStatus().toString();
        }
    }

    @Override
    public Order generate() {
        Order order = new Order();
        order.setId(this.id);
        if (this.items != null && !this.items.isEmpty()) {
            List<ShoppingCartItem> shoppingCartItems = Lists.newArrayList();
            for (ShoppingCartItemType item : this.items) {
                shoppingCartItems.add(item.generate());
            }
            order.setItems(shoppingCartItems);
        }
        order.setAddress(this.address);
        order.setPhone(this.phone);
        order.setUserId(this.userId);
        order.setOrderDate(this.orderDate);
        order.setDeliverFee(this.deliverFee);
        order.setPayableFee(this.payableFee);
        order.setTotalPrice(this.totalPrice);
        order.setTaxFee(this.taxFee);
        if (this.trackingNum != null) {
            order.setTrackingNum(this.trackingNum);
        }
        if (this.employeeName != null) {
            order.setEmployeeName(this.employeeName);
        }
        if (this.status != null) {
            order.setStatus(OrderStatus.valueOf(this.status));
        }
        if (this.shippingMethod != null) {
            order.setShippingMethod(Order.ShippingMethod.valueOf(this.shippingMethod));
        }
        if (this.deliverStatus != null) {
            order.setDeliverStatus(Order.DeliveryStatus.valueOf(this.deliverStatus));
        }

        return order;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public List<ShoppingCartItemType> getItems() {
        return items;
    }

    public void setItems(List<ShoppingCartItemType> items) {
        this.items = items;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public float getDeliverFee() {
        return deliverFee;
    }

    public void setDeliverFee(float deliverFee) {
        this.deliverFee = deliverFee;
    }

    public float getPayableFee() {
        return payableFee;
    }

    public void setPayableFee(float payableFee) {
        this.payableFee = payableFee;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public float getTaxFee() {
        return taxFee;
    }

    public void setTaxFee(float taxFee) {
        this.taxFee = taxFee;
    }

    public String getTrackingNum() {
        return trackingNum;
    }

    public void setTrackingNum(String trackingNum) {
        this.trackingNum = trackingNum;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public String getDeliverStatus() {
        return deliverStatus;
    }

    public void setDeliverStatus(String deliverStatus) {
        this.deliverStatus = deliverStatus;
    }
}
