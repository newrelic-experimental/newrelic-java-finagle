package com.newrelic.instrumentation.finagle.http;

import java.net.InetAddress;
import java.net.URI;
import java.util.logging.Level;

import com.newrelic.api.agent.ExternalParameters;
import com.newrelic.api.agent.HttpParameters;
import com.newrelic.api.agent.NewRelic;
import com.twitter.finagle.Service;
import com.twitter.finagle.http.Method;
import com.twitter.finagle.http.Request;

public class FinagleHttpUtils {

	
	public static <Req, Rep> boolean isFinch(Service<Req, Rep> service) {
		Package clazzPackage = service.getClass().getPackage();
		
		return clazzPackage.getName().startsWith("io.finch");
	}
	
	public static ExternalParameters getExternalFromRequest(Request request) {
		String remoteHost = request.remoteHost();
		int remotePort = request.remotePort();
		
		if(remoteHost == null || remoteHost.isEmpty()) {
			InetAddress remoteAddr = request.remoteAddress();
			if(remoteAddr != null && remoteAddr instanceof InetAddress) {
				InetAddress addr = (InetAddress)remoteAddr;
				remoteHost = addr.getHostName();
			}
		}
		
		 Method method = request.method();
		 String methodName = null;
		 if(method != null) {
			 methodName = method.name();
		 }
		 
		 String path = request.path();
		 
		 StringBuffer sb = new StringBuffer();
		 if(remoteHost != null) {
			 sb.append("http://");
			 sb.append(remoteHost);
			 
			 if(remotePort > 0) {
				 sb.append(":");
				 sb.append(remotePort);
			 }
			 
			 if(path != null && !path.isEmpty()) {
				 sb.append(path);
			 }
		 }
		 
		 if(sb.length() > 0) {
			 URI uri = URI.create(sb.toString());
			 if(methodName != null && !methodName.isEmpty()) {
				 return HttpParameters.library("Finagle-Netty4").uri(uri).procedure(methodName).noInboundHeaders().build();
			 } else {
				 return HttpParameters.library("Finagle-Netty4").uri(uri).procedure("send").noInboundHeaders().build();
			 }
		 }
		
		return null;
	}
	
	
	public static void dumpRequestInfo(Request request) {
		String remoteHost = request.remoteHost();
		int remotePort = request.remotePort();
		
		if(remoteHost == null || remoteHost.isEmpty()) {
			InetAddress remoteAddr = request.remoteAddress();
			if(remoteAddr != null && remoteAddr instanceof InetAddress) {
				InetAddress addr = (InetAddress)remoteAddr;
				remoteHost = addr.getHostName();
			}
		}
		
		 Method method = request.method();
		 String methodName = null;
		 if(method != null) {
			 methodName = method.name();
		 }
		 
		 String uri = request.uri();

		 String path = request.path();
		 
		 NewRelic.getAgent().getLogger().log(Level.FINE, "Request: remoteHost: {0}, remotePort: {1}, method: {2}, uri: {3}, path: {4}", remoteHost, remotePort, methodName, uri, path);
		 		
	}
}
