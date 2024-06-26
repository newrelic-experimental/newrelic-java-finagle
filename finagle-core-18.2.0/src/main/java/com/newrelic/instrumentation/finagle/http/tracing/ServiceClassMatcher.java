package com.newrelic.instrumentation.finagle.http.tracing;

import java.util.Collection;

import com.newrelic.agent.deps.org.objectweb.asm.ClassReader;
import com.newrelic.agent.instrumentation.classmatchers.ChildClassMatcher;
import com.newrelic.agent.instrumentation.classmatchers.ClassMatcher;
import com.newrelic.agent.instrumentation.classmatchers.ExactClassMatcher;

public class ServiceClassMatcher extends ClassMatcher {
	
	private static final String SERVICE_CLASS_NAME = "com.twitter.finagle.Service";
	private static final String SERVICEPROXY_CLASS_NAME = "com.twitter.finagle.ServiceProxy";
	private static final String IGNORED = "$$anon";
	private static final String CLIENTBUILDER_CREATED = "com.twitter.finagle.builder.ClientBuilderClient$$anon$2";
	private ChildClassMatcher childClassMatcher = null;
	private ChildClassMatcher serviceProxyMatcher = null;
	private ExactClassMatcher builtClientMatcher = null;
	
	public ServiceClassMatcher() {
		childClassMatcher = new ChildClassMatcher(SERVICE_CLASS_NAME,false);
		serviceProxyMatcher = new ChildClassMatcher(SERVICEPROXY_CLASS_NAME,false);
		builtClientMatcher = new ExactClassMatcher(CLIENTBUILDER_CREATED);
	}
	

	@Override
	public boolean isMatch(ClassLoader loader, ClassReader cr) {
		if(builtClientMatcher.isMatch(loader, cr)) return true;
		String className = cr.getClassName();
		return childClassMatcher.isMatch(loader, cr) && !className.contains(IGNORED) && !serviceProxyMatcher.isMatch(loader, cr);
	}

	@Override
	public boolean isMatch(Class<?> clazz) {
		if(builtClientMatcher.isMatch(clazz)) return true;
		String className = clazz.getName();
		
		return childClassMatcher.isMatch(clazz) && !className.contains(IGNORED) && !serviceProxyMatcher.isMatch(clazz);
	}

	@Override
	public Collection<String> getClassNames() {
		return childClassMatcher.getClassNames();
	}

}
