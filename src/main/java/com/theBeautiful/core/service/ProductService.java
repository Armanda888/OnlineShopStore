package com.theBeautiful.core.service;

import com.theBeautiful.model.Product;

import java.util.List;

/**
 * Created by jiaoli on 10/15/17
 */
public interface ProductService {

    String addProduct(Product product);

    void updateProduct(Product product);

    Product getById(String productId);

    List getByCategory(String category);

    List<Product> getProducts();

    void deleteProduct(Product product);
}
