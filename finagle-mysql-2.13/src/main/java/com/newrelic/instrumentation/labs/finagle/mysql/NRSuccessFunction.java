package com.newrelic.instrumentation.labs.finagle.mysql;

import scala.Function1;
import scala.runtime.BoxedUnit;

/*
 * Called when the future ends successfully
 */
public class NRSuccessFunction<A> implements Function1<A, BoxedUnit> {
	
	private NewRelicHolder holder = null;
	
	public NRSuccessFunction(NewRelicHolder h) {
		holder = h;
	}

	@Override
	public BoxedUnit apply(A v1) {
		// end the segment
		if(holder != null) {
			holder.endSegment();
		}
		return BoxedUnit.UNIT;
	}

}
