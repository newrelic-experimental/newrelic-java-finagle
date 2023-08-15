package com.twitter.finagle.netty4.decoder;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

import io.netty.channel.ChannelHandlerContext_instrumentation;

@Weave
public abstract class DecoderHandler<T> {

	@Trace(dispatcher = true)
	public void channelRead(ChannelHandlerContext_instrumentation ctx, Object msg) {
		NewRelic.getAgent().getTracedMethod().addCustomAttribute("Message-Class", msg.getClass().getName());
		Weaver.callOriginal();
	}
}
