package com.twitter.finagle.dispatch;

import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.twitter.util.Future;
import com.twitter.util.Promise;

import scala.runtime.BoxedUnit;

@Weave(type = MatchType.BaseClass)
public abstract class SerialServerDispatcher<Req, Rep> {

	
	@Trace(dispatcher = true) 
	public Future<Rep> dispatch(Req req, Promise<BoxedUnit> p) {
		return Weaver.callOriginal();
	}
}
