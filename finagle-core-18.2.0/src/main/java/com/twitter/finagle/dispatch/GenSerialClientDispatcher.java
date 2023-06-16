package com.twitter.finagle.dispatch;

import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.twitter.util.Future;

@Weave(type = MatchType.BaseClass)
public abstract class GenSerialClientDispatcher<Req, Rep, In, Out, T> {
	
	@Trace(dispatcher = true)
	public Future<Rep> apply(Req request) {
		return Weaver.callOriginal();
	}

}
