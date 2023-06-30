package com.newrelic.instrumentation.finagle.http;

import com.newrelic.api.agent.ExternalParameters;
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
	private ExternalParameters params;
	
	public NewRelicHolder(String segmentName) {
		segment = NewRelic.getAgent().getTransaction().startSegment(segmentName);
	}
	
	public void endSegment() {
		if(segment != null) {
			// report as external if needed
			if(params != null) {
				segment.reportAsExternal(params);
			}
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
	
	/*
	 * report as External
	 */
	public void addExternal(ExternalParameters p) {
		params = p;
		
	}
}
