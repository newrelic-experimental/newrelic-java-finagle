package com.twitter.finagle;

import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.finagle.http.FinagleHttpUtils;
import com.newrelic.instrumentation.finagle.http.NRFailureFunction;
import com.newrelic.instrumentation.finagle.http.NRSuccessFunction;
import com.newrelic.instrumentation.finagle.http.NewRelicHolder;
import com.twitter.finagle.http.Request;
import com.twitter.util.Future;

@Weave(type = MatchType.BaseClass)
public abstract class Service<Req, Res> {

	@NewField
	public String label;

	public Future<Res> apply(Req request) {
		Future<Res> result = Weaver.callOriginal();
		if(request instanceof Request) {
			String segmentName = null;
			if(label != null && !label.isEmpty()) {
				segmentName = "Finagle/Service/" + label;
			} else {
				if(!FinagleHttpUtils.ignore(getClass().getName())) {
					segmentName = "Finagle/Service/" + getClass().getName();
				}
			}
			// construct holder to hold the segment and is called to end the segment
			if(segmentName != null) {
				NewRelicHolder holder = new NewRelicHolder(segmentName);

				return result.onSuccess(new NRSuccessFunction<>(holder)).onFailure(new NRFailureFunction(holder));
			}
		}

		return result;
	}
}
