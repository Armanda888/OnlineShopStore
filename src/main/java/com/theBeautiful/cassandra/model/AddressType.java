package com.theBeautiful.cassandra.model;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.Field;
import com.datastax.driver.mapping.annotations.UDT;
import com.theBeautiful.model.Address;

@UDT(keyspace = "bundles", name = "Address_Type")
public class AddressType {

    @Field(name = "id")
    private String id;

    @Field(name = "address_one")
    private String address1;

    @Field(name = "address_two")
    private String address2;

    @Field(name = "state")
    private String state;

    @Field(name = "city")
    private String city;

    @Field(name = "country")
    private String country;

    @Field(name = "postcode")
    private String postcode;

    public AddressType() {
    }

    public AddressType(Address address) {
        this.id = address.getId();
        this.address1 = address.getAddress1();
        this.address2 = address.getAddress2();
        this.state = address.getState();
        this.city = address.getCity();
        this.country = address.getCountry();
        this.postcode = address.getPostcode();
    }

    public Address generate() {
        Address address = new Address();
        address.setId(this.id);
        address.setAddress1(this.address1);
        address.setAddress2(this.address2);
        address.setState(this.state);
        address.setCity(this.city);
        address.setPostcode(this.postcode);
        address.setCountry(this.country);

        return address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
