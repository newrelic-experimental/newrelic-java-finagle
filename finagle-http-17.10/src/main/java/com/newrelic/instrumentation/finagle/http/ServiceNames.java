package com.newrelic.instrumentation.finagle.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import com.newrelic.api.agent.NewRelic;
import com.twitter.finagle.Name;
import com.twitter.finagle.Service;
import com.twitter.finagle.ServiceFactory;

public class ServiceNames {

	private static HashMap<String, String> serviceNames = new HashMap<>();
	private static HashMap<String, String> serviceFactoryNames = new HashMap<>();
	private static ArrayList<String> nonServiceNames = new ArrayList<>();
	
	public static <Req,Rep> void addServiceName(Object serviceObj, Object nameObj, String label) {
			addServiceName(serviceObj.getClass(), nameObj.toString(), label);
	}
	
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
	
	public static <Req,Rep> void addServiceFactoryName(ServiceFactory<Req,Rep> serviceFactory, String name, String label) {
		addServiceFactoryName(serviceFactory.getClass(), name, label);
	}

	public static <Req,Rep> void addServiceFactoryName(Object serviceFactoryObj, Name name, String label) {
		if(serviceFactoryObj instanceof ServiceFactory) {
			addServiceFactoryName((ServiceFactory<?,?>)serviceFactoryObj, name, label);
		}
	}
	
	public static <Req,Rep> void addServiceFactoryName(Object serviceFactoryObj, Object nameObject, String label) {
		if(nameObject instanceof Name) {
			addServiceFactoryName(serviceFactoryObj, (Name)nameObject, label);
		}

	}
	
	public static <Req,Rep> void addServiceFactoryName(ServiceFactory<Req,Rep> service, Name name, String label) {
		addServiceFactoryName(service, getNameString(name), label);
	}
	
	public static void addServiceFactoryName(Class<?> clazz, String name, String label) {
		addServiceFactoryName(clazz.getName(), name, label);
	}
	
	public static void addServiceFactoryName(String classname, String name, String label) {
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
		if(!serviceFactoryNames.containsKey(classname)) {
			NewRelic.getAgent().getLogger().log(Level.FINE, "Associating ServiceFactory {0} with name {1}", classname, nameStr);
			serviceFactoryNames.put(classname, nameStr);
		}
	}
	
	public static <Req,Rep> String getServiceName(Service<Req, Rep> service) {
		return getServiceName(service.getClass());
	}
	
	public static String getServiceName(Class<?> service) {
		return getServiceName(service.getName());
	}
	
	public static String getServiceName(String name) {
		String serviceName = serviceNames.get(name);
		if(serviceName != null && !serviceName.isEmpty()) {
			NewRelic.getAgent().getLogger().log(Level.FINE, "Retrived Service Factory name {0} for service {1}", serviceName, name);
		} else if(!nonServiceNames.contains(name)) {
			NewRelic.getAgent().getLogger().log(Level.FINE, "Did not find service name for service {0}", name);
			nonServiceNames.add(name);
		}
		return serviceName;
	}
	
	public static <Req,Rep> String getServiceFactoryName(ServiceFactory<Req, Rep> service) {
		return getServiceFactoryName(service.getClass());
	}
	
	public static String getServiceFactoryName(Class<?> service) {
		return getServiceFactoryName(service.getName());
	}
	
	public static String getServiceFactoryName(String name) {
		String serviceName = serviceNames.get(name);
		if(serviceName != null && !serviceName.isEmpty()) {
			NewRelic.getAgent().getLogger().log(Level.FINE, "Retrived Service Factory name {0} for servicefactory {1}", serviceName, name);
		} else if(!nonServiceNames.contains(name)) {
			NewRelic.getAgent().getLogger().log(Level.FINE, "Did not find service name for servicefactory {0}", name);
			nonServiceNames.add(name);
		}
		return serviceName;
	}
	
	public static String getNameString(Object obj) {
		if(obj instanceof Name) {
			return getNameString((Name)obj);
		}
		return null;
	}
	
	public static String getNameString(Name name) {
		if(name != null) return Name.showable().show(name);
		return null;
	}
}
