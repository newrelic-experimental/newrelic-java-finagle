package com.twitter.finagle.http.codec;

import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.twitter.finagle.http.Request;
import com.twitter.finagle.http.Response;
import com.twitter.util.Future;

@Weave
public abstract class HttpServerDispatcher {

	@Trace
	public Future<Response> dispatch(Request req) {
		return Weaver.callOriginal();
	}
}
