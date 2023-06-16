package com.twitter.finagle.http;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.TransactionNamePriority;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

import scala.Option;

@Weave
public abstract class HttpMuxer {
	
	public Option<Route> route(Request req) {
		
		Option<Route> option = Weaver.callOriginal();
		
		/*
		 * if route is defined then use its pattern to name the transaction
		 */
		if(option.isDefined()) {
			Route r = option.get();
			if(r != null) {
				String p = r.pattern();
				if(p != null && !p.isEmpty()) {
					if(p.startsWith("/")) p = p.substring(1);
					NewRelic.getAgent().getTransaction().setTransactionName(TransactionNamePriority.FRAMEWORK_HIGH, false, "Finagle-HTTP", "Finagle","HTTPRequest",p);
				}
			}
		}
		
		return option;
	}
}
