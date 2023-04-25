package com.twitter.finagle;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.logging.Level;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.finagle.thrift.Utils;
import com.twitter.finagle.thrift.ThriftClientRequest;

@Weave
public abstract class Thrift {

	public static Service<ThriftClientRequest, byte[]> newService(Name dest, String label) {
		String n = null;
		if(dest != null) {
			String d = Name.showable().show(dest);
			if(d != null && !d.isEmpty()) {
				n = d;
			}
		}
		String metricName = n != null ? "Thrift/" + n + "/" + label : "Thrift/" + label;
		return Utils.getServiceWrapper(Weaver.callOriginal(), metricName);
	}
	
	public static Service<ThriftClientRequest, byte[]> newService(String dest, String label) {
		String metricName = dest != null ? "Thrift/" + dest + "/" + label : "Thrift/" +label;
		return Utils.getServiceWrapper(Weaver.callOriginal(), metricName);
	}
	
	public static Service<ThriftClientRequest, byte[]> newService(String dest) {
		Service<ThriftClientRequest, byte[]> service = Weaver.callOriginal();
		if(dest != null && !dest.isEmpty()) {
			return Utils.getServiceWrapper(service, "Thrift/" + dest);
		}
		return Utils.getServiceWrapper(service);
	}
	
	
	@Weave
	public static class Client {
		
		public Service<ThriftClientRequest, byte[]> newService(Name dest, String label) {
			String n = null;
			if(dest != null) {
				String d = Name.showable().show(dest);
				if(d != null && !d.isEmpty()) {
					n = d;
				}
			}
			String metricName = "Thrift/" + n != null ? n + "/" + label : label;
			Service<ThriftClientRequest, byte[]> serv = Weaver.callOriginal();
		
			return Utils.getServiceWrapper(serv, metricName);
		}
		
		public Service<ThriftClientRequest, byte[]> newService(String dest) { 
			Service<ThriftClientRequest, byte[]> service = Weaver.callOriginal();
			if(dest != null && !dest.isEmpty()) {
				return Utils.getServiceWrapper(service, "Thrift/" + dest);
			}
			return Utils.getServiceWrapper(service);
		}
		
		public Service<ThriftClientRequest, byte[]> newService(String dest, String label) {
			Service<ThriftClientRequest, byte[]> service = Weaver.callOriginal();
			if(dest != null && !dest.isEmpty()) {
				return Utils.getServiceWrapper(service, "Thrift/" + dest + "/" + label);
			}
			return Utils.getServiceWrapper(service);
		}
		
	}
	
	@Weave
	public static class Server {

		
        public ListeningServer serve(SocketAddress addr, Service<byte[], byte[]> service) {
        	String serverAdd = null;
        	if(addr instanceof InetSocketAddress) {
        		InetSocketAddress iAddr = (InetSocketAddress)addr;
        		String host = iAddr.getHostName();
        		int port = iAddr.getPort();
        		if(port > 0) {
        			serverAdd = host + ":" + port;
        		} else {
        			serverAdd = host;
        		}
        	}
        	String metricName = null;
        	if(serverAdd != null) {
        		metricName = "Thrift/Serve/" + serverAdd;
        	} else {
        		metricName = "Thrift/Serve";
        	}
        	service =  Utils.getServiceWrapperWithStart(service, metricName);
            return Weaver.callOriginal();
        }
        
        public ListeningServer serve(String addr, Service<byte[], byte[]> service) {
        	String metricName = null;
        	if(addr != null) {
        		metricName = "Thrift/Serve/" + addr;
        	} else {
        		metricName = "Thrift/Serve";
        	}
        	service =  Utils.getServiceWrapperWithStart(service, metricName);
            return Weaver.callOriginal();
        }
        
        public ListeningServer serveAndAnnounce(String name, SocketAddress addr, Service<byte[], byte[]> service) {
        	String serverAdd = null;
        	if(addr instanceof InetSocketAddress) {
        		InetSocketAddress iAddr = (InetSocketAddress)addr;
        		String host = iAddr.getHostName();
        		int port = iAddr.getPort();
        		if(port > 0) {
        			serverAdd = host + ":" + port;
        		} else {
        			serverAdd = host;
        		}
        	}
        	String metricName = null;
        	if(serverAdd != null) {
        		metricName = "Thrift/Serve/" + serverAdd + "/" + name;
        	} else {
        		metricName = "Thrift/Serve/"+name;
        	}
        	service =  Utils.getServiceWrapperWithStart(service, metricName);
            return Weaver.callOriginal();
        }
        
        public ListeningServer serveAndAnnounce(String name, String addr, Service<byte[], byte[]> service) {
        	String metricName = null;
        	if(addr != null) {
        		metricName = "Thrift/Serve/" + addr + "/" + name;
        	} else {
        		metricName = "Thrift/Serve/"+name;
        	}
        	service =  Utils.getServiceWrapperWithStart(service, metricName);
            return Weaver.callOriginal();
        }
        
        public ListeningServer serveAndAnnounce(String name, Service<byte[], byte[]> service) {
        	String metricName = null;
        	if(name != null) {
        		metricName = "Thrift/Serve/" + name;
        	} else {
        		metricName = "Thrift/Serve";
        	}
        	service =  Utils.getServiceWrapperWithStart(service, metricName);
            return Weaver.callOriginal();
        }
        
	}
}
