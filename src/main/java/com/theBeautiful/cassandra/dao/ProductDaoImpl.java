package com.theBeautiful.cassandra.dao;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.theBeautiful.cassandra.BundleSchema;
import com.theBeautiful.cassandra.model.PriceType;
import com.theBeautiful.cassandra.model.ProductEntity;
import com.theBeautiful.cassandra.model.ProductImageType;
import com.theBeautiful.cassandra.util.CassandraConnectorInterface;
import com.theBeautiful.cassandra.util.JiamiString;
import com.theBeautiful.config.BundleServices;
import com.theBeautiful.model.Price;
import com.theBeautiful.model.Product;
import com.theBeautiful.model.ProductImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by jiaoli on 10/10/17
 */
public class ProductDaoImpl implements ProductDao {
    private final static Logger LOG = LoggerFactory.getLogger(ProductDao.class);


    private ProductAccessor getAccessor() {
        return BundleServices.getCassandraInterface().getAccessor(ProductAccessor.class);
    }

    private CassandraConnectorInterface getCassandraInterface() {
        return BundleServices.getCassandraInterface();
    }

    private Mapper<ProductEntity> getEntityMapper() {
        return BundleServices.getCassandraInterface().getMapper(ProductEntity.class);
    }

    @Override
    public void upsertProduct(Product product) {
        Map<String, PriceType> prices = Maps.newHashMap();

        if (product.getPrices() != null) {
            for (Map.Entry<String, Price> entry : product.getPrices().entrySet()) {
                prices.put(entry.getKey(), new PriceType(entry.getValue()));
            }
        }
        Statement stmt = null;
        Product existingProduct = null;
        if (product.getId() != null) {
            existingProduct = getById(product.getId());
        }
        boolean updateFlag = false;
        /*
        * If insert to table for the first time, acceptable fields are id, name, description, category, sizes
        * textures, colors, prices.
        *
        * if update the product, will do a patch. only update the value in the new product object
         * can update name, description, category, onSale
        * */
        if (existingProduct == null) {
            List<ProductImageType> productImageTypes = Lists.newArrayList();

            if (product.getProductImages() != null) {
                for (ProductImage image : product.getProductImages()) {
                    productImageTypes.add(new ProductImageType(image));
                }
            }
            stmt = getAccessor().upsert(product.getId(), product.getName(), product.getDescription(),
                    product.getCategory(), product.getSizes(), product.getTextures(), product.getColors(), prices, productImageTypes);
        } else {
            updateFlag = true;
            String name = existingProduct.getName();
            String description = existingProduct.getDescription();
            String category = existingProduct.getCategory();
            Boolean onSale = existingProduct.getOnSale();
            Set<Integer> sizes = existingProduct.getSizes();
            Set<String> colors = existingProduct.getColors();
            Set<String> textures = existingProduct.getTextures();
            Map<String, Price> existingPrices = existingProduct.getPrices();
            if (name == null || !name.equals(product.getName())) {
                name = product.getName();
            }
            if (description == null || !description.equals(product.getDescription())) {
                description = product.getDescription();
            }
            if (category == null || !category.equals(product.getCategory())) {
                category = product.getCategory();
            }
            if (onSale == null || onSale != product.getOnSale()) {
                onSale = product.getOnSale();
            }
            if (sizes == null || sizes != product.getSizes()) {
                sizes = product.getSizes();
            }
            if (colors == null || colors != product.getColors()) {
                colors = product.getColors();
            }
            if (textures == null || textures != product.getTextures()) {
                textures = product.getTextures();
            }
            stmt = getAccessor().updateProduct(product.getId(), name, description, category, onSale, sizes, colors, textures, prices);
        }
        getCassandraInterface().execute(stmt);
        LOG.info("Product with id " + product.getId() + " and name " + product.getName() + " has been " + (updateFlag ? "Updated" : "Inserted"));
    }

    @Override
    public Product getById(String productId) {
        Statement stmt = getAccessor().getById(productId);
        ResultSet result = getCassandraInterface().execute(stmt);
        ProductEntity productEntity = null;
        if (result == null) {
            return null;
        } else {

            Result<ProductEntity> productEntities = getEntityMapper().map(result);
            if (productEntities != null) {
                productEntity = productEntities.one();
            }
        }
        return productEntity == null ? null : productEntity.generate();
    }

    @Override
    public List<Product> getByCategory(String category) {
        Statement stmt = getAccessor().getByCategory(category);
        ResultSet resultSet = getCassandraInterface().execute(stmt);
        if (resultSet == null) {
            return null;
        }
        List<Product> products = Lists.newArrayList();
        Result<ProductEntity> results = getEntityMapper().map(resultSet);
        if (results != null) {
            Result<ProductEntity> productEntities = getEntityMapper().map(resultSet);
            for (ProductEntity entity : productEntities) {
                products.add(entity.generate());
            }
        }
        return products;
    }
/**
    @Override
    public Product addSize(Product product, Set<Integer> size) {
        Statement stmt = getAccessor().addSize(product.getId(), size);
        getCassandraInterface().execute(stmt);
        Set<Integer> sizes = product.getSizes();
        sizes.addAll(size);
        product.setSizes(sizes);
        LOG.info("Added size " + size + " to product " + product.getId());
        return product;
    }

    @Override
    public Product removeSize(Product product, Set<Integer> size) {
        Set<Integer> sizes = product.getSizes();
        if (!sizes.contains(size)) {
            return product;
        }
        Statement stmt = getAccessor().removeSize(product.getId(), size);
        getCassandraInterface().execute(stmt);
        sizes.removeAll(size);
        product.setSizes(sizes);
        LOG.info("Removed size " + size + " from product " + product.getId());
        return product;
    }

    @Override
    public Product addColor(Product product, Set<String> color) {
        Set<String> colors = product.getColors();
        Statement stmt = getAccessor().addColor(product.getId(), color);
        getCassandraInterface().execute(stmt);
        colors.addAll(color);
        product.setColors(colors);
        LOG.info("Added color " + color + " from product " + product.getId());
        return product;
    }

    @Override
    public Product removeColor(Product product, Set<String> color) {
        Set<String> colors = product.getColors();
        if (!colors.contains(color)) {
            return product;
        }
        Statement stmt = getAccessor().removeColor(product.getId(), color);
        getCassandraInterface().execute(stmt);
        colors.removeAll(color);
        product.setColors(colors);
        LOG.info("Removed color " + color + " from product " + product.getId());
        return product;
    }

    @Override
    public Product addTexture(Product product, Set<String> texture) {
        Set<String> textures = product.getTextures();
        Statement stmt = getAccessor().addColor(product.getId(), texture);
        getCassandraInterface().execute(stmt);
        textures.addAll(texture);
        product.setTextures(textures);
        LOG.info("Added texture " + texture + " from product " + product.getId());
        return product;
    }

    @Override
    public Product removeTexture(Product product, Set<String> texture) {
        Set<String> textures = product.getTextures();
        if (!textures.contains(texture)) {
            return product;
        }
        Statement stmt = getAccessor().removeTexture(product.getId(), texture);
        getCassandraInterface().execute(stmt);
        textures.removeAll(texture);
        product.setTextures(textures);
        LOG.info("Removed texture " + texture + " from product " + product.getId());
        return product;
    }
    **/

    //TODO if we need to update the whole sizes/colors/textures
    public void updateSize(Product product, Set<Integer> sizes) {
        Statement stmt = getAccessor().updateSizeOfProduct(product.getId(), sizes);
        getCassandraInterface().execute(stmt);
        LOG.info("Updated product with id " + product.getId() + " with new size " + sizes);
    }


    public void updateColor(Product product, Set<String> colors) {
        Statement stmt = getAccessor().updateColorOfProduct(product.getId(), colors);
        getCassandraInterface().execute(stmt);
        LOG.info("Updated product with id " + product.getId() + " with new colors " + colors);
    }

    public void updateTexture(Product product, Set<String> textures) {
        Statement stmt = getAccessor().updateTexturesOfProduct(product.getId(), textures);
        getCassandraInterface().execute(stmt);
        LOG.info("Updated product with id " + product.getId() + " with new textures " + textures);
    }

    public void updatePrices(Product product, Map<String, Price> prices) {
        Map<String, PriceType> priceTypeMap = Maps.newHashMap();
        if (prices != null) {
            for (Map.Entry<String, Price> price : prices.entrySet()) {
                priceTypeMap.put(price.getKey(), new PriceType(price.getValue()));
            }
        }
        Statement stmt = getAccessor().updatePricesOfProduct(product.getId(), priceTypeMap);
        getCassandraInterface().execute(stmt);
        LOG.info("Updated product with id " + product.getId() + " with new prices " + priceTypeMap);
    }

    @Override
    public List<Product> getProducts() {
        Statement stmt = getAccessor().getProducts();
        ResultSet resultSet = getCassandraInterface().execute(stmt);
        List products = Lists.newArrayList();
        if (resultSet != null) {
            Result<ProductEntity> productEntities = getEntityMapper().map(resultSet);
            for (ProductEntity entity : productEntities) {
                products.add(entity.generate());
            }
        }
        return products;
    }

    @Override
    public void removeProduct(Product product) {
        Statement stmt = getAccessor().removeById(product.getId());
        getCassandraInterface().execute(stmt);
        return;
    }

    @Accessor
    public interface ProductAccessor {
        @Query("SELECT * FROM " + BundleSchema.KEYSPACE + "." + BundleSchema.PRODUCT_TABLE)
        Statement getProducts();

        @Query("SELECT * FROM " + BundleSchema.KEYSPACE + "." + BundleSchema.PRODUCT_TABLE + " " +
                "WHERE id = :id")
        Statement getById(@Param("id") String id);

        @Query("SELECT * FROM " + BundleSchema.KEYSPACE + "." + BundleSchema.PRODUCT_TABLE + " " +
                "WHERE category = :category AllOW FILTERING")
        Statement getByCategory(@Param("category") String category);

        @Query("INSERT INTO " + BundleSchema.KEYSPACE + "." + BundleSchema.PRODUCT_TABLE + " " +
                "(id, name, description, category, sizes, textures, colors, prices, productImages) VALUES (:id, :name, :description, :category, :sizes, :textures, :colors, :prices, :productImages)")
        Statement upsert(@Param("id") String productId,
                         @Param("name") String name,
                         @Param("description") String description,
                         @Param("category") String category,
                         @Param("sizes") Set<Integer> sizes,
                         @Param("textures") Set<String> textures,
                         @Param("colors") Set<String> colors,
                         @Param("prices") Map<String, PriceType> prices,
                         @Param("productImages")List<ProductImageType> productImages);

        @Query("UPDATE " + BundleSchema.KEYSPACE + "." + BundleSchema.PRODUCT_TABLE + " " +
                "SET name = :name, description = :description, category = :category, onSale = :onSale, sizes = :sizes, colors = :colors, textures = :textures, prices = :prices WHERE id = :id")
        Statement updateProduct(@Param("id") String productId,
                                @Param("name") String name,
                                @Param("description") String description,
                                @Param("category") String category,
                                @Param("onSale") Boolean onSale,
                                @Param("sizes") Set<Integer> sizes,
                                @Param("textures") Set<String> textures,
                                @Param("colors") Set<String> colors,
                                @Param("prices") Map<String, PriceType> prices);

        @Query("UPDATE " + BundleSchema.KEYSPACE + "." + BundleSchema.PRODUCT_TABLE + " " +
                "SET sizes = :sizes WHERE id = :id")
        Statement updateSizeOfProduct(@Param("id") String productId,
                                @Param("sizes") Set<Integer> sizes);

        @Query("UPDATE " + BundleSchema.KEYSPACE + "." + BundleSchema.PRODUCT_TABLE + " " +
                "SET colors = :colors WHERE id = :id")
        Statement updateColorOfProduct(@Param("id") String productId,
                                      @Param("colors") Set<String> colors);

        @Query("UPDATE " + BundleSchema.KEYSPACE + "." + BundleSchema.PRODUCT_TABLE + " " +
                "SET textures = :textures WHERE id = :id")
        Statement updateTexturesOfProduct(@Param("id") String productId,
                                       @Param("textures") Set<String> textures);

        @Query("UPDATE " + BundleSchema.KEYSPACE + "." + BundleSchema.PRODUCT_TABLE + " " +
                "SET prices = :prices WHERE id = :id")
        Statement updatePricesOfProduct(@Param("id") String productId,
                                          @Param("prices") Map<String, PriceType> prices);

        @Query("DELETE FROM " + BundleSchema.KEYSPACE + "." + BundleSchema.PRODUCT_TABLE + " " +
                "WHERE id = :id")
        Statement removeById(@Param("id") String productId);
    }
}
