package com.newrelic.instrumentation.finagle.http;

import java.util.Map;

import com.twitter.finagle.http.Method;
import com.twitter.finagle.http.Request;
import com.twitter.finagle.http.Response;
import com.twitter.finagle.http.Status;

public class Utils {

	public static void addAttribute(Map<String,Object> attributes, String key, Object value) {
		if(attributes != null && key != null && !key.isEmpty() && value != null) {
			attributes.put(key, value);
		}
	}

	public static void addRequest(Map<String,Object> attributes, Request request) {
		if (request != null) {
			addMethod(attributes, request.method());
			addAttribute(attributes, "Path", request.path());
			addAttribute(attributes, "RemoteHost", request.remoteHost());
			int port = request.remotePort();
			addAttribute(attributes, "RemotePort", port);
		}
	}
	
	public static void addMethod(Map<String,Object> attributes, Method method) {
		if(method != null) {
			addAttribute(attributes, "Method", method.name());
		}
	}
	
	public static void addStatus(Map<String,Object> attributes, Status status) {
		if (status != null) {
			addAttribute(attributes, "StatusCode", status.code());
			addAttribute(attributes, "StatusReason", status.reason());
		}
	}
	
	public static void addResponse(Map<String,Object> attributes, Response response) {
		if (response != null) {
			addStatus(attributes, response.status());
			addAttribute(attributes, "StatusCode", response.statusCode());
		}
	}
}
