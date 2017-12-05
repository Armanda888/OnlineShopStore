package com.theBeautiful;


import com.theBeautiful.config.BundleServices;
import com.theBeautiful.core.rest.RestResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MainApplicationServer extends Application<MainConfiguration> {
	private static final Logger LOG = LoggerFactory.getLogger(MainApplicationServer.class);

	private final static BundleServices bundleServices = new BundleServices();

	private ResourceConfig resourceConfig = new ResourceConfig();

	private ServletContextHandler context;
	private Server server;

	public static void main(String[] args) throws Exception{
		new MainApplicationServer().run(args);
	}
	
	@Override
	public void initialize(Bootstrap<MainConfiguration> bootstrap){
		super.initialize(bootstrap);
		start();
		//configurationBootstrap.addBundle(new AssetsBundle("/mvcs", "/", "index.html", "static"));
	}
	
	@Override
	public void run(MainConfiguration configuration, Environment environment) throws Exception {
		List<RestResource> restResources = bundleServices.getRestResources();
		/*
		for (RestResource rest : restResources) {
			resourceConfig.register(rest);
		}
		ServletHolder servlet = new ServletHolder(new ServletContainer(resourceConfig));
		server = new Server();
		context = new ServletContextHandler(server, "/*");
		context.addServlet(servlet, "/*");
		server.start();
		*/
		for (RestResource rest : restResources) {
			environment.jersey().register(rest);
		}


	}

	private void start() {
		try {
			bundleServices.start();
		} catch (Exception ex) {
			Error error = new Error("Application initialization error when bundle services start", ex);
			LOG.error(error.getMessage());
		}
	}

}
