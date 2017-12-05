package com.theBeautiful.model;

import java.util.Date;
import java.util.List;

public class Order {
    private String id;

    private String userId;

    private List<ShoppingCartItem> items;

    private String address;

    private String phone;

    private Date orderDate;

    /* Delivery fee*/
    private float deliverFee;

    private OrderStatus status;

    /*
    * Price of all the items plus deliver fee.
    * */
    private float payableFee;

    /*
    * total fee after coupon
    * */
    private float totalPrice;

    private float taxFee;

    private String trackingNum;

    /* Employee who handled this order, e.g delivered it, updated its status. */
    private String employeeName;

    private ShippingMethod shippingMethod;

    private DeliveryStatus deliverStatus;

    public Order() {
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public DeliveryStatus getDeliverStatus() {
        return deliverStatus;
    }

    public void setDeliverStatus(DeliveryStatus deliverStatus) {
        this.deliverStatus = deliverStatus;
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

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
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

    public ShippingMethod getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(ShippingMethod shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public enum DeliveryStatus {
        IN_PROGRESS,
        NOT_PROCESSED,
        DONE
    }

    public enum ShippingMethod {
        UPS,
        USPS,
        FEDEX
    }
}
