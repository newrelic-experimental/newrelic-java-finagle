package com.newrelic.instrumentation.finagle.core;

import java.util.Map;

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

	@SuppressWarnings("unchecked")
	public static <Res> void addEventListener(Object future, NewRelicHolder holder, Object request) {
		if(future instanceof com.twitter.util.Future) {
			
			com.twitter.util.Future<Res> f = (com.twitter.util.Future<Res>)future;
			NRFutureEventListener<Res> eventListener = new NRFutureEventListener<>(holder, request);
			f.addEventListener(eventListener);
		}
	}
}
