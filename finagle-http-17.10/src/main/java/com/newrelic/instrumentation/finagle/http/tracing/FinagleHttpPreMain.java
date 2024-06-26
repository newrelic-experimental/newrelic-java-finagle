package com.newrelic.instrumentation.finagle.http.tracing;

import java.lang.instrument.Instrumentation;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import com.newrelic.agent.InstrumentationProxy;
import com.newrelic.agent.TracerService;
import com.newrelic.agent.core.CoreService;
import com.newrelic.agent.instrumentation.ClassTransformerService;
import com.newrelic.agent.instrumentation.context.ClassMatchVisitorFactory;
import com.newrelic.agent.instrumentation.context.ContextClassTransformer;
import com.newrelic.agent.instrumentation.context.InstrumentationContextManager;
import com.newrelic.agent.service.ServiceFactory;
import com.newrelic.api.agent.Logger;
import com.newrelic.api.agent.NewRelic;

public class FinagleHttpPreMain {

	private static int max_retries = 20;
	private static int retries = 0;
	private static ScheduledExecutorService executor = null;
	
	public static void premain(String args, Instrumentation inst) {
		initialize();
	}
	
	public static void initialize() {
		boolean b = setup();
		if(!b) {
			executor = Executors.newSingleThreadScheduledExecutor();
			executor.schedule(new Setup(), 100L, TimeUnit.MILLISECONDS);
		}
	}
	
	public static boolean setup() {
		Logger logger = NewRelic.getAgent().getLogger();
		logger.log(Level.FINE, "Call to initiate Finagle Client ClassTransformer");
		TracerService tracerService = ServiceFactory.getTracerService();
		ClassTransformerService classTransformationService = ServiceFactory.getClassTransformerService();
		CoreService coreService = ServiceFactory.getCoreService();
		
		if(tracerService != null && classTransformationService != null && coreService != null) {
			logger.log(Level.FINE, "Found all three services needed to create classtransformer");
			tracerService.registerTracerFactory(FinagleCoreClientTracerFactory.TRACER_FACTORY_NAME, new FinagleCoreClientTracerFactory());
			logger.log(Level.FINE, "Registered tracer factory");
			InstrumentationContextManager contextMgr = classTransformationService.getContextManager();
			
			logger.log(Level.FINE, "Got context manager: {0}",contextMgr);
			if(contextMgr != null) {
				ClientClassTransformer transformer = new ClientClassTransformer(contextMgr);
				logger.log(Level.FINE, "Createdtrace classtransformer: {0}",transformer);
				ClientClassAndMethodMatcher matcher = new ClientClassAndMethodMatcher();
				ClassMatchVisitorFactory matchVistor = transformer.addMatcher(matcher);
				
				logger.log(Level.FINE, "Added matcher: {0} and created ClassMatchVisitorFactory: {1}",matcher,matchVistor);
				Set<ClassMatchVisitorFactory> factories = new HashSet<>();
				factories.add(matchVistor);
				NewRelic.getAgent().getLogger().log(Level.FINE, "Finagle Http Client ClassTransformer has been started");
				Map<ClassMatchVisitorFactory, ContextClassTransformer> allMatchers = contextMgr.getMatchVisitors();
				logger.log(Level.FINE, "There are {0} entries in the match visitors map", allMatchers.size());
				for(ClassMatchVisitorFactory factory : allMatchers.keySet()) {
					ContextClassTransformer factoryTransformer = allMatchers.get(factory);
					logger.log(Level.FINE, "\tMapping visitor factory {0} to class transformer {1}",factory,factoryTransformer);
				}
				Class<?>[] allLoadedClasses = ServiceFactory.getCoreService().getInstrumentation().getAllLoadedClasses();
				classTransformationService.retransformMatchingClassesImmediately(allLoadedClasses, factories);
				return true;
			}
		}
		logger.log(Level.FINE, "Setup failed");
		return false;
	}
	
	private static class Setup implements Runnable {
		
		private static int count = 0;

		@Override
		public void run() {
			count++;
			NewRelic.getAgent().getLogger().log(Level.FINE, "Call {0} to attempt setting up Finagle ClassTransformer",count);
			boolean b = setup();
			
			if(!b) {
				if(count < max_retries) {
					executor.schedule(this, 2L, TimeUnit.SECONDS);
				} else {
					NewRelic.getAgent().getLogger().log(Level.FINE, "Failed to initiate Finagle Client Transformer after {0} tries", max_retries);
					executor.shutdownNow();
				}
			}

		}
		
	}
}
