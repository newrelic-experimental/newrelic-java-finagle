package io.netty.bootstrap;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.TracedMethod;
import com.newrelic.api.agent.TransportType;
import com.newrelic.instrumentation.labs.finagle.netty.FinagleNettyHeaders;

import io.netty.channel.ChannelHandlerContext_instrumentation;
import io.netty.handler.codec.http.HttpMessage;

public class FinagleNettyDispatcher {

	private static volatile FinagleNettyDispatcher instance = null;
	public static final AtomicBoolean instrumented = new AtomicBoolean(false);
	
	public static FinagleNettyDispatcher get() {
		if(instance == null) {
			synchronized (FinagleNettyDispatcher.class) {
				if(instance == null) {
					instance = new FinagleNettyDispatcher();
					instrumented.set(true);
				}
			}
		}
		return instance;
	}
	
	private FinagleNettyDispatcher() {
		AgentBridge.instrumentation.retransformUninstrumentedClass(FinagleNettyDispatcher.class);
	}
	
	@Trace(dispatcher = true)
	public static void channelRead(ChannelHandlerContext_instrumentation ctx, Object msg) {
		NewRelic.getAgent().getTracedMethod().addCustomAttribute("Message-Class", msg.getClass().getName());
		TracedMethod tracer = NewRelic.getAgent().getTracedMethod();
		if(tracer == null) {
			NewRelic.getAgent().getLogger().log(Level.FINER, "Unable to dispatch FingleNettyDispatcher, no tracer");
		} else {
			tracer.setMetricName("Custom","Finagle","HttpResponseReceived");
		}
		NewRelic.getAgent().getTransaction().convertToWebTransaction();
		if(msg instanceof HttpMessage) {
			FinagleNettyHeaders headers = new FinagleNettyHeaders((HttpMessage)msg);
			NewRelic.getAgent().getTransaction().acceptDistributedTraceHeaders(TransportType.HTTP, headers);
		}

	}
	
}
