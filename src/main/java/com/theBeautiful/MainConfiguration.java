package com.theBeautiful;

import io.dropwizard.Configuration;

public class MainConfiguration extends Configuration{

	private String name;
	private String port = "80";
	
	

	public String getPort() {
		return port;
	}



	public String getName() {
		return name;
	}
	
	
}
