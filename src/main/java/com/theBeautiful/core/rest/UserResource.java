package com.theBeautiful.core.rest;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.theBeautiful.core.service.UserService;
import com.theBeautiful.model.Address;
import com.theBeautiful.model.User;
import com.wordnik.swagger.annotations.ApiParam;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by jiaoli on 10/7/17
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource implements RestResource{
    private final Provider<UserService> userServiceProvider;
    private UserService userService;

    @Inject
    public UserResource(Provider<UserService> userServiceProvider) {
        this.userServiceProvider = userServiceProvider;
    }

    private void initializeService() {
        if (userService == null) {
            userService = userServiceProvider.get();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response signUp(@ApiParam("user") User user) {
        if (user == null || user.getEmail() == null || user.getPassword() == null) {
            Error error = new Error("Invalid email or password");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
        initializeService();
        User existingUser = userService.getByEmail(user.getEmail());
        if (existingUser != null) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        userService.signUp(user);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getUsers() {
        initializeService();
        List<User> users = userService.getUsers();
        return Response.status(Response.Status.OK).entity(users).build();
    }

    @GET
    @Path("/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserByEmail(@PathParam("email") String email) {
        initializeService();
        User user = userService.getByEmail(email);
        return Response.status(Response.Status.OK).entity(user).build();
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@ApiParam("email") User user) {
        if (user == null || user.getPassword() == null || user.getEmail() == null)  {
            Error error = new Error("Invalid email or password");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
        initializeService();
        User userDb = userService.getByEmail(user.getEmail());
        if (userDb == null ) {
            Error error = new Error("User with the email doesn't exist or password invalid");
            return Response.status(Response.Status.UNAUTHORIZED).entity(error).build();
        }
        boolean isAuthenticated = userService.login(userDb, user.getPassword());
        if (!isAuthenticated) {
            Error error = new Error("User with the email doesn't exist or password invalid");
            return Response.status(Response.Status.UNAUTHORIZED).entity(error).build();
        }
        return Response.status(Response.Status.ACCEPTED).build();
    }

    @PUT
    @Path("/{userId}/addresses")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAddress(@PathParam("userId") String userId,
                          @ApiParam("address") Address address) {
        if (userId == null || address == null || !address.isValid()) {
            Error error = new Error("Invalid email or new address to add.");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
        initializeService();
        User user = userService.getById(userId);
        if (user == null) {
            Error error = new Error("User not found.");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
        user = userService.addAddress(user, address);
        return Response.status(Response.Status.NO_CONTENT).entity(user).build();
    }

    @DELETE
    @Path("/{userId}/addresses/{addressId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAddress(@PathParam("userId") String userId,
                               @PathParam("addressId") String addressId) {
        if (userId == null || addressId == null) {
            Error error = new Error("Invalid email or new address to add.");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
        initializeService();
        User user = userService.getById(userId);
        if (user == null || user.getAddresses() == null) {
            Error error = new Error("User not found or address not found for the user.");
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
        List<Address> addresses = user.getAddresses();
        Address address = null;
        for (Address addr : addresses) {
            if (addr.getId() != null && addr.getId().equals(addressId)) {
                address = addr;
                break;
            }
        }
        if (address == null) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        userService.removeAddress(user, address);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAddress(@PathParam("userId") String userId) {
        if (userId == null) {
            Error error = new Error("Invalid email or new address to add.");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
        initializeService();
        User existingUser = userService.getById(userId);
        if (existingUser == null) {
            Error error = new Error("User doesn't exist with given email.");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
        userService.removeUserByEmail(existingUser);
        return Response.status(Response.Status.ACCEPTED).build();
    }


    @Override
    public String getBaseUrl() {
        return getClass().getAnnotation(Path.class).value();
    }

    @Override
    public String getName() {
        return "User REST service";
    }
}
