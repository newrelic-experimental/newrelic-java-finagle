package com.twitter.finagle.dispatch;

import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.finagle.core.Utils;
import com.twitter.util.Future;
import com.twitter.util.Promise;

import scala.runtime.BoxedUnit;

@Weave(type = MatchType.BaseClass)
public class GenSerialServerDispatcher<Req,Rep,In,Out> {

	@Trace
	public Future<Rep> dispatch(Out out, Promise<BoxedUnit> p) {
		return Utils.addNRListener(Weaver.callOriginal(), "GenSerialServerDispatcher/dispatch");
	}
	
	@Trace(dispatcher = true)
	public Future<BoxedUnit> handle(Rep resp) {
		return Utils.addNRListener(Weaver.callOriginal(), "GenSerialServerDispatcher/handle");
	}
}
