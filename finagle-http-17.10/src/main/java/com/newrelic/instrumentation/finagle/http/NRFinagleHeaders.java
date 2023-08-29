package com.newrelic.instrumentation.finagle.http;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.newrelic.api.agent.HeaderType;
import com.newrelic.api.agent.Headers;
import com.twitter.finagle.http.HeaderMap;
import com.twitter.finagle.http.Message;

import scala.collection.JavaConverters;

/**
 * Used insert or extract distributed tracing headers on the Finagle Request or Response (both extend the Message class)
 * 
 * @author dhilpipre
 *
 */
public class NRFinagleHeaders implements Headers {
	
	private Message message = null;
	
	public NRFinagleHeaders(Message req) {
		message = req;
	}

	@Override
	public HeaderType getHeaderType() {
		return HeaderType.HTTP;
	}

	@Override
	public String getHeader(String name) {
		HeaderMap headers = message.headerMap();
		if(headers == null) {
			return null;
		}
		return headers.getOrNull(name);
	}

	@Override
	public Collection<String> getHeaders(String name) {
		List<String> list = new ArrayList<>();
		String value = getHeader(name);
		if(value != null) {
			list.add(value);
		}
		return list;
	}

	@Override
	public void setHeader(String name, String value) {
		HeaderMap headers = message.headerMap();
		if(headers != null) {
			headers.set(name, value);
		}
		
	}

	@Override
	public void addHeader(String name, String value) {
		setHeader(name, value);
	}

	@Override
	public Collection<String> getHeaderNames() {
		HeaderMap headers = message.headerMap();
		if(headers != null) {
			scala.collection.Set<String> keys = headers.keySet();
			Set<String> javaKeySet = JavaConverters.setAsJavaSet(keys);
			return javaKeySet;
		}
		
		return new ArrayList<>();
	}

	@Override
	public boolean containsHeader(String name) {
		return getHeaderNames().contains(name);
	}

}
