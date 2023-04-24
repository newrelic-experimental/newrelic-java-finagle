package com.twitter.finagle.http.codec;

import java.net.URI;
import java.net.URL;

import com.newrelic.api.agent.HttpParameters;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.finagle.http.NRFinagleHeaders;
import com.newrelic.instrumentation.finagle.http.NRFutureEventListener;
import com.twitter.finagle.http.Method;
import com.twitter.finagle.http.Request;
import com.twitter.finagle.http.Response;
import com.twitter.util.Future;
import com.twitter.util.Promise;

import scala.runtime.BoxedUnit;

@Weave
public abstract class HttpClientDispatcher {

	@Trace(dispatcher = true)
	public Future<BoxedUnit> dispatch(Request req, Promise<Response> promise) {
		NRFinagleHeaders headers = new NRFinagleHeaders(req);
		NewRelic.getAgent().getTransaction().insertDistributedTraceHeaders(headers);
		String host = req.remoteHost();
		URI uri = null;
		int port = req.remotePort();
		try {
			URL url = new URL("http", host, port, req.path());
			uri = url.toURI();
		} catch (Exception e) {
		}
		if(uri == null) {
			uri = URI.create(req.uri());
		}
		Method method = req.method();
		String proc = method != null ? method.name() : "dispatch";
		HttpParameters params = HttpParameters.library("Finagle").uri(uri).procedure(proc).noInboundHeaders().build();
		Segment segment = NewRelic.getAgent().getTransaction().startSegment("HttpClientDispatch");
		
		
		Future<BoxedUnit> unit = Weaver.callOriginal();
		unit.addEventListener(new NRFutureEventListener<>(segment,params));
		return unit;
	}
}
