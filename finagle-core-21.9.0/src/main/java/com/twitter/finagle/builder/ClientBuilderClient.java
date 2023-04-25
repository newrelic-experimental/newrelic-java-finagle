package com.twitter.finagle.builder;

import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.finagle.core.Utils;
import com.twitter.finagle.Name;
import com.twitter.finagle.Service;

@Weave
public abstract class ClientBuilderClient<Req, Rep> {

	
	public Service<Req, Rep> newService(Name name, String label) {
		String s  = Name.showable().show(name);
		String metricName;
		if(s != null && !s.isEmpty()) {
			metricName = s + "/" + label;
		} else {
			metricName = label;
		}
		return Utils.getServiceWrapper(Weaver.callOriginal(), metricName);
	}
	
	public Service<Req, Rep> newService(String name, String label) {
		String metricName;
		if(name != null && !name.isEmpty()) {
			metricName = name + "/" + label;
		} else {
			metricName = label;
		}
		return Utils.getServiceWrapper(Weaver.callOriginal(), metricName);
	}

	public Service<Req, Rep> newService(String name) {
		return Utils.getServiceWrapper(Weaver.callOriginal(), name);
	}

}
