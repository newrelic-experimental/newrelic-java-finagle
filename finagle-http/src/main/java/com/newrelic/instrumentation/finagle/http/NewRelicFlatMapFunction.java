package com.newrelic.instrumentation.finagle.http;

import com.twitter.finagle.Service;
import com.twitter.finagle.http.Request;
import com.twitter.finagle.http.Response;
import com.twitter.util.Future;

import scala.Function1;

public class NewRelicFlatMapFunction<Req1, Rep1> implements Function1<Service<Request, Response>, Future<Service<Req1, Rep1>>> {
	
	private Function1<Service<Request, Response>, Future<Service<Req1, Rep1>>> actual = null;
	private String label = null;
	
	
	public NewRelicFlatMapFunction(Function1<Service<Request, Response>, Future<Service<Req1, Rep1>>> a) {
		actual = a;
	}
	
	public void setLabel(String l) {
		label = l;
	}

	@Override
	public Future<Service<Req1, Rep1>> apply(Service<Request, Response> v1) {
		if(!(v1 instanceof NewRelicServiceWrapper)) {
			NewRelicServiceWrapper wrapper = new NewRelicServiceWrapper(v1);
			if(label != null && !label.isEmpty()) {
				wrapper.setLabel(label);
			}
			v1 = wrapper;
		}
		return actual.apply(v1);
	}

}
