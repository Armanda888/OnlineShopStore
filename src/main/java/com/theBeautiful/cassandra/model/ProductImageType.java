package com.theBeautiful.cassandra.model;

import com.datastax.driver.mapping.annotations.Field;
import com.datastax.driver.mapping.annotations.UDT;
import com.theBeautiful.model.ProductImage;

/**
 * Created by jiaoli on 10/29/17
 */

@UDT(keyspace = "bundles", name = "Product_Image_Type")
public class ProductImageType {
    @Field(name = "id")
    private String id;

    @Field(name = "description")
    private String description;

    @Field(name = "productImage")
    private String productImage;

    @Field(name = "defaultImage")
    private Boolean defaultImage;

    @Field(name = "imageType")
    private String imageType;

    @Field(name = "imageUrl")
    private String imageUrl;

    @Field(name = "imageCrop")
    private Boolean imageCrop;

    public ProductImageType() {
    }

    public ProductImageType(ProductImage productImage) {
        this.id = productImage.getId();
        if (productImage.getDescription() != null) {
            this.description = productImage.getDescription();
        }
        if (productImage.getProductImage() != null) {
            this.productImage = productImage.getProductImage();
        }
        this.defaultImage = productImage.getDefaultImage();
        if (productImage.getImageType() != null) {
            this.imageType = productImage.getImageType().toString();
        }
        if (productImage.getImageUrl() != null) {
            this.imageUrl = productImage.getImageUrl();
        }
        this.imageCrop = productImage.getImageCrop();
    }

    public ProductImage generate() {
        ProductImage productImage = new ProductImage();
        productImage.setId(this.id);
        productImage.setDescription(this.description);
        productImage.setProductImage(this.productImage);
        productImage.setDefaultImage(this.defaultImage);
        productImage.setImageType(ProductImage.ImageType.valueOf(this.imageType));
        productImage.setImageUrl(this.imageUrl);
        productImage.setImageCrop(this.imageCrop);
        return productImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public Boolean getDefaultImage() {
        return defaultImage;
    }

    public void setDefaultImage(Boolean defaultImage) {
        this.defaultImage = defaultImage;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getImageCrop() {
        return imageCrop;
    }

    public void setImageCrop(Boolean imageCrop) {
        this.imageCrop = imageCrop;
    }
}
