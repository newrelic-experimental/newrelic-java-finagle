package com.twitter.finagle.dispatch;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.finagle.core.Utils;
import com.twitter.util.Future;
import com.twitter.util.Promise;

import scala.runtime.BoxedUnit;

@Weave(type = MatchType.BaseClass)
public abstract class GenSerialClientDispatcher<Req, Rep, In, Out> {

	@Trace
	public Future<BoxedUnit> dispatch(Req req, Promise<Rep> promise) {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","ClientDispatcher",getClass().getSimpleName(),"dispatch");
		return Utils.addNRListener(Weaver.callOriginal(), "GenSerialClientDispatcher") ;
	}
}
