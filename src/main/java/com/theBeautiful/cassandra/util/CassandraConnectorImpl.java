package com.theBeautiful.cassandra.util;

import com.datastax.driver.core.*;
import com.datastax.driver.core.exceptions.DriverException;
import com.datastax.driver.core.policies.DefaultRetryPolicy;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.google.common.collect.Maps;
import com.theBeautiful.cassandra.DaoSchema;
import com.theBeautiful.cassandra.exceptions.CassandraException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CassandraConnectorImpl implements CassandraConnectorInterface {

    private static final Logger LOG = LoggerFactory.getLogger(CassandraConnectorImpl.class);
    //everytime we access the session, we need a write lock.
    private final ReadWriteLock sessionLock = new ReentrantReadWriteLock();
    private final Lock cacheLock = new ReentrantLock();

    //local cache
    private Map<Class<?>, Object> accessorCache = Maps.newHashMap();
    private Map<String, Map<ConsistencyLevel, PreparedStatement>> preparedStatementCache = Maps.newHashMap();
    private Map<Class<?>, Mapper<?>> mapperCache = Maps.newHashMap();
    private Cluster cluster;

    private Session session;

    private final String NODE_NAME = "127.0.0.1";
    private final Integer PORT = 9042;
    private final ConsistencyLevel DEFAULT_CONSISTENCY_LEVEL = ConsistencyLevel.LOCAL_ONE;

    private int DEFAULT_RETRY = 5;
    private long DEFAULT_WAIT_MILLISECONDS = 100;
    private int DEFAULT_FETCH_SIZE = 100;

    private MappingManager mappingManager;

    private volatile boolean shutdown = false;

    //TODO need to attempt for a few times?
    public void connect() throws CassandraException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Connecting to Cassandra...");
        }
        if (shutdown) {
            throw new CassandraException("Cassandra has been down.");
        }
        sessionLock.writeLock().lock();
        try {
            Cluster.Builder b = Cluster.builder().addContactPoint(NODE_NAME);
            if (PORT != null) {
                b.withPort(PORT);
            }
            cluster = b.withRetryPolicy(DefaultRetryPolicy.INSTANCE)
                    .withoutJMXReporting()
                    .build();
            Metadata metadata = getMetadata();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Connected to cluster: " + metadata.getClusterName());
            }
            for (Host host : metadata.getAllHosts()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Datacenter: " + host.getDatacenter() +
                            ", Host: " + host.getAddress() +
                            ", Rack: " + host.getRack());
                }
            }
            session = cluster.connect();
            mappingManager = new MappingManager(session);
        } finally {
            sessionLock.writeLock().unlock();
        }
    }

    public Session getSession() {
        return this.session;
    }

    public <T> T getAccessor(Class<T> className) {
        if (session == null) {
            try {
                connect();
            } catch (CassandraException ex) {
                LOG.error("Cassandra connection error ", ex);
            }
        }
        sessionLock.readLock().lock();
        try {
            T found = (T) accessorCache.get(className);
            if (found == null) {
                cacheLock.lock();
                try {
                    found = mappingManager.createAccessor(className);
                    accessorCache.put(className, found);
                } catch (Throwable throwable) {
                    LOG.error("Failed to get accessor for class ", className, throwable);
                    //throw new CassandraException(throwable.getMessage()); TODO how do I handle this cassandra error.
                } finally {
                    cacheLock.unlock();
                }
            }
            return found;
        } finally {
            sessionLock.readLock().unlock();
        }
    }

    public <T> Mapper<T> getMapper(Class<T> className) {
        if (session == null) {
            try {
                connect();
            } catch (CassandraException ex) {
                LOG.error("Cassandra connection error ", ex);
            }
        }
        sessionLock.readLock().lock();
        try {
            Mapper<T> mapper = (Mapper<T>) mapperCache.get(className);
            if (mapper == null) {
                cacheLock.lock();
                try {
                    mapper = mappingManager.mapper(className);
                    mapper.setDefaultDeleteOptions(Mapper.Option.consistencyLevel(DEFAULT_CONSISTENCY_LEVEL));
                    mapper.setDefaultGetOptions(Mapper.Option.consistencyLevel(DEFAULT_CONSISTENCY_LEVEL));
                    mapper.setDefaultSaveOptions(Mapper.Option.consistencyLevel(DEFAULT_CONSISTENCY_LEVEL));
                    mapperCache.put(className, mapper);
                } finally {
                    cacheLock.unlock();
                }
            }
            return mapper;
        } finally {
            sessionLock.readLock().unlock();
        }
    }

    public void close() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Closing connection...");
        }
        session.close();
        cluster.close();
    }

    //to initialize the database schema, insert keyspace and all tables.
    public void initDBSchema(DaoSchema daoSchema) throws Exception {
        //TODO check if the namespace exists.
        if (daoSchema.getCreateSchema() != null) {
            List<String> statements = daoSchema.getCreateSchema();
            for (String stmt : statements) {
                SimpleStatement simpleStatement = new SimpleStatement(stmt + ";");
                session.execute(simpleStatement);
            }
            //TODO handle cassandra init exception, should fail start if not pass.
        }
    }

    public ResultSet execute(Statement statement) {
        try {
            return execute(statement, DEFAULT_RETRY, DEFAULT_WAIT_MILLISECONDS);
        } catch (Exception e) {
            LOG.error("Error when execute the statemente ", statement, e);
        }
        return null;
    }

    private ResultSet execute(Statement statement, int retry, long delay) throws Exception {
        int fetchSize = statement.getFetchSize();
        if (fetchSize == 0) {
            statement.setFetchSize(DEFAULT_FETCH_SIZE);
        }
        if (retry < DEFAULT_RETRY) {
            retry = DEFAULT_RETRY;
        }
        if (delay < DEFAULT_WAIT_MILLISECONDS) {
            delay = DEFAULT_WAIT_MILLISECONDS;
        }
        statement.setConsistencyLevel(ConsistencyLevel.LOCAL_ONE);
        int attempt = 0;
        while (true) {
            try {
                if (attempt > 0 && statement instanceof BatchStatement) {
                    for (Statement stmt : ((BatchStatement) statement).getStatements()) {
                        execute(stmt);
                    }
                    break;
                } else {
                    return executeStatements(statement);
                }
            } catch (Exception e) {
                LOG.error("Cassandra exception when executing statements", e);
                attempt = handleDBException(attempt, retry, delay, e);
            }
        }
        return null;
    }

    private int handleDBException(int attempt, int retry, long delay, Exception e) throws Exception {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException ex) {
            LOG.warn("wait time expired", ex);
        }
        if (++attempt == retry) {
            throw e;
        }
        return attempt;
    }

    private ResultSet executeStatements(Statement statement) {
        if (session == null) {
            try {
                connect();
            } catch (CassandraException ex) {
                LOG.error("Cassandra connection error ", ex);
            }
        }
        sessionLock.readLock().lock();
        ResultSet result = null;
        try {
            result = session.execute(statement);
            return result;
        } catch (DriverException e) {
            LOG.error("Driver exception");
            throw e;
        } finally {
            sessionLock.readLock().unlock();
        }
    }

    public void createKeyspace(
            String keyspaceName, String replicationStrategy, int replicationFactor) {
        StringBuilder sb =
                new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ")
                        .append(keyspaceName).append(" WITH replication = {")
                        .append("'class':'").append(replicationStrategy)
                        .append("','replication_factor':").append(replicationFactor)
                        .append("};");

        String query = sb.toString();
        session.execute(query);
    }

    protected void finalize() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finalizing the Cassandra connection...");
        }
        close();
    }

    public Metadata getMetadata() {
        return cluster.getMetadata();
    }
}
