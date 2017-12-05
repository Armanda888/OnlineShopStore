package com.theBeautiful;

import com.theBeautiful.cassandra.util.CassandraConnectorImpl;

/**
 * Hello world!
 * testing connection.
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        CassandraConnectorImpl client = new CassandraConnectorImpl();
        try {
            client.connect();
            //Session session = client.getSession();
            //KeyspaceRepository schemaRepository = new KeyspaceRepository(session);
            client.createKeyspace("test", "SimpleStrategy", 1);
        } catch (Exception e) {

        }
    }
}
