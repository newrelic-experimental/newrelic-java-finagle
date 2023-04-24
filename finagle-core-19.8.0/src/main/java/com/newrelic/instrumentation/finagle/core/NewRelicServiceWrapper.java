package com.newrelic.instrumentation.finagle.core;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.twitter.finagle.Service;
import com.twitter.util.Future;

public class NewRelicServiceWrapper<Req, Rep> extends Service<Req, Rep> {
	
	private Service<Req, Rep> delegate = null;
	private String name = null;
	private static boolean isTransformed = false;
	
	public NewRelicServiceWrapper(Service<Req, Rep> d) {
		delegate = d;
		if(!isTransformed) {
			AgentBridge.instrumentation.retransformUninstrumentedClass(getClass());
			isTransformed = true;
		}
	}
	
	public void setName(String n) {
		name = n;
	}

	@Override
	@Trace
	public Future<Rep> apply(Req request) {
		if(name != null && !name.isEmpty()) {
			NewRelic.getAgent().getTracedMethod().setMetricName("Custom","Service",name);
		}
		return delegate.apply(request);
	}

}
