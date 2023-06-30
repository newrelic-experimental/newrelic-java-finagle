package com.newrelic.instrumentation.labs.finagle.netty;

import java.net.InetAddress;
import java.net.URI;

import com.newrelic.api.agent.ExternalParameters;
import com.newrelic.api.agent.HttpParameters;
import com.twitter.finagle.http.Message;
import com.twitter.finagle.http.Method;
import com.twitter.finagle.http.Request;

import io.netty.handler.codec.http.DefaultHttpObject;
import io.netty.handler.codec.http.HttpMessage;

public class FinagleNettyUtils {

	
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
	
	public static boolean shouldGenerateDispatcher(Object obj) {
		if(obj instanceof DefaultHttpObject) {
			return false;
		}
		if(obj instanceof HttpMessage) {
			return true;
		}
		if(obj instanceof Message) {
			return true;
		}
	
		return false;
	}
	
}
