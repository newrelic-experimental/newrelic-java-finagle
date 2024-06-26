package com.newrelic.instrumentation.finagle.http.tracing;

import com.newrelic.agent.instrumentation.classmatchers.ClassAndMethodMatcher;
import com.newrelic.agent.instrumentation.classmatchers.ClassMatcher;
import com.newrelic.agent.instrumentation.methodmatchers.MethodMatcher;
import com.newrelic.agent.instrumentation.methodmatchers.NameMethodMatcher;

public class ServiceClassAndMethod implements ClassAndMethodMatcher {
	
	private ClassMatcher classMatcher;
	private MethodMatcher methodMatcher;
	
	public ServiceClassAndMethod() {
		classMatcher = new ServiceClassMatcher();
		methodMatcher = new NameMethodMatcher("apply");
	}

	@Override
	public ClassMatcher getClassMatcher() {
		return classMatcher;
	}

	@Override
	public MethodMatcher getMethodMatcher() {
		return methodMatcher;
	}

}
