package com.newrelic.instrumentation.finagle.http;

import java.util.HashMap;
import java.util.logging.Level;

import com.newrelic.api.agent.NewRelic;
import com.twitter.finagle.Name;
import com.twitter.finagle.Service;

public class ServiceNames {

	private static HashMap<String, String> serviceNames = new HashMap<>();
	
	public static <Req,Rep> void addServiceName(Service<Req,Rep> service, Name name, String label) {
		addServiceName(service, getNameString(name), label);
	}
	
	public static void addServiceName(Class<?> clazz, String name, String label) {
		addServiceName(clazz.getName(), name, label);
	}
	
	public static void addServiceName(String classname, String name, String label) {
		StringBuffer sb = new StringBuffer();
		if(name != null) {
			sb.append(name);
			if(label != null) {
				sb.append('/');
				sb.append(label);
			}
		} else {
			if(label != null) {
				sb.append(label);
			}
		}
		if(sb.length() == 0) return;
		
		String nameStr = sb.toString();
		if(!serviceNames.containsKey(classname)) {
			NewRelic.getAgent().getLogger().log(Level.FINE, "Associating Service {0} with name {1}", classname, nameStr);
			serviceNames.put(classname, nameStr);
		}
	}
	
	public static <Req,Rep> void addServiceName(Service<Req, Rep> service, String name, String label) {
		addServiceName(service.getClass(), name, label);
	}
	
	public static <Req,Rep> String getServiceName(Service<Req, Rep> service) {
		return getServiceName(service.getClass());
	}
	
	public static String getServiceName(Class<?> service) {
		return getServiceName(service.getName());
	}
	
	public static String getServiceName(String name) {
		String serviceName = serviceNames.get(name);
		NewRelic.getAgent().getLogger().log(Level.FINE, "Retrived Service {0} for service {1}", serviceName, name);
		return serviceName;
	}
	
	public static String getNameString(Name name) {
		if(name != null) return Name.showable().show(name);
		return null;
	}
}
