package com.newrelic.instrumentation.finagle.http;

import com.twitter.finagle.ClientConnection;
import com.twitter.finagle.Service;
import com.twitter.finagle.ServiceFactory;
import com.twitter.finagle.Status;
import com.twitter.finagle.http.Request;
import com.twitter.finagle.http.Response;
import com.twitter.util.Duration;
import com.twitter.util.Future;
import com.twitter.util.Time;

import scala.Function1;
import scala.runtime.BoxedUnit;

public class NewRelicServiceFactoryWrapper extends ServiceFactory<Request, Response> {
	
	private ServiceFactory<Request, Response> delegate = null;
	private String label = null;
	
	public NewRelicServiceFactoryWrapper(ServiceFactory<Request, Response> actual) {
		delegate = actual;
	}
	
	public void setLabel(String l) {
		label = l;
	}

	@Override
	public Future<BoxedUnit> close(Time deadline) {
		return delegate.close(deadline);
	}

	@Override
	public <A> Function1<ClientConnection, A> andThen(Function1<Future<Service<Request, Response>>, A> g) {
		return delegate.andThen(g);
	}

	@Override
	public Future<Service<Request, Response>> apply(ClientConnection conn) {
		return delegate.apply(conn);
	}

	@Override
	public Future<BoxedUnit> close(Duration after) {
		return delegate.close(after);
	}

	@Override
	public <A> Function1<A, Future<Service<Request, Response>>> compose(Function1<A, ClientConnection> g) {
		return delegate.compose(g);
	}

	@Override
	public <Req1, Rep1> ServiceFactory<Req1, Rep1> flatMap(Function1<Service<Request, Response>, Future<Service<Req1, Rep1>>> f) {
		NewRelicFlatMapFunction<Req1, Rep1>  wrapperFn = new NewRelicFlatMapFunction<>(f);
		if(label != null) {
			wrapperFn.setLabel(label);
		}
		return delegate.flatMap(new NewRelicFlatMapFunction<Req1, Rep1>(f));
	}

	@Override
	public <Req1, Rep1> ServiceFactory<Req1, Rep1> map(Function1<Service<Request, Response>, Service<Req1, Rep1>> f) {
		return delegate.map(f);
	}

	@Override
	public Status status() {
		return delegate.status();
	}

	@Override
	public String toString() {
		return delegate.toString();
	}


}
