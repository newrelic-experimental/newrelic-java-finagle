package com.twitter.finagle.http.codec;

import java.util.HashMap;

import com.newrelic.api.agent.ExternalParameters;
import com.newrelic.api.agent.NewRelic;
//import java.net.URI;
//import java.net.URL;
//
//import com.newrelic.api.agent.HttpParameters;
//import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.finagle.http.FinagleHttpUtils;
import com.newrelic.instrumentation.finagle.http.NRFailureFunction;
import com.newrelic.instrumentation.finagle.http.NRFinagleHeaders;
import com.newrelic.instrumentation.finagle.http.NRSuccessFunction;
import com.newrelic.instrumentation.finagle.http.NewRelicHolder;
import com.newrelic.instrumentation.finagle.http.Utils;
//import com.newrelic.instrumentation.finagle.http.NRFailureFunction;
//import com.newrelic.instrumentation.finagle.http.NRFinagleHeaders;
//import com.newrelic.instrumentation.finagle.http.NRSuccessFunction;
//import com.newrelic.instrumentation.finagle.http.NewRelicHolder;
//import com.twitter.finagle.http.Method;
import com.twitter.finagle.http.Request;
import com.twitter.finagle.http.Response;
import com.twitter.util.Future;
import com.twitter.util.Promise;

import scala.runtime.BoxedUnit;

@Weave
public abstract class HttpClientDispatcher {

	@Trace
	public Future<BoxedUnit> dispatch(Request req, Promise<Response> promise) {
		// add attributes about the request being sent to the span
		HashMap<String, Object> attributes = new HashMap<>();
		Utils.addRequest(attributes, req);
		NewRelic.getAgent().getTracedMethod().addCustomAttributes(attributes);
		
		// insert distributed tracing headers into the request
		NRFinagleHeaders headers = new NRFinagleHeaders(req);
		NewRelic.getAgent().getTransaction().insertDistributedTraceHeaders(headers);
		NewRelicHolder holder = new NewRelicHolder("HttpClientDispatcher");
		ExternalParameters params = FinagleHttpUtils.getExternalFromRequest(req);
		if(params != null) {
			holder.addExternal(params);
		}
		Future<Response> future = promise.onSuccess(new NRSuccessFunction<Response>(holder)).onFailure(new NRFailureFunction(holder));
		promise.become(future);
		return Weaver.callOriginal();
	}
}
