package com.twitter.finagle;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Transaction;
import com.newrelic.api.agent.TransactionNamePriority;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.finagle.http.NRFutureEventListener;
import com.newrelic.instrumentation.finagle.http.NewRelicHolder;
import com.newrelic.instrumentation.finagle.http.ServiceNames;
import com.twitter.util.Future;

@Weave(type = MatchType.BaseClass)
public abstract class Service<Req, Res> {

	public Future<Res> apply(Req request) {
		String serviceName = ServiceNames.getServiceName(this);
		NewRelicHolder holder = null;
		if(serviceName != null) {
			holder = new NewRelicHolder("Custom/Finagle/Service/"+serviceName);
			Transaction transaction = NewRelic.getAgent().getTransaction();
			transaction.setTransactionName(TransactionNamePriority.FRAMEWORK_HIGH, false, "Finagle-Service", "Finagle","Service",serviceName);
		}
		Future<Res> f = Weaver.callOriginal();
		if(holder != null) {
			return f.addEventListener(new NRFutureEventListener<>(holder, request));
		}
		return f;
	}
}
