package com.newrelic.instrumentation.finagle.core;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;

/*
 * Class to hold the segment since it needs to be stopped in two possible places, 
 * first if it is successful and the second if results in an error
 * It can also be passed an ExternalParameters object if the segment is to be 
 * reported as external
 */
public class NewRelicHolder {

	private Segment segment = null;
	
	public NewRelicHolder(String segmentName) {
		segment = NewRelic.getAgent().getTransaction().startSegment(segmentName);
	}
	
	public void endSegment() {
		if(segment != null) {
			segment.end();
			segment = null;
		}
	}
	
	/*
	 * not used but added if we need to cancel the segment at some point
	 */
	public void ignoreSegment() {
		if(segment != null) {
			segment.ignore();
			segment = null;
		}
	}
	
}
