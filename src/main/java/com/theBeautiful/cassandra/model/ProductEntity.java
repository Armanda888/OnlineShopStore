package com.theBeautiful.cassandra.model;

import com.datastax.driver.mapping.annotations.Frozen;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.theBeautiful.model.Price;
import com.theBeautiful.model.Product;
import com.theBeautiful.model.ProductImage;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Table(keyspace = "bundles", name = "Product")
public class ProductEntity implements DBEntityInterface<Product>{
    @PartitionKey
    private String id;

    private String name;

    private String description;

    private String category;

    private Set<Integer> sizes;

    private Set<String> textures;

    private Set<String> colors;

    private Boolean onSale;

    @Frozen("Map<String, Frozen<PriceType>>")
    private Map<String, PriceType> prices;

    private String priceRule;

    @Frozen("List<Frozen<ProductImageType>>")
    private List<ProductImageType> productImages;

    private Map<String, String> reviews;

    private Integer saleNum;

    private Integer stockNum;

    public ProductEntity() {
    }

    public ProductEntity(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.category = product.getCategory();
        this.sizes = product.getSizes();
        this.textures = product.getTextures();
        this.colors = product.getColors();
        this.onSale = product.getOnSale();
        if (product.getPrices() != null) {
            if (this.getPrices() == null) {
                this.prices = Maps.newHashMap();
            }
            for (Map.Entry<String, Price> entry : product.getPrices().entrySet()) {
                this.prices.put(entry.getKey(), new PriceType(entry.getValue()));
            }
        }
        if (product.getProductImages() != null && !product.getProductImages().isEmpty()) {
            this.productImages = Lists.newArrayList();
            for (ProductImage image : product.getProductImages()) {
                this.productImages.add(new ProductImageType(image));
            }
        }
        this.reviews = product.getReviews();
        this.saleNum = product.getSaleNum();
        this.stockNum = product.getStockNum();
    }

    @Override
    public Product generate() {
        Product product = new Product();
        product.setId(this.id);
        product.setName(this.name);
        product.setDescription(this.description);
        product.setCategory(this.category);
        product.setSizes(this.sizes);
        product.setTextures(this.textures);
        product.setColors(this.colors);
        product.setOnSale(this.onSale);
        if (this.prices != null) {
            Map<String, Price> priceMap = Maps.newHashMap();
            for (Map.Entry<String, PriceType> entry : this.prices.entrySet()) {
                priceMap.put(entry.getKey(), entry.getValue().generate());
            }
            product.setPrices(priceMap);
        }
        if (this.productImages != null) {
            List productImagesList = Lists.newArrayList();
            for (ProductImageType imageType : this.productImages) {
                productImagesList.add(imageType.generate());
            }
            product.setProductImages(productImagesList);
        }
        product.setReviews(this.reviews);
        product.setSaleNum(this.saleNum);
        product.setStockNum(this.stockNum);

        return product;
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

    public Map<String, PriceType> getPrices() {
        return prices;
    }

    public void setPrices(Map<String, PriceType> prices) {
        this.prices = prices;
    }

    public List<ProductImageType> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImageType> productImages) {
        this.productImages = productImages;
    }

    public Map<String, String> getReviews() {
        return reviews;
    }

    public void setReviews(Map<String, String> reviews) {
        this.reviews = reviews;
    }

    public Set<Integer> getSizes() {
        return sizes;
    }

    public void setSizes(Set<Integer> sizes) {
        this.sizes = sizes;
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

    public String getPriceRule() {
        return priceRule;
    }

    public void setPriceRule(String priceRule) {
        this.priceRule = priceRule;
    }
}
