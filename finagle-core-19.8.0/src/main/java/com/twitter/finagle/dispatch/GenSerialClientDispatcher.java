package com.twitter.finagle.dispatch;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;

import com.newrelic.api.agent.ExternalParameters;
import com.newrelic.api.agent.GenericParameters;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.finagle.core.Utils;
import com.twitter.finagle.stats.StatsReceiver;
import com.twitter.finagle.transport.Transport;
import com.twitter.util.Future;
import com.twitter.util.Promise;

import scala.runtime.BoxedUnit;

@Weave(type = MatchType.BaseClass)
public abstract class GenSerialClientDispatcher<Req, Rep, In, Out> {
	
	@NewField
	protected ExternalParameters extParams = null;

	
	public GenSerialClientDispatcher(Transport<In, Out> trans, StatsReceiver statsReceiver) {
		if(extParams == null) {
			@SuppressWarnings("deprecation")
			SocketAddress remoteAddr = trans.remoteAddress();
			if(remoteAddr instanceof InetSocketAddress) {
				InetSocketAddress iAddress = (InetSocketAddress)remoteAddr;
				String protocol = "finagle://";
				String host = iAddress.getHostName();
				int port = iAddress.getPort();
				String uriString;
				if(port > 0) {
					uriString = protocol + host + ":" + port;
				} else {
					uriString = protocol + host;
				}
				URI uri = URI.create(uriString);
				extParams = GenericParameters.library("Finagle").uri(uri).procedure("dispatch").build();
			}
		}
		
	}
	
	@Trace
	public Future<BoxedUnit> dispatch(Req req, Promise<Rep> promise) {
		Future<BoxedUnit> result = Weaver.callOriginal();
		if(extParams != null) {
			Future<BoxedUnit> resultWithListener = Utils.addNRListener(result, "GenSerialClientDispatcher",extParams) ;
			extParams = null;
			return resultWithListener;
		} else {
			return Utils.addNRListener(result, "GenSerialClientDispatcher");
		}
			
	}
}
