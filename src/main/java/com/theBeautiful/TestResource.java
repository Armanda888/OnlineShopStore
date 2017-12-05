package com.theBeautiful;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class TestResource {
	private String name;
	
	@Path("/hello")
	@GET
	public String getHello(){
		return "Hello";
	}
}
