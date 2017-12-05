package com.theBeautiful.cassandra.util;

import java.io.IOException;

/**
 * @author rishabh.
 */
public interface JsonSupport<T> {
    /**
     * Creates an instance of this object, from JSON string.
     *
     * @param json JSON String.
     * @param type The Class of the object that needs to be created.
     * @param <T>
     * @return An instance of the specified Class.
     * @throws IOException On providing incorrect JSON string.
     */
    <T> T instance(String json, Class<T> type) throws IOException;

    /**
     * Alternative to the toString().<br>
     * ATTENTION: To be used with caution as it is process intensive.
     *
     * @return The JSON representation of the object.
     */
    String toJsonString();
}
