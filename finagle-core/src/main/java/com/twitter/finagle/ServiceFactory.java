package com.twitter.finagle;

import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.finagle.core.Utils;

@Weave(type = MatchType.BaseClass)
public class ServiceFactory<Req, Rep> {

	public Service<Req,Rep> toService() {
		Service<Req,Rep> service = Weaver.callOriginal();
//		return Utils.getServiceWrapper(service);
		return service;
	}
}
