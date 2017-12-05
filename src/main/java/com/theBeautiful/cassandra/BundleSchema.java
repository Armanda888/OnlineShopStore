package com.theBeautiful.cassandra;

import com.datastax.driver.core.TypeCodec;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class BundleSchema extends DaoSchema{

    private static final String VERSION = "1.0";

    public static final String KEYSPACE = "bundles";

    public static final String USER_TABLE = "user";

    public static final String ORDER_TABLE = "order";

    public static final String PRODUCT_TABLE = "product";

    //replication of cassandra db.
    private final int REPLICATION_FACTOR = 1;

    private static final String CREATE_KEYSPACE_TEMPLATE = "CREATE KEYSPACE IF NOT EXISTS " + KEYSPACE + " with replication = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 } AND durable_writes = true";

    private static final String SCHEMA_PATH = "dao/cassandra/BundleStore.cql";

    public BundleSchema() {
        super(KEYSPACE, VERSION);
    }

    public Collection<TypeCodec<?>> getRequiredCodecs() {
        return null;
    }

    public List<String> getCreateSchema() throws Exception {
        List<String> queryList = Lists.newArrayList();
        try {
            InputStream input = getClass().getClassLoader().getResourceAsStream(SCHEMA_PATH);
            if (input == null) {
                throw new Exception("Couldn't find schema resource file " + SCHEMA_PATH);
            }
            String fileContent = IOUtils.toString(input);
            String[] queries = fileContent.split(";\\s*");
            queryList = Arrays.asList(queries);
        } catch (IOException e) {
            throw new RuntimeException("The database schema definition is missing or corrupted " + SCHEMA_PATH, e);
        }
        List<String> createStatements = new ArrayList<String>(queryList.size() + 1);
        createStatements.add(String.format(CREATE_KEYSPACE_TEMPLATE, REPLICATION_FACTOR));
        createStatements.addAll(queryList);
        return createStatements;
    }
}
