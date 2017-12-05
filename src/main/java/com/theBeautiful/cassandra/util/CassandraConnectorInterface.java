package com.theBeautiful.cassandra.util;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import com.datastax.driver.mapping.Mapper;
import com.theBeautiful.cassandra.DaoSchema;
import com.theBeautiful.cassandra.exceptions.CassandraException;

/**
 * Created by jiaoli on 10/6/17
 */
public interface CassandraConnectorInterface {

    void connect() throws CassandraException;
    <T>T getAccessor(Class<T> className);
    <T>Mapper<T> getMapper(Class<T> className);
    void initDBSchema(DaoSchema daoSchema) throws Exception ;
    ResultSet execute(Statement statement);

}
