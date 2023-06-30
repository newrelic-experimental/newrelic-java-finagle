package com.twitter.finagle.http.codec;

import java.util.HashMap;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.Transaction;
import com.newrelic.api.agent.TransportType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.finagle.http.NRFinagleHeaders;
import com.newrelic.instrumentation.finagle.http.Utils;
import com.twitter.finagle.http.Request;
import com.twitter.finagle.http.Response;
import com.twitter.util.Future;

import scala.runtime.BoxedUnit;

@Weave
public abstract class HttpServerDispatcher {
	
	@Trace(dispatcher = true)
	public Future<Response> dispatch(Request req) {
		// add attributes about the request to the span
		HashMap<String, Object> attributes = new HashMap<>();
		Utils.addRequest(attributes, req);
		NewRelic.getAgent().getTracedMethod().addCustomAttributes(attributes);
		
		// process incoming distributed tracing headers if they exist
		NRFinagleHeaders headers = new NRFinagleHeaders(req);
		NewRelic.getAgent().getTransaction().acceptDistributedTraceHeaders(TransportType.HTTP, headers);
		Transaction transaction = NewRelic.getAgent().getTransaction();
		if(!transaction.isWebTransaction()) {
			transaction.convertToWebTransaction();
		}
		return Weaver.callOriginal();
	}
	
	@Trace
	public Future<BoxedUnit> handle(Response resp) {
		// add attributes about the response to the span
		HashMap<String, Object> attributes = new HashMap<>();
		Utils.addResponse(attributes, resp);
		NewRelic.getAgent().getTracedMethod().addCustomAttributes(attributes);
		
		// insert the distributed tracking headers into the response.  need due to async processing on client side
		NRFinagleHeaders headers = new NRFinagleHeaders(resp);
		NewRelic.getAgent().getTransaction().insertDistributedTraceHeaders(headers);
		return Weaver.callOriginal();
	}
}
