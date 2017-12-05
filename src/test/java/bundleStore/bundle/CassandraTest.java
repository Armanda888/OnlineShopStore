package bundleStore.bundle;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.theBeautiful.cassandra.exceptions.CassandraException;
import com.theBeautiful.cassandra.util.CassandraConnectorImpl;
import com.theBeautiful.cassandra.KeyspaceRepository;
import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class CassandraTest {
    private KeyspaceRepository schemaRepository;

    private Session session;

    @BeforeClass
    public static void init() throws ConfigurationException, TTransportException, IOException, InterruptedException {
        // Start an embedded Cassandra Server
        EmbeddedCassandraServerHelper.startEmbeddedCassandra(20000L);
    }

    @Before
    public void connect() throws CassandraException {
        CassandraConnectorImpl client = new CassandraConnectorImpl();
        client.connect();
        this.session = client.getSession();
        schemaRepository = new KeyspaceRepository(session);
    }

    @Test
    public void whenCreatingAKeyspace_thenCreated() {
        String keyspaceName = "testBaeldungKeyspace";
        schemaRepository.createKeyspace(keyspaceName, "SimpleStrategy", 1);

        //ResultSet result = session.execute("SELECT * FROM system_schema.keyspaces WHERE keyspace_name = 'testBaeldungKeyspace';");

        ResultSet result = session.execute("SELECT * FROM system_schema.keyspaces;");

        // Check if the Keyspace exists in the returned keyspaces.
        Row row = result.one();
        assertTrue(row != null);
    }

    @Test
    public void whenDeletingAKeyspace_thenDoesNotExist() {
        String keyspaceName = "testBaeldungKeyspace";

        // schemaRepository.createKeyspace(keyspaceName, "SimpleStrategy", 1);
        schemaRepository.deleteKeyspace(keyspaceName);

        ResultSet result = session.execute("SELECT * FROM system_schema.keyspaces;");
        Row row = result.one();
        assertFalse(row == null);
    }

    @AfterClass
    public static void cleanup() {
        EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
    }
}
