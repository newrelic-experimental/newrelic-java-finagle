package com.newrelic.instrumentation.finagle.http;

import java.util.Map;

import com.newrelic.api.agent.NewRelic;
import com.twitter.util.FutureEventListener;

public class NRFutureEventListener<T> implements FutureEventListener<T> {
	
	private NewRelicHolder holder = null;
	private Object object = null;
	
	public NRFutureEventListener(NewRelicHolder h, Object obj) {
		holder = h;
		object = obj;
	}

	@Override
	public void onFailure(Throwable cause) {
		NewRelic.noticeError(cause);
		if(holder != null) {
			holder.endSegment();
		}
	}

	@Override
	public void onSuccess(T value) {
		Map<String, Object> attributes = Utils.getAttributes(object);
		if(attributes != null) {
			holder.addAttributes(attributes);
		}
		attributes = Utils.getAttributes(value);
		if(attributes != null) {
			holder.addAttributes(attributes);
		}
		if(holder != null) {
			holder.endSegment();
		}
	}

}
