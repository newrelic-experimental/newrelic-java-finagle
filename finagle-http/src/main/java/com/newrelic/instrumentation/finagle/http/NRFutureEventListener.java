package com.newrelic.instrumentation.finagle.http;

import com.newrelic.api.agent.ExternalParameters;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;
import com.twitter.util.FutureEventListener;

public class NRFutureEventListener<T> implements FutureEventListener<T> {
	
	private Segment segment = null;
	private ExternalParameters params = null;
	
	public NRFutureEventListener(Segment seg) {
		segment = seg;
	}
	
	public NRFutureEventListener(Segment seg, ExternalParameters p) {
		segment = seg;
		params = p;
	}

	@Override
	public void onFailure(Throwable cause) {
		if(segment != null) {
			if(params != null) {
				segment.reportAsExternal(params);
			}
			segment.end();
		}
		NewRelic.noticeError(cause);
	}

	@Override
	public void onSuccess(T value) {
		if(segment != null) {
			if(params != null) {
				segment.reportAsExternal(params);
			}
			segment.end();
		}
	}

}
