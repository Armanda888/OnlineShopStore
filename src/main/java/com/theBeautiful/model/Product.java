package com.theBeautiful.model;

import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Product {
    private String id;

    private String name;

    private String description;

    private String category;

    private Set<Integer> sizes = Sets.newHashSet();

    private Set<String> textures = Sets.newHashSet();

    private Set<String> colors;

    private Boolean onSale;

    private Map<String, Price> prices;

    //TODO priceRule is to indicate how the key is going to be constructed of when finding the salePrice and originalPrice.
    private PriceRule priceRule;

    private List<ProductImage> productImages;

    private Map<String, String> reviews;

    private Integer saleNum;

    private Integer stockNum;

    public Product() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Set<Integer> getSizes() {
        return sizes;
    }

    public void setSizes(Set<Integer> sizes) {
        this.sizes = sizes;
    }

    public Set<String> getTextures() {
        return textures;
    }

    public void setTextures(Set<String> textures) {
        this.textures = textures;
    }

    public Set<String> getColors() {
        return colors;
    }

    public void setColors(Set<String> colors) {
        this.colors = colors;
    }

    public Boolean getOnSale() {
        return onSale;
    }

    public void setOnSale(Boolean onSale) {
        this.onSale = onSale;
    }

    public Map<String, Price> getPrices() {
        return prices;
    }

    public void setPrices(Map<String, Price> prices) {
        this.prices = prices;
    }

    public List<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }

    public Map<String, String> getReviews() {
        return reviews;
    }

    public void setReviews(Map<String, String> reviews) {
        this.reviews = reviews;
    }

    public Integer getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(Integer saleNum) {
        this.saleNum = saleNum;
    }

    public Integer getStockNum() {
        return stockNum;
    }

    public void setStockNum(Integer stockNum) {
        this.stockNum = stockNum;
    }

    public PriceRule getPriceRule() {
        return priceRule;
    }

    public void setPriceRule(PriceRule priceRule) {
        this.priceRule = priceRule;
    }

    public void addSizes(Collection<Integer> sizesColllection) {
        this.sizes.addAll(sizesColllection);
    }

    public void addTextures(Collection<String> texturesCollection) {
        this.textures.addAll(texturesCollection);
    }

    protected enum PriceRule {
        Style,
        Size,
        Type,
        Style_Size,
        Type_Size
    }
}
