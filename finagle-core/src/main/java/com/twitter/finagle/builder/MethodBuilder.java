package com.twitter.finagle.builder;

import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.finagle.core.Utils;
import com.twitter.finagle.Service;

import scala.Option;

public class MethodBuilder<Req, Rep> {

	@SuppressWarnings("unused")
	private Service<Req,Rep> wrappedService(Option<String> name) {
		String n = null;
		Service<Req,Rep> service = Weaver.callOriginal();
		if(name.isDefined()) {
			return Utils.getServiceWrapper(service, n);
		}
		return Utils.getServiceWrapper(service);
		
	}
}
