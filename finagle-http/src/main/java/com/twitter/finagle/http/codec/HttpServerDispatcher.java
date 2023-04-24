package com.twitter.finagle.http.codec;

import java.util.HashMap;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.finagle.http.Utils;
import com.twitter.finagle.http.Request;
import com.twitter.finagle.http.Response;
import com.twitter.util.Future;

import scala.runtime.BoxedUnit;

@Weave
public abstract class HttpServerDispatcher {

	@Trace
	public Future<Response> dispatch(Request req) {
		HashMap<String, Object> attributes = new HashMap<>();
		Utils.addRequest(attributes, req);
		NewRelic.getAgent().getTracedMethod().addCustomAttributes(attributes);
		return Weaver.callOriginal();
	}
	
	@Trace
	public Future<BoxedUnit> handle(Response resp) {
		HashMap<String, Object> attributes = new HashMap<>();
		Utils.addResponse(attributes, resp);
		NewRelic.getAgent().getTracedMethod().addCustomAttributes(attributes);
		return Weaver.callOriginal();
	}
}
