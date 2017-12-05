package com.theBeautiful.cassandra;

import com.datastax.driver.core.TypeCodec;

import java.util.Collection;
import java.util.List;

public abstract class DaoSchema {
    private final String namespace;
    private final String schemaVersion;

    protected DaoSchema(String namespace, String schemaVersion) {
        this.namespace = namespace;
        this.schemaVersion = schemaVersion;
    }

    public abstract Collection<TypeCodec<?>> getRequiredCodecs();

    public abstract List<String> getCreateSchema() throws Exception;

    public String getNamespace() {
        return namespace;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }
}
