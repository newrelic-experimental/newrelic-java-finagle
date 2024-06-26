package com.newrelic.instrumentation.finagle.http.tracing;

import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import com.newrelic.agent.deps.org.objectweb.asm.commons.Method;
import com.newrelic.agent.instrumentation.classmatchers.ClassAndMethodMatcher;
import com.newrelic.agent.instrumentation.classmatchers.OptimizedClassMatcher.Match;
import com.newrelic.agent.instrumentation.classmatchers.OptimizedClassMatcherBuilder;
import com.newrelic.agent.instrumentation.context.ClassMatchVisitorFactory;
import com.newrelic.agent.instrumentation.context.ContextClassTransformer;
import com.newrelic.agent.instrumentation.context.InstrumentationContext;
import com.newrelic.agent.instrumentation.context.InstrumentationContextManager;
import com.newrelic.agent.instrumentation.methodmatchers.MethodMatcher;
import com.newrelic.agent.instrumentation.tracing.TraceDetailsBuilder;
import com.newrelic.api.agent.Logger;
import com.newrelic.api.agent.NewRelic;

public class ClientClassTransformer implements ContextClassTransformer {

	private Map<String,ClassMatchVisitorFactory> matchers = null;
	private final InstrumentationContextManager contextMgr;

	public ClientClassTransformer(InstrumentationContextManager mgr) {
		contextMgr = mgr;
		matchers = new HashMap<>();
	}

	protected ClassMatchVisitorFactory addMatcher(ClassAndMethodMatcher matcher) {
		NewRelic.getAgent().getLogger().log(Level.FINE, "Adding matcher {0} to classtransformer", matcher);
		OptimizedClassMatcherBuilder builder = OptimizedClassMatcherBuilder.newBuilder();
		builder.addClassMethodMatcher(matcher);
		ClassMatchVisitorFactory matchVistor = builder.build();
		matchers.put(matcher.getClass().getSimpleName(), matchVistor);
		contextMgr.addContextClassTransformer(matchVistor, this);
		return matchVistor;
	}
	
    protected void removeMatcher(ClassAndMethodMatcher matcher) {
    	ClassMatchVisitorFactory matchVisitor = matchers.remove(matcher.getClass().getSimpleName());
    	if(matchVisitor != null) {
    		contextMgr.removeMatchVisitor(matchVisitor);
    	}
    }


	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer, InstrumentationContext context, Match match)
					throws IllegalClassFormatException {
		Logger logger = NewRelic.getAgent().getLogger();
		logger.log(Level.FINE, "Call to ClientClassTransformer.transform for class {0}",className);
		for(Method method : match.getMethods()) {
			for(ClassAndMethodMatcher matcher : match.getClassMatches().keySet()) {
				if (matcher.getMethodMatcher().matches(MethodMatcher.UNSPECIFIED_ACCESS, method.getName(),
						method.getDescriptor(), match.getMethodAnnotations(method))) {
					context.putTraceAnnotation(method, TraceDetailsBuilder.newBuilder().setTracerFactoryName(FinagleCoreClientTracerFactory.TRACER_FACTORY_NAME).setInstrumentationSourceName("FinagleCore").build());
					logger.log(Level.FINE, "Add tracer factory to class {0} and method {1}", className,method);
				}

			}
		}

		return null;
	}

}
