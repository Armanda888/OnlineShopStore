package com.theBeautiful.cassandra.dao;

import com.datastax.driver.core.*;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import com.google.common.collect.Lists;
import com.google.inject.Singleton;
import com.theBeautiful.cassandra.BundleSchema;
import com.theBeautiful.cassandra.model.AddressType;
import com.theBeautiful.cassandra.model.UserEntity;
import com.theBeautiful.cassandra.util.CassandraConnectorInterface;
import com.theBeautiful.cassandra.util.JiamiString;
import com.theBeautiful.config.BundleServices;
import com.theBeautiful.model.Address;
import com.theBeautiful.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;

@Singleton
public class UserDaoImpl implements UserDao{
    private static final Logger LOG = LoggerFactory.getLogger(UserDaoImpl.class);

    private static final String SALT = "Change the world";

    private UserAccessor getUserAccessor() {
        return BundleServices.getCassandraInterface().getAccessor(UserAccessor.class);
    }

    private Mapper<UserEntity> getEntityMapper() {
        return BundleServices.getCassandraInterface().getMapper(UserEntity.class);
    }

    private CassandraConnectorInterface getCassandraInterface() {
        return BundleServices.getCassandraInterface();
    }

    public List<User> getUsers() {
        Statement stmt = getUserAccessor().getUsers();
        ResultSet result = getCassandraInterface().execute(stmt);
        Result<UserEntity> userEntities = getEntityMapper().map(result);
        List users = Lists.newArrayList();
        for (UserEntity userEntity : userEntities) {
            users.add(userEntity.generate());
        }
        return users;
    }

    @Override
    public User upsert(User user) {

        if (user.getEmail() == null || user.getPassword() == null) {
           LOG.error("User input invalid with email " + user.getEmail());
        }
        String hashedPass = JiamiString.generateHash(SALT + user.getPassword());
        Statement statement = getUserAccessor().upsertUser(JiamiString.generateId(), user.getEmail(), hashedPass,
                user.getFirstName(), user.getLastName());
        getCassandraInterface().execute(statement);
        return user;
    }

    @Override
    public boolean login(User user, String password) {
        String hashedPass = JiamiString.generateHash(SALT + password);
        return hashedPass.equals(user.getPassword());
    }

    @Override
    public User getByEmail(String email) {
        if (email == null) {
            LOG.error("Email invalid to retrieve user info.");
            throw new NullPointerException("Email invalid to retrieve user info.");
        }
        Statement stmt = getUserAccessor().getByEmail(email);
        ResultSet result = getCassandraInterface().execute(stmt);
        UserEntity userEntity = null;
        if (result == null) {
            return null;
        } else {
            Result<UserEntity> userEntities = getEntityMapper().map(result);
            if (userEntities != null) {
                userEntity = userEntities.one();
            }
        }
        return userEntity == null ? null : userEntity.generate();
    }

    @Override
    public User getById(String userId) {
        if (userId == null) {
            LOG.error("Id invalid to retrieve user info.");
            throw new NullPointerException("Email invalid to retrieve user info.");
        }
        Statement stmt = getUserAccessor().getById(userId);
        ResultSet result = getCassandraInterface().execute(stmt);
        UserEntity userEntity = null;
        if (result == null) {
            return null;
        } else {
            Result<UserEntity> userEntities = getEntityMapper().map(result);
            if (userEntities != null) {
                userEntity = userEntities.one();
            }
        }
        return userEntity == null ? null : userEntity.generate();
    }

    @Override
    public User addAddress(User user, Address address) {
        if (address == null || !address.isValid()) {
            LOG.error("Address is invalid to add.", address);
            throw new InvalidParameterException("Address invalid");
        }
        address.setId(JiamiString.generateId());
        AddressType addressType = new AddressType(address);
        Statement stmt = getUserAccessor().addAddressToUser(Arrays.asList(addressType), user.getId(), user.getEmail());
        getCassandraInterface().execute(stmt);

        user.addAddress(address);
        return user;
    }

    @Override
    public User removeAddress(User user, Address address) {
        if (address == null || !address.isValid()) {
            LOG.error("Address is invalid to add.", address);
            throw new InvalidParameterException("Address invalid");
        }
        List<Address> addresses = user.getAddresses();
        addresses.remove(address);

        List<AddressType> addressTypes = Lists.newArrayList();
        for (Address addr : addresses) {
            addressTypes.add(new AddressType(addr));
        }
        Statement stmt = getUserAccessor().removeAddressFromUser(addressTypes, user.getId(), user.getEmail());
        getCassandraInterface().execute(stmt);

        user.addAddress(address);
        return user;
    }


    @Override
    public void removeByEmail(User user) {
        if (user == null) {
            LOG.error("No user associated with email ", user.getEmail());
        }
        Statement stmt = getUserAccessor().removeByEmail(user.getId(), user.getEmail());
        getCassandraInterface().execute(stmt);
    }

    @Accessor
    public interface UserAccessor {
        @Query("SELECT * FROM " + BundleSchema.KEYSPACE + "." + BundleSchema.USER_TABLE)
        Statement getUsers();

        @Query("SELECT * FROM " + BundleSchema.KEYSPACE + "." + BundleSchema.USER_TABLE + " " +
        "WHERE email = :email ALLOW FILTERING")
        Statement getByEmail(@Param("email") String email);

        @Query("SELECT * FROM " + BundleSchema.KEYSPACE + "." + BundleSchema.USER_TABLE + " " +
                "WHERE id = :userId ALLOW FILTERING")
        Statement getById(@Param("userId") String userId);

        @Query("INSERT INTO " + BundleSchema.KEYSPACE + "." + BundleSchema.USER_TABLE + " " +
        "(id, email, password, firstName, lastName) VALUES (:id, :email, :password, :firstName, :lastName) IF NOT EXISTS")
        Statement upsertUser(@Param("id") String id,
                            @Param("email") String email,
                            @Param("password") String password,
                            @Param("firstName") String firstName,
                            @Param("lastName") String lastName);

        @Query("UPDATE " + BundleSchema.KEYSPACE + "." + BundleSchema.USER_TABLE + " " +
            "SET addresses = :address" + " WHERE id = :id AND email = :email")
        Statement addAddressToUser(@Param("address") List<AddressType> address,
                                   @Param("id") String id,
                                   @Param("email") String email);

        @Query("UPDATE " + BundleSchema.KEYSPACE + "." + BundleSchema.USER_TABLE + " " +
                "SET addresses = :address" + " WHERE id = :id AND email = :email")
        Statement removeAddressFromUser(@Param("address") List<AddressType> addresses,
                                   @Param("id") String id,
                                   @Param("email") String email);

        @Query("DELETE FROM " + BundleSchema.KEYSPACE + "." + BundleSchema.USER_TABLE + " " +
            "WHERE id = :id AND email = :email")
        Statement removeByEmail(@Param("id") String id,
                                @Param("email") String email);
    }
}
