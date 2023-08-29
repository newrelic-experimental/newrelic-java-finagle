package com.newrelic.instrumentation.finagle.core;

import java.util.HashMap;
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
		Map<String, Object> attributes = new HashMap<>();
		Utils.addAttribute(attributes, "Returned Object", value.getClass().getName());
		Utils.addAttribute(attributes, "InputObject", object.getClass().getName());
		holder.addAttributes(attributes);
		if(holder != null) {
			holder.endSegment();
		}
	}

}
