package com.theBeautiful.cassandra.dao;

import com.theBeautiful.model.Price;
import com.theBeautiful.model.Product;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by jiaoli on 10/10/17
 */
public interface ProductDao {
    /*
    * Add or update product
    * */
    void upsertProduct(Product product);

    Product getById(String productId);

    List<Product> getByCategory(String category);

    List<Product> getProducts();
/*
    Product addSize(Product product, Set<Integer> size);

    Product removeSize(Product product, Set<Integer> size);

    Product addColor(Product product, Set<String> color);

    Product removeColor(Product product, Set<String> color);

    Product addTexture(Product product, Set<String> texture);

    Product removeTexture(Product product, Set<String> texture);
*/

    void updateSize(Product product, Set<Integer> sizes);

    void updateColor(Product product, Set<String> colors);

    void updateTexture(Product product, Set<String> textures);

    void updatePrices(Product product, Map<String, Price> prices);


    void removeProduct(Product product);
}
