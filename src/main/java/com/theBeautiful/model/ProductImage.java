package com.theBeautiful.model;

/**
 * Created by jiaoli on 10/29/17
 */
public class ProductImage {
    private String id;

    /*description of the product image, or alt name*/
    private String description;

    private String productImage;

    /* whether this image the default image */
    private Boolean defaultImage;

    /*whether it's from system or url*/
    private ImageType imageType = ImageType.LOCAL;

    /*location to get the image*/
    private String imageUrl;

    /*if the image is cropped or not*/
    private Boolean imageCrop;

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

    public ImageType getImageType() {
        return imageType;
    }

    public void setImageType(ImageType imageType) {
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

    public enum ImageType {
        LOCAL,
        WEB
    }
}
