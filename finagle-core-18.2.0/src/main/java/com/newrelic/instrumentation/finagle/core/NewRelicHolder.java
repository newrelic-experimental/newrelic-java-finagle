package com.newrelic.instrumentation.finagle.core;

import java.util.Map;

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
	private Map<String, Object> attributes = null;
	
	public NewRelicHolder(String segmentName) {
		segment = NewRelic.getAgent().getTransaction().startSegment(segmentName);
	}
	
	public void endSegment() {
		if(segment != null) {
			if(attributes != null) {
				segment.addCustomAttributes(attributes);
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

	public void addAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
}
