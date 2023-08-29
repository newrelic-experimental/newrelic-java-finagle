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

}
