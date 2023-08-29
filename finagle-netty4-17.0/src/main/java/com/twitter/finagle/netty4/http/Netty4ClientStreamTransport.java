package com.twitter.finagle.netty4.http;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.labs.finagle.netty.FinagleHeaders;
import com.twitter.finagle.http.Request;
import com.twitter.finagle.http.Response;
import com.twitter.finagle.http.exp.Multi;
import com.twitter.util.Future;

import scala.runtime.BoxedUnit;

@Weave
public abstract class Netty4ClientStreamTransport {

	@Trace
	public Future<Multi<Response>> read() {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","Finagle","Netty4ClientStreamTransport","readResponse");
		return Weaver.callOriginal();
	}
	
	@Trace
	public Future<BoxedUnit> write(Request request) {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","Finagle","Netty4ClientStreamTransport","writeRequest");
		FinagleHeaders headers = new FinagleHeaders(request);
		NewRelic.getAgent().getTransaction().insertDistributedTraceHeaders(headers);
		return Weaver.callOriginal();
	}
}
