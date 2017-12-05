package com.theBeautiful.cassandra.exceptions;

/**
 * Created by jiaoli on 10/5/17
 */
public class CassandraException extends Exception {
    private static final long serialVersionUID = -1343343445345L;

    public CassandraException(String msg) {
        super(msg);
    }
}
