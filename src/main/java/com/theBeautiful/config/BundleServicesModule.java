package com.theBeautiful.config;

import com.google.inject.AbstractModule;
import com.theBeautiful.cassandra.dao.ProductDao;
import com.theBeautiful.cassandra.dao.ProductDaoImpl;
import com.theBeautiful.cassandra.dao.UserDao;
import com.theBeautiful.cassandra.dao.UserDaoImpl;
import com.theBeautiful.cassandra.util.CassandraConnectorImpl;
import com.theBeautiful.cassandra.util.CassandraConnectorInterface;
import com.theBeautiful.core.service.ProductService;
import com.theBeautiful.core.service.ProductServiceImpl;
import com.theBeautiful.core.service.UserService;
import com.theBeautiful.core.service.UserServiceImpl;

/**
 * Created by jiaoli on 10/1/17
 */
public class BundleServicesModule extends AbstractModule {

    protected void configure() {

        bind(UserDao.class).to(UserDaoImpl.class);
        bind(ProductDao.class).to(ProductDaoImpl.class);
        bind(CassandraConnectorInterface.class).to(CassandraConnectorImpl.class);

        bind(UserService.class).to(UserServiceImpl.class);
        bind(ProductService.class).to(ProductServiceImpl.class);

    }
}
