package com.newrelic.instrumentation.finagle.core;

import java.util.logging.Level;

import com.newrelic.api.agent.NewRelic;
import com.twitter.finagle.Name;
import com.twitter.finagle.Service;
import com.twitter.finagle.ServiceFactory;

public class ServiceUtils {

	private static ServiceUtils INSTANCE = null;
	private static int instanceCount = 0;
	
	private ServiceUtils() {
		instanceCount++;
		NewRelic.getAgent().getLogger().log(Level.FINE, "Created ServiceUtils, instancecount {0}", instanceCount);	
	}
	
	public static synchronized ServiceUtils getInstance() {
		if(INSTANCE == null) {
				if(INSTANCE == null) {
					INSTANCE = new ServiceUtils();
				}
		}
		return INSTANCE;
	}
	
	public <Req,Rep> void addServiceFactoryName(ServiceFactory<Req,Rep> service, Name name, String label) {
		
	}
	
	public <Req,Rep> void addServiceName(Service<Req,Rep> service, Name name, String label) {
		
	}
	
	public <Req,Rep> void addServiceName(Service<Req, Rep> service, String name, String label) {
		
	}
	
	public <Req,Rep> String getServiceName(Object object) {
		return null;
	}
	
	public  <Req,Rep> String getServiceName(Service<Req, Rep> service) {
		return null;
	}
	
	public <Req,Rep> String getServiceFactoryName(Object service) {
		return null;
	}
	
	public <Req,Rep> String getServiceFactoryName(ServiceFactory<Req, Rep> service) {
		return null;
	}
	
	public <Req,Rep> void addServiceFactoryName(ServiceFactory<Req,Rep> serviceFactory, String name, String label) {
		
	}
	

		
}
