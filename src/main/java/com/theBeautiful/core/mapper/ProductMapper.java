package com.theBeautiful.core.mapper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Floats;
import com.theBeautiful.cassandra.dao.ProductDao;
import com.theBeautiful.core.rest.ProductResource;
import com.theBeautiful.model.Price;
import com.theBeautiful.model.Product;
import com.theBeautiful.model.ProductImage;
import com.theBeautiful.model.api.v1.*;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiaoli on 11/30/17
 */
public class ProductMapper {
    public Product map(ProductRest productRest) {
        if (productRest == null || productRest.getData() == null) {
            return null;
        }
        Product product = new Product();
        ProductAttributesRest attributesRest = productRest.getData().getAttributes();
        product.setId(productRest.getData().getId());
        product.setName(attributesRest.getName());
        product.setDescription(attributesRest.getDescription());
        product.setCategory(attributesRest.getCategory());
        if (attributesRest.getSizes() != null && !attributesRest.getSizes().isEmpty()) {
            product.addSizes(attributesRest.getSizes());
        }
        if (attributesRest.getTextures() != null && !attributesRest.getTextures().isEmpty()) {
            product.addTextures(attributesRest.getTextures());
        }
        if (attributesRest.getProductImages() != null && !attributesRest.getProductImages().isEmpty()) {
            List<ProductImage> productImageList = Lists.newArrayList();
            for (ProductImageRest imageRest : attributesRest.getProductImages()) {
                ProductImage productImage = new ProductImage();
                productImage.setImageUrl(imageRest.getImageUrl());
                productImage.setSmallImageUrl(imageRest.getSmallImageUrl());
                if (imageRest.getImageType() != null) {
                    productImage.setImageType(ProductImage.ImageType.valueOf(imageRest.getImageType().name()));
                }

                productImageList.add(productImage);
            }
            product.setProductImages(productImageList);
        }

        if (attributesRest.getPrices() != null && attributesRest.getPrices().getAdditionalProperties() != null) {
            Map<String, Price> prices = Maps.newHashMap();
            for (Map.Entry<String, Object> entry : attributesRest.getPrices().getAdditionalProperties().entrySet()) {
                Price price = new Price();
                LinkedHashMap priceList = (LinkedHashMap) entry.getValue();
                price.setPrice(Double.valueOf(priceList.get("originPrice").toString()));
                price.setSalePrice(Double.valueOf(priceList.get("salePrice").toString()));
                prices.put(entry.getKey(), price);
            }
            product.setPrices(prices);
        }


        return product;
    }

    public ProductDataRest mapData(Product domain) {
        ProductAttributesRest attributesRest = new ProductAttributesRest();
        attributesRest.setName(domain.getName());
        attributesRest.setDescription(domain.getDescription());
        attributesRest.setCategory(domain.getCategory());

        if (domain.getPrices() != null) {
            PricesRest pricesRest = new PricesRest();
            for (Map.Entry<String, Price> entry : domain.getPrices().entrySet()) {
                PriceRest priceRest = new PriceRest();
                priceRest.setOriginPrice(entry.getValue().getPrice());
                priceRest.setSalePrice(entry.getValue().getSalePrice());
                pricesRest.setAdditionalProperty(entry.getKey(), priceRest);
            }
            attributesRest.setPrices(pricesRest);
        }

        if (domain.getProductImages() != null) {
            List<ProductImageRest> imageRestList = Lists.newArrayList();
            for (ProductImage image : domain.getProductImages()) {
                ProductImageRest imageRest = new ProductImageRest();
                imageRest.setImageUrl(image.getImageUrl());
                imageRest.setSmallImageUrl(image.getSmallImageUrl());
                if (image.getImageType() != null) {
                    imageRest.setImageType(ProductImageRest.ImageType.valueOf(image.getImageType().name()));
                }
                imageRestList.add(imageRest);
            }
            attributesRest.setProductImages(imageRestList);
        }
        if (domain.getSizes() != null && !domain.getSizes().isEmpty()) {
            attributesRest.setSizes(Lists.newArrayList(domain.getSizes()));
        }
        if (domain.getTextures() != null && !domain.getTextures().isEmpty()) {
            attributesRest.setTextures(Lists.newArrayList(domain.getTextures()));
        }
        ProductDataRest productDataRest = new ProductDataRest();
        productDataRest.setAttributes(attributesRest);

        productDataRest.setId(domain.getId());
        productDataRest.setType(ProductDataRest.Type.PRODUCTS);
        return productDataRest;
    }

    public ProductRest map(Product domain) {
        ProductRest productRest = new ProductRest();
        productRest.setData(mapData(domain));
        return productRest;
    }

    public ProductListRest mapList(List<Product> products) {
        ProductListRest productList = new ProductListRest();
        if (products == null || products.isEmpty()) {
            return productList;
        }
        List productData = Lists.newArrayList();
        for (Product product : products) {
            productData.add(mapData(product));
        }
        productList.setData(productData);
        return productList;
    }
}
