package io.netty.handler.codec;

import java.util.List;

import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

import io.netty.bootstrap.FinagleNettyDispatcher;
import io.netty.channel.ChannelHandlerContext_instrumentation;
import io.netty.handler.codec.http.HttpResponse;

@Weave(type = MatchType.BaseClass)
public abstract class MessageToMessageCodec<INBOUND_IN, OUTBOUND_IN> {

	
	protected void decode(ChannelHandlerContext_instrumentation ctx, INBOUND_IN in, List<Object> list) {
		Weaver.callOriginal();
		
		for(Object obj : list) {
			if(obj instanceof HttpResponse && ctx.pipeline().finagleLayerToken == null) {
				//ctx.pipeline().finagleLayerToken = NewRelic.getAgent().getTransaction().getToken();
				if(!FinagleNettyDispatcher.instrumented.get()) {
					FinagleNettyDispatcher.get();
				}
				FinagleNettyDispatcher.channelRead(ctx, obj);
			}
		}
	}
	
	protected void encode(ChannelHandlerContext_instrumentation ctx, OUTBOUND_IN out, List<Object> list) {
		if(ctx.pipeline().finagleLayerToken != null) {
			ctx.pipeline().finagleLayerToken.expire();
		}
		Weaver.callOriginal();
	}
}
