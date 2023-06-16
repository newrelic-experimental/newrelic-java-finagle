package com.newrelic.instrumentation.finagle.core;

import com.newrelic.api.agent.NewRelic;

import scala.Function1;
import scala.runtime.BoxedUnit;

/*
 * Function called when the future generates an exception.
 * 
 */
public class NRFailureFunction implements Function1<Throwable, BoxedUnit> {
	
	private NewRelicHolder holder = null;
	
	public NRFailureFunction(NewRelicHolder h) {
		holder = h;
	}

	@Override
	public BoxedUnit apply(Throwable v1) {
		// Reports the exception and ends the segment
		NewRelic.noticeError(v1);
		if(holder != null) {
			holder.endSegment();
		}
		return BoxedUnit.UNIT;
	}

}
