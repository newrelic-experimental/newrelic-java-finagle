package com.twitter.finagle.client;

import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.finagle.core.Utils;
import com.twitter.finagle.Name;
import com.twitter.finagle.Service;

@Weave(type = MatchType.Interface)
public abstract class EndpointerStackClient<Req,Rep,This> {

	public Service<Req,Rep> newService(Name name, String label) {
		String n = Name.showable().show(name);
		String metricName;
		if(n != null && !n.isEmpty()) { 
			metricName = n + "/" + label;
		} else {
			metricName = label;
		}
		
		return Utils.getServiceWrapper(Weaver.callOriginal(), metricName);
	}
}
