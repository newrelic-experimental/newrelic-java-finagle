package com.twitter.finagle.dispatch;

import java.util.logging.Level;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.finagle.core.Utils;
import com.twitter.finagle.transport.Transport;
import com.twitter.util.Future;
import com.twitter.util.Promise;

import scala.runtime.BoxedUnit;

@Weave(type = MatchType.BaseClass)
public class GenSerialServerDispatcher<Req,Rep,In,Out> {
	
	@NewField
	public Token token;
	
	public GenSerialServerDispatcher(Transport<In, Out> trans) {
		
	}

	@Trace(dispatcher = true)
	public Future<Rep> dispatch(Out out, Promise<BoxedUnit> p) {
		NewRelic.getAgent().getLogger().log(Level.FINE, "Call to {0}.dispatch(), out = {1},promise = {2}" , getClass().getName(), out, p);
		Token t = NewRelic.getAgent().getTransaction().getToken();
		if(t != null && t.isActive()) {
			token = t;
		} else if(t != null) {
			t.expire();
		}
		return Utils.addNRListener(Weaver.callOriginal(), "GenSerialServerDispatcher/dispatch");
	}
	
	@Trace(async = true)
	public Future<BoxedUnit> handle(Rep resp) {
		NewRelic.getAgent().getLogger().log(Level.FINE, "Call to {0}.handle(), Rep = {1}" , getClass().getName(), resp);
		if(token != null) {
			token.linkAndExpire();
			token = null;
		}
		return Utils.addNRListener(Weaver.callOriginal(), "GenSerialServerDispatcher/handle");
	}
}
