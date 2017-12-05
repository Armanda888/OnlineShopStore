package com.theBeautiful.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.theBeautiful.cassandra.BundleSchema;
import com.theBeautiful.cassandra.util.CassandraConnectorImpl;
import com.theBeautiful.cassandra.util.CassandraConnectorInterface;
import com.theBeautiful.core.rest.ProductResource;
import com.theBeautiful.core.rest.RestResource;
import com.theBeautiful.core.rest.UserResource;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jiaoli on 10/1/17
 */
public class BundleServices {
    private Injector injector;

    private final static CassandraConnectorInterface CASSANDRA_INTERFACE = new CassandraConnectorImpl();

    public BundleServices() {
        initializeInjector();
    }

    public BundleServices(Injector injector) {
        this.injector = injector;
    }

    public void start() throws Exception {
        initializeInjector();
        initDBSchema();
    }

    private void initializeInjector () {
        if (null == this.injector) {
            injector = Guice.createInjector(new BundleServicesModule());
        }
    }

    //TODO to initialize database schema.
    protected void initDBSchema() throws Exception {
        CassandraConnectorInterface cassandraConnectorInterface = getCassandraInterface();
        cassandraConnectorInterface.connect();
        BundleSchema bundleSchema = new BundleSchema();
        cassandraConnectorInterface.initDBSchema(bundleSchema);
    }

    public static CassandraConnectorInterface getCassandraInterface() {
        return CASSANDRA_INTERFACE;
    }

    public List<RestResource> getRestResources() {
        initializeInjector();
        UserResource userResource = injector.getInstance(UserResource.class);
        ProductResource productResource = injector.getInstance(ProductResource.class);
        return Arrays.asList(userResource, productResource);
    }
}
