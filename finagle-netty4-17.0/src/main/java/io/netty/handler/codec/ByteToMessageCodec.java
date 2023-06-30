package io.netty.handler.codec;

import java.util.List;

import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.labs.finagle.netty.FinagleNettyUtils;

import io.netty.bootstrap.FinagleNettyDispatcher;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext_instrumentation;

@Weave(type = MatchType.BaseClass)
public abstract class ByteToMessageCodec<I> {

    protected void encode(ChannelHandlerContext_instrumentation ctx, I msg, ByteBuf out) {
		if(ctx.pipeline().finagleLayerToken != null) {
			ctx.pipeline().finagleLayerToken.expire();
			ctx.pipeline().finagleLayerToken = null;
		}
		Weaver.callOriginal();
    }

    protected void decode(ChannelHandlerContext_instrumentation ctx, ByteBuf in, List<Object> out) {
    	Weaver.callOriginal();
    	
		for(Object obj : out) {
			if(FinagleNettyUtils.shouldGenerateDispatcher(obj) && ctx.pipeline().finagleLayerToken == null) {
				if(!FinagleNettyDispatcher.instrumented.get()) {
					FinagleNettyDispatcher.get();
				}
				FinagleNettyDispatcher.channelRead(ctx, obj);
			}
		}
    }

}
