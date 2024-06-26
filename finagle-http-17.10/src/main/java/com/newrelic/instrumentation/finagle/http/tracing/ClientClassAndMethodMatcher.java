package com.newrelic.instrumentation.finagle.http.tracing;

import com.newrelic.agent.instrumentation.classmatchers.ClassAndMethodMatcher;
import com.newrelic.agent.instrumentation.classmatchers.ClassMatcher;
import com.newrelic.agent.instrumentation.methodmatchers.MethodMatcher;

public class ClientClassAndMethodMatcher implements ClassAndMethodMatcher {
	
	private ClientClassMatcher clazzMatcher;
	private ClientMethodMatcher methodMatcher;
	
	public ClientClassAndMethodMatcher() {
		clazzMatcher = new ClientClassMatcher();
		methodMatcher = new ClientMethodMatcher();
	}

	@Override
	public ClassMatcher getClassMatcher() {
		return clazzMatcher;
	}

	@Override
	public MethodMatcher getMethodMatcher() {
		return methodMatcher;
	}

}
