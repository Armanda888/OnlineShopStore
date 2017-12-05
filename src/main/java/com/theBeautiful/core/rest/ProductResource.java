package com.theBeautiful.core.rest;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.theBeautiful.core.mapper.ProductMapper;
import com.theBeautiful.core.service.ProductService;
import com.theBeautiful.model.Product;
import com.theBeautiful.model.api.v1.ProductDataRest;
import com.theBeautiful.model.api.v1.ProductRest;
import com.wordnik.swagger.annotations.ApiParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by jiaoli on 10/10/17
 */
@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource implements RestResource{

    private final Provider<ProductService> productServiceProvider;
    private ProductService productService;
    private final ProductMapper productMapper = new ProductMapper();

    @Inject
    public ProductResource(Provider<ProductService> productServiceProvider) {
        this.productServiceProvider = productServiceProvider;
    }

    private void initializeService() {
        if (this.productService == null) {
            this.productService = this.productServiceProvider.get();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getProducts() {
        initializeService();
        List<Product> products = productService.getProducts();
        return Response.status(Response.Status.OK).entity(productMapper.mapList(products)).build();
    }

    @GET
    @Path("/{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getProducts(@PathParam("productId") String productId) {
        initializeService();
        if (productId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Product product = productService.getById(productId);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(null).build();
        }
        return Response.status(Response.Status.OK).entity(productMapper.map(product)).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response signUp(@ApiParam("product") ProductRest product) {
        if (product == null) {
            Error error = new Error("Invalid product");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
        initializeService();
        Product existingProduct = null;
        if (product.getData().getId() != null) {
            existingProduct = productService.getById(product.getData().getId());
        }
        if (existingProduct != null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Product with the id already exists.").build();
        }
        String productId = productService.addProduct(productMapper.map(product));
        ProductRest productRest = new ProductRest();
        ProductDataRest dataRest = product.getData();
        dataRest.setId(productId);
        productRest.setData(dataRest);
        return Response.status(Response.Status.CREATED).entity(productRest).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateProduct(@ApiParam("product") ProductRest product) {
        if (product == null || product.getData().getId() == null) {
            Error error = new Error("Invalid product");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
        initializeService();
        Product existingProduct = productService.getById(product.getData().getId());
        if (existingProduct == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Product with the id doesn't exists.").build();
        }
        productService.updateProduct(productMapper.map(product));
        return Response.status(Response.Status.NO_CONTENT).entity(product).build();
    }

    //TODO add review, post /products/{productId}/reviews and delete /products/{productId}/reviews/{reviewId}
    @DELETE
    @Path("/{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateProduct(@PathParam("productId") String productId) {
        if (productId == null) {
            Error error = new Error("Invalid product");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
        initializeService();
        Product product = productService.getById(productId);
        if (product == null) {
            return Response.noContent().build();
        }
        productService.deleteProduct(product);
        return Response.accepted().build();
    }

    @Override
    public String getBaseUrl() {
        return getClass().getAnnotation(Path.class).value();
    }

    @Override
    public String getName() {
        return "User Product service";
    }
}
