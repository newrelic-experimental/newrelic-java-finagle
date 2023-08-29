package com.newrelic.instrumentation.finagle.http;

import java.util.HashMap;
import java.util.Map;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.TransactionNamePriority;
import com.twitter.finagle.http.Method;
import com.twitter.finagle.http.Request;
import com.twitter.finagle.http.Response;
import com.twitter.finagle.http.Status;

/**
 * Utility class used to populate name value pairs that are then added as
 * attributes to a Span
 * 
 * @author dhilpipre
 *
 */
public class Utils {

	public static void addAttribute(Map<String,Object> attributes, String key, Object value) {
		if(attributes != null && key != null && !key.isEmpty() && value != null) {
			attributes.put(key, value);
		}
	}

	public static void addRequest(Map<String,Object> attributes, Request request) {
		if (request != null) {
			addMethod(attributes, request.method(),"Request");
			addAttribute(attributes, "Request-Path", request.path());
			addAttribute(attributes, "Request-RemoteHost", request.remoteHost());
			int port = request.remotePort();
			addAttribute(attributes, "Request-RemotePort", port);
		}
	}
	
	public static void addMethod(Map<String,Object> attributes, Method method, String prefix) {
		if(method != null) {
			String name = prefix != null ? prefix + "-Method" : "Method";
			addAttribute(attributes, name, method.name());
		}
	}
	
	public static void addStatus(Map<String,Object> attributes, Status status) {
		if (status != null) {
			addAttribute(attributes, "Response-StatusCode", status.code());
			addAttribute(attributes, "Response-StatusReason", status.reason());
		}
	}
	
	public static void addResponse(Map<String,Object> attributes, Response response) {
		if (response != null) {
			addStatus(attributes, response.status());
			addAttribute(attributes, "Response-StatusCode", response.statusCode());
		}
	}
	
	public static void setTransactionName(Request request) {
		String path = request.path();
		if(path != null) {
			if(path.equals("") || path.equals("/")) {
				path = "Root";
			}
			NewRelic.getAgent().getTransaction().setTransactionName(TransactionNamePriority.FRAMEWORK_LOW, false, "FinagleServerDispatcher", "Finagle","Request",path);
		}
	}
	
	public static Map<String, Object> getAttributes(Object obj) {
		HashMap<String, Object> attributes = new HashMap<>();
		if(obj instanceof Request) {
			HashMap<String, Object> requestAttributes = new HashMap<>();
			addRequest(requestAttributes, (Request)obj);
			attributes.putAll(requestAttributes);
		}
		if(obj instanceof Response) {
			HashMap<String, Object> responseAttributes = new HashMap<>();
			addResponse(responseAttributes, (Response)obj);
			attributes.putAll(responseAttributes);
		}
		
		return attributes.isEmpty() ? null : attributes;
	}
}
