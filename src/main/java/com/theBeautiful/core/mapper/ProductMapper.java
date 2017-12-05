package com.theBeautiful.core.mapper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.theBeautiful.cassandra.dao.ProductDao;
import com.theBeautiful.core.rest.ProductResource;
import com.theBeautiful.model.Price;
import com.theBeautiful.model.Product;
import com.theBeautiful.model.ProductImage;
import com.theBeautiful.model.api.v1.*;

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
        if (attributesRest.getProductImages() != null && !attributesRest.getProductImages().isEmpty()) {
            List<ProductImage> productImageList = Lists.newArrayList();
            for (String image : attributesRest.getProductImages()) {
                ProductImage productImage = new ProductImage();
                productImage.setImageUrl(image);
                productImageList.add(productImage);
            }
            product.setProductImages(productImageList);
        }
        if (attributesRest.getPrices() != null) {
            Map<String, Price> prices = Maps.newHashMap();
            for (Map.Entry<String, Object> entry : attributesRest.getPrices().getAdditionalProperties().entrySet()) {
                Price price = new Price();
                PriceRest priceRest = (PriceRest) entry.getValue();
                price.setPrice(priceRest.getPrice().floatValue());
                price.setSalePrice(priceRest.getSalePrice().floatValue());
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

        if (domain.getPrices() != null) {
            PricesRest pricesRest = new PricesRest();
            for (Map.Entry<String, Price> entry : domain.getPrices().entrySet()) {
                PriceRest priceRest = new PriceRest();
                priceRest.setPrice((double) entry.getValue().getPrice());
                priceRest.setSalePrice((double) entry.getValue().getSalePrice());
                pricesRest.setAdditionalProperty(entry.getKey(), priceRest);
            }
            attributesRest.setPrices(pricesRest);
        }
        if (domain.getProductImages() != null) {
            List<String> images = Lists.newArrayList();
            for (ProductImage image : domain.getProductImages()) {
                images.add(image.getImageUrl());
            }
            attributesRest.setProductImages(images);
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
