package com.twitter.finagle.netty4.http;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.labs.finagle.netty.FinagleHeaders;
import com.twitter.finagle.http.Response;
import com.twitter.util.Future;

import scala.runtime.BoxedUnit;

@Weave
public abstract class Netty4ServerStreamTransport {
	
	@Trace(dispatcher = true)
	public Future<BoxedUnit> write(Response response) {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","Finagle","Netty4ServerStreamTransport","writeResponse");
		FinagleHeaders headers = new FinagleHeaders(response);
		NewRelic.getAgent().getTransaction().insertDistributedTraceHeaders(headers);
		return Weaver.callOriginal();
	}
}
