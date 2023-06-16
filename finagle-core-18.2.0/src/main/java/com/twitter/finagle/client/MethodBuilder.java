package com.twitter.finagle.client;

import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.finagle.core.NewRelicServiceWrapper;
import com.twitter.finagle.Service;

import scala.Option;

@Weave
public abstract class MethodBuilder<Req, Res> {

	/*
	 * Called when a new service is constructed.  We wrap the resulting service with our wrapper service
	 */
	@SuppressWarnings("unused")
	private Service<Req, Res> newService(Option<String> name) {
		Service<Req, Res> service = Weaver.callOriginal();
		NewRelicServiceWrapper<Req, Res> wrapper = new NewRelicServiceWrapper<>(service);

		String destName = clientName();

		if(destName != null && !destName.isEmpty()) {
			wrapper.setLabel(destName);
		}

		return wrapper;
	}

	private String clientName() {
		return Weaver.callOriginal();
	}
}
