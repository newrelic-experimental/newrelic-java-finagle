package com.newrelic.instrumentation.labs.finagle.netty;

import java.util.Collection;

import com.newrelic.api.agent.HeaderType;
import com.newrelic.api.agent.Headers;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;

public class FinagleNettyHeaders implements Headers {
	
	private HttpMessage msg = null;
	
	public FinagleNettyHeaders(HttpMessage message) {
		msg = message;
	}

	@Override
	public HeaderType getHeaderType() {
		return HeaderType.HTTP;
	}

	@Override
	public String getHeader(String name) {
		HttpHeaders headers = msg.headers();
		return headers.get(name);
	}

	@Override
	public Collection<String> getHeaders(String name) {
		HttpHeaders headers = msg.headers();
		return headers.getAll(name);
	}

	@Override
	public void setHeader(String name, String value) {
		HttpHeaders headers = msg.headers();
		headers.set(name, value);
	}

	@Override
	public void addHeader(String name, String value) {
		HttpHeaders headers = msg.headers();
		headers.add(name, value);
	}

	@Override
	public Collection<String> getHeaderNames() {
		HttpHeaders headers = msg.headers();
		return headers.names();
	}

	@Override
	public boolean containsHeader(String name) {
		
		return getHeaderNames().contains(name);
	}

}
