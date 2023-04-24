package com.twitter.finagle.dispatch;

import java.util.logging.Level;

import com.newrelic.api.agent.NewRelic;
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
		NewRelic.getAgent().getLogger().log(Level.FINE, "Call to {0}.dispatch(), req = {1}",getClass().getName(),req.getClass().getName());
		return Weaver.callOriginal();
	}
}
