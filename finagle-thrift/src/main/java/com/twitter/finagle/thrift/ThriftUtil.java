package com.twitter.finagle.thrift;

import org.apache.thrift.protocol.TProtocolFactory;

import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.finagle.thrift.Utils;
import com.twitter.finagle.Service;
import com.twitter.finagle.stats.StatsReceiver;

import scala.Option;
import scala.collection.immutable.Map;

@Weave
public class ThriftUtil {


	public static Service<byte[], byte[]> serverFromIface(final Object impl, final TProtocolFactory protocolFactory, final String serviceName) {
		return Utils.getServiceWrapperWithStart(Weaver.callOriginal(), serviceName);
	}

	public static Service<byte[], byte[]> serverFromIfaces(final Map<String, Object> ifaces, final Option<String> defaultService, final TProtocolFactory protocolFactory, final StatsReceiver stats, final int maxThriftBufferSize, final String label) {
		String metricName = null;
		if(defaultService.isDefined()) {
			String serviceName = defaultService.get();
			metricName = serviceName + "/" + label;
		} else {
			metricName = label;
		}

		return Utils.getServiceWrapperWithStart(Weaver.callOriginal(), metricName);
	}

	public static Service<byte[], byte[]> serverFromIfaces(final Map<String, Object> ifaces, final Option<String> defaultService, final RichServerParam serverParam) {
		String metricName = null;
		String label = serverParam.serviceName();
		if(defaultService.isDefined()) {
			String serviceName = defaultService.get();
			metricName = serviceName + "/" + label;
		} else {
			metricName = label;
		}

		return Utils.getServiceWrapperWithStart(Weaver.callOriginal(), metricName);
	}

	public static Service<byte[], byte[]> serverFromIface(final Object impl, final TProtocolFactory protocolFactory, final StatsReceiver stats, final int maxThriftBufferSize, final String label) { 
		String metricName = label;

		return Utils.getServiceWrapperWithStart(Weaver.callOriginal(), metricName);
		
	}
	
	public static Service<byte[], byte[]> serverFromIface(final Object impl, final RichServerParam serverParam) {
		String metricName = serverParam.serviceName();

		return Utils.getServiceWrapperWithStart(Weaver.callOriginal(), metricName);
	}
}
