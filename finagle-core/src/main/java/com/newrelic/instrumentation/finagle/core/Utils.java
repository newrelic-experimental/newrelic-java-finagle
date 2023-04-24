package com.newrelic.instrumentation.finagle.core;

import java.util.Map;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;
import com.twitter.finagle.Service;
import com.twitter.util.Future;

public class Utils {

	public static void addAttribute(Map<String,String> attributes, String key, String value) {
		if(attributes != null && key != null && !key.isEmpty() && value != null) {
			attributes.put(key, value);
		}
	}
	
	public static <Req,Rep> Service<Req,Rep> getServiceWrapper(Service<Req,Rep> service) {
		if(service instanceof NewRelicServiceWrapper) return service;
		
		return new NewRelicServiceWrapper<Req,Rep>(service);
	}
	
	public static <Req,Rep> Service<Req,Rep> getServiceWrapper(Service<Req,Rep> service, String name) {
		if(service instanceof NewRelicServiceWrapper) return service;
		
		NewRelicServiceWrapper<Req, Rep> wrapper = new NewRelicServiceWrapper<>(service);
		wrapper.setName(name);
		return wrapper;
	}
	
	public static <A> Future<A> addNRListener(Future<A> f, String segmentName) {
		Segment segment = NewRelic.getAgent().getTransaction().startSegment(segmentName);
		NRFutureEventListener<A> listener = new NRFutureEventListener<>(segment);
		return f.addEventListener(listener);
	}

}