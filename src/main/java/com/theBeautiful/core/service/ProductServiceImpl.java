package com.theBeautiful.core.service;

import com.google.inject.Inject;
import com.theBeautiful.cassandra.dao.ProductDao;
import com.theBeautiful.cassandra.util.JiamiString;
import com.theBeautiful.model.Product;

import java.util.List;

/**
 * Created by jiaoli on 10/15/17
 */
public class ProductServiceImpl implements ProductService {
    private final ProductDao productDao;

    @Inject
    public ProductServiceImpl(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List getByCategory(String category) {
        if (category != null) {
            return this.productDao.getByCategory(category);
        }
        return null;
    }

    @Override
    public String addProduct(Product product) {
        if (product.getId() == null) {
            product.setId(JiamiString.generateId());
        }
        if (product != null) {
            this.productDao.upsertProduct(product);
        }
        return product.getId();
    }

    @Override
    public void updateProduct(Product product) {
        if (product != null) {
            this.productDao.upsertProduct(product);
        }
    }

    @Override
    public Product getById(String productId) {
        Product product = null;
        if (productId != null) {
            product = this.productDao.getById(productId);
        }
        return product;
    }

    @Override
    public List<Product> getProducts() {
        return this.productDao.getProducts();
    }

    @Override
    public void deleteProduct(Product product) {
        if (product != null && product.getId() != null) {
            this.productDao.removeProduct(product);
        }
    }
}
