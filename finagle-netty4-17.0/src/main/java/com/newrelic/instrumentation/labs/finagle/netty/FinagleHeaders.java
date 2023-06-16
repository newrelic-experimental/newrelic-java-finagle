package com.newrelic.instrumentation.labs.finagle.netty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.newrelic.api.agent.HeaderType;
import com.newrelic.api.agent.Headers;
import com.twitter.finagle.http.HeaderMap;
import com.twitter.finagle.http.Message;

import scala.collection.JavaConverters;
import scala.collection.Set;

public class FinagleHeaders implements Headers {
	
	private Message message;
	
	public FinagleHeaders(Message msg) {
		message = msg;
	}

	@Override
	public HeaderType getHeaderType() {
		return HeaderType.HTTP;
	}

	@Override
	public String getHeader(String name) {
		HeaderMap headerMap = message.headerMap();
		if(headerMap != null) {
			return headerMap.getOrNull(name);
		}
		return null;
	}

	@Override
	public Collection<String> getHeaders(String name) {
		HeaderMap headerMap = message.headerMap();
		List<String> list = new ArrayList<>();
		if(headerMap != null) {
			String value = headerMap.getOrNull(name);
			if(value != null) {
				list.add(value);
			}
		}
		return list;
	}

	@Override
	public void setHeader(String name, String value) {
		HeaderMap headerMap = message.headerMap();
		headerMap.set(name, value);
	}

	@Override
	public void addHeader(String name, String value) {
		HeaderMap headerMap = message.headerMap();
		headerMap.add(name, value);
	}

	@Override
	public Collection<String> getHeaderNames() {
		HeaderMap headerMap = message.headerMap();
		Set<String> keys = headerMap.keySet();
		return JavaConverters.setAsJavaSet(keys);
	}

	@Override
	public boolean containsHeader(String name) {
		
		return getHeaderNames().contains(name);
	}

}
