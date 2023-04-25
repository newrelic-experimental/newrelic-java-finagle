package com.twitter.finagle.thrift;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;

import com.newrelic.api.agent.ExternalParameters;
import com.newrelic.api.agent.GenericParameters;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.finagle.thrift.Utils;
import com.twitter.finagle.stats.StatsReceiver;
import com.twitter.finagle.transport.Transport;
import com.twitter.util.Future;
import com.twitter.util.Promise;

import scala.runtime.BoxedUnit;

@Weave
public abstract class ThriftSerialClientDispatcher {
	
	@NewField
	ExternalParameters extParams;

	@SuppressWarnings("deprecation")
	public ThriftSerialClientDispatcher(Transport<ThriftClientRequest,byte[]> trans, StatsReceiver statsReceiver) {
		SocketAddress address = trans.remoteAddress();
		if(address instanceof InetSocketAddress) {
			InetSocketAddress iAddress = (InetSocketAddress)address;
			String uriString = "finagle-thrift://" + iAddress.getHostName() + ":" + iAddress.getPort();
			URI uri = URI.create(uriString);
			extParams = GenericParameters.library("Finagle-Thrift").uri(uri).procedure("dispatch").build();
		}
	}
	
	@Trace
	public Future<BoxedUnit> dispatch(ThriftClientRequest req, Promise<Promise<byte[]>> p) {
		
		
		return Utils.addNRListener(Weaver.callOriginal(), "ThriftSerialClientDispatcher", extParams);
	}
}
