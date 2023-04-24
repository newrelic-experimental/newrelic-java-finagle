package com.twitter.finagle;

import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.finagle.core.Utils;

@Weave(type = MatchType.Interface)
public abstract class Client<Req,Rep> {

	public Service<Req,Rep> newService(Name name, String label) {
		String n = Name.showable().show(name);
		String metricName = null;
		if(n != null && !n.isEmpty()) {
			metricName = n + "/" + label;
		} else {
			metricName = label;
		}
		
		Service<Req,Rep>  newService = Weaver.callOriginal();
		return Utils.getServiceWrapper(newService, metricName);
	}
	
	public Service<Req,Rep> newService( String dest) {
		Service<Req,Rep>  newService = Weaver.callOriginal();
		return Utils.getServiceWrapper(newService, dest);
	}
}
