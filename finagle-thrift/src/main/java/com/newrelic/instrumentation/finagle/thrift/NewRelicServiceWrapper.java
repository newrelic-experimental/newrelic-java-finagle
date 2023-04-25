package com.newrelic.instrumentation.finagle.thrift;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.twitter.finagle.Service;
import com.twitter.util.Future;

public class NewRelicServiceWrapper<Req, Rep> extends Service<Req, Rep> {
	
	private Service<Req, Rep> delegate = null;
	private String name = null;
	private static boolean isTransformed = false;
	private Map<String, Object> attributes = null;
	
	public NewRelicServiceWrapper(Service<Req, Rep> d) {
		delegate = d;
		if(!isTransformed) {
			AgentBridge.instrumentation.retransformUninstrumentedClass(getClass());
			isTransformed = true;
		}
	}
	
	public void addAttribute(String key, Object value) {
		if(attributes == null) attributes = new HashMap<>();
		attributes.put(key, value);
	}
	
	public boolean isNameSet() {
		return name != null;
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
		NewRelic.getAgent().getTracedMethod().addCustomAttribute("Delegate", delegate.getClass().getName());
		if(attributes != null) NewRelic.getAgent().getTracedMethod().addCustomAttributes(attributes);
		return delegate.apply(request);
	}

}
