package com.newrelic.instrumentation.finagle.http;

import java.util.ArrayList;
import java.util.List;

public class FinagleHttpUtils {

	private static List<String> ignores = new ArrayList<>();
	
	static {
		ignores.add("com.twitter.finagle.Service$$anon$1");
	}
	
	public static boolean ignore(String classname) {
		if(classname.startsWith("com.twitter.finagle")) return true;
		return ignores.contains(classname);
	}
	
}
