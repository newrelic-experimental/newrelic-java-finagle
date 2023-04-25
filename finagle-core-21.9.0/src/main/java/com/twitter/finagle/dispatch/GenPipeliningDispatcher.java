package com.twitter.finagle.dispatch;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.finagle.core.Utils;
import com.twitter.util.Future;
import com.twitter.util.Promise;
import com.twitter.util.Try;

@Weave(type = MatchType.BaseClass)
public abstract class GenPipeliningDispatcher<Req, Rep, In, Out, T> {

	@Trace
	public Future<T> pipeline(Req request, Promise<Rep> p) {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","GenPipeliningDispatcher",getClass().getSimpleName(),"pipeline");
		return Utils.addNRListener(Weaver.callOriginal(), "GenPipeliningDispatcher/pipeline");
	}
	
	@Trace(dispatcher = true)
	public void respond(T t, Promise<Rep> p, Try<Out> tryOut) {
		Weaver.callOriginal();
	}
}
