package com.newrelic.instrumentation.finagle.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.logging.Level;

import com.newrelic.api.agent.NewRelic;
import com.twitter.finagle.Name;
import com.twitter.finagle.Service;
import com.twitter.finagle.ServiceFactory;


public class ServiceNames {

	private static int instanceCount = 0;
	public static ServiceNames getInstance() {
		return SingletonHolder.instance;
	}

	private ServiceNames() {
		instanceCount++;
		NewRelic.getAgent().getLogger().log(Level.FINE, "Call to ServiceNames.<init>, instance count: {0}",instanceCount);
		serviceNames = new Hashtable<>();
	}


	//	private static HashMap<String, String> serviceNames = new HashMap<>();
	private final Hashtable<Object, String> serviceNames;
	//	private static HashMap<String, String> serviceFactoryNames = new HashMap<>();
	private static HashMap<Integer, String> serviceFactoryNames = new HashMap<>();
	private ArrayList<Object> nonServiceNames = new ArrayList<>();


	public <Req,Rep> void addServiceName(Service<Req,Rep> service, Name name, String label) {
		NewRelic.getAgent().getLogger().log(Level.FINE, "Call to ServiceNames.addServiceName({0}-{3},{1},{2})", service,name,label, service.hashCode());
		//addServiceName(service, getNameString(name), label);
		//		Integer hash = service.hashCode();
		if(!serviceNames.containsKey(service)) {
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

			NewRelic.getAgent().getLogger().log(Level.FINE, "Associating service {0} with name {1}", service,nameStr);

			serviceNames.put(service, nameStr);
			NewRelic.getAgent().getLogger().log(Level.FINE, "After adding size of serviceNames is {0} ", serviceNames.size());
			NewRelic.getAgent().getLogger().log(Level.FINE, "For hash {0}, entry is {1}", service, serviceNames.get(service));

		}
	}


	public <Req,Rep> void addServiceName(Service<Req, Rep> service, String name, String label) {
		NewRelic.getAgent().getLogger().log(Level.FINE, "Call to ServiceNames.addServiceName({0}-{3},{1},{2})", service,name,label, service.hashCode());
		//addServiceName(service, getNameString(name), label);
		if(!serviceNames.containsKey(service)) {
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
			NewRelic.getAgent().getLogger().log(Level.FINE, "Associating hash code {0} with name {1}", service,nameStr);

			serviceNames.put(service, nameStr);
			NewRelic.getAgent().getLogger().log(Level.FINE, "After adding size of serviceNames is {0} ", serviceNames.size());
			NewRelic.getAgent().getLogger().log(Level.FINE, "For hash {0}, entry is {1}", service, serviceNames.get(service));

		}
	}


	public  <Req,Rep> void addServiceFactoryName(ServiceFactory<Req,Rep> service, Name name, String label) {
	}
	
	

	public  <Req,Rep> String getServiceName(Service<Req, Rep> service) {
		String name = null;
		synchronized (serviceNames) {
			name = serviceNames.get(service);
		}
		return name;
	}

	public <Req,Rep> String getServiceName(Object object) {
		NewRelic.getAgent().getLogger().log(Level.FINE, "Call to getServiceName with Object {0}", object);
		NewRelic.getAgent().getLogger().log(Level.FINE, "Current size of serviceNames is {0}", serviceNames.size());
		for(Object key : serviceNames.keySet()) {
			NewRelic.getAgent().getLogger().log(Level.FINE, "\tkey: {0}, value : {1}",key,serviceNames.get(key));
		}
		String serviceName = null;
		synchronized (serviceNames) {
			serviceName = serviceNames.get(object);
		}
		if(serviceName != null && !serviceName.isEmpty()) {
			NewRelic.getAgent().getLogger().log(Level.FINE, "Retrived Service Factory name {0} for service  {1}", serviceName, object);
		} else if(!nonServiceNames.contains(object)) {
			NewRelic.getAgent().getLogger().log(Level.FINE, "Did not find service name for service  {0}", object);
			nonServiceNames.add(object);
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
	
	static class SingletonHolder {
		static ServiceNames instance = new ServiceNames();
	}
}
