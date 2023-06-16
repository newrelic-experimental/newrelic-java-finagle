package com.twitter.finagle.builder;

import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.finagle.core.NewRelicServiceWrapper;
import com.twitter.finagle.Service;
import com.twitter.finagle.Stack;
import com.twitter.finagle.param.Label;

@Weave(type = MatchType.BaseClass)
public abstract class ClientBuilder<Req, Rep, HasCluster, HasCodec, HasHostConnectionLimit> {

	/*
	 * Called when a new service is constructed.  We wrap the resulting service with our wrapper service
	 */
	public Service<Req, Rep> build(com.twitter.finagle.builder.ClientConfigEvidence<HasCluster, HasCodec, HasHostConnectionLimit> c) {
		Service<Req, Rep> service = Weaver.callOriginal();
		Stack.Params params = params();
		boolean value = params.contains(Label.param());
		NewRelicServiceWrapper<Req, Rep> wrapper = new NewRelicServiceWrapper<>(service);
		if(value) {
			Label label = params.apply(Label.param());
			wrapper.setLabel(label.label());
			
		}
		return wrapper;
	}
	
	public abstract Stack.Params params();
}
