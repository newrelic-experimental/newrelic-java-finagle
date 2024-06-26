package com.newrelic.instrumentation.finagle.http.tracing;

import java.util.Collection;
import java.util.logging.Level;

import com.newrelic.agent.deps.org.objectweb.asm.ClassReader;
import com.newrelic.agent.instrumentation.classmatchers.ClassMatcher;
import com.newrelic.agent.instrumentation.classmatchers.ExactClassMatcher;
import com.newrelic.agent.instrumentation.classmatchers.OrClassMatcher;
import com.newrelic.api.agent.NewRelic;

public class ClientClassMatcher extends ClassMatcher {
	
	private ClassMatcher matcher = null;
	private static final String NILCLIENT_NAME = "com.twitter.finagle.builder.ClientConfig$NilClient";
	private static final String CLIENT_NAME = "com.twitter.finagle.Http$Client";
	private static final String HTTP_NAME = "com.twitter.finagle.Http";
	
	public ClientClassMatcher() {
		ExactClassMatcher nilClientMatcher = new ExactClassMatcher(NILCLIENT_NAME);
		ExactClassMatcher clientMatcher = new ExactClassMatcher(CLIENT_NAME);
		ExactClassMatcher httpMatcher = new ExactClassMatcher(HTTP_NAME);
		matcher = OrClassMatcher.getClassMatcher(nilClientMatcher,clientMatcher,httpMatcher);
	}

	@Override
	public boolean isMatch(ClassLoader loader, ClassReader cr) {
		boolean b =  matcher.isMatch(loader, cr);
		NewRelic.getAgent().getLogger().log(Level.FINE, "Call to ClientClassMatcher is using classloader: {0} and classreader: {1} is returning {2}", loader,cr,b);
		return b;
	}

	@Override
	public boolean isMatch(Class<?> clazz) {
		boolean b = matcher.isMatch(clazz);
		NewRelic.getAgent().getLogger().log(Level.FINE, "Call to ClientClassMatcher is using class: {0} and classreader: {1} is returning {2}", clazz,b);
		return b;
	}

	@Override
	public Collection<String> getClassNames() {
		return matcher.getClassNames();
	}

}
