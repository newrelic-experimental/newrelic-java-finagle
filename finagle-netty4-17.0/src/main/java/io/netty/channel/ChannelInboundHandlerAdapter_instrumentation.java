package io.netty.channel;

import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

@Weave(type = MatchType.BaseClass, originalName = "io.netty.channel.ChannelInboundHandlerAdapter")
public abstract class ChannelInboundHandlerAdapter_instrumentation implements ChannelInboundHandler {

	@Trace(async = true)
	public void channelRead(ChannelHandlerContext_instrumentation ctx, Object msg) throws Exception {
		if(ctx.pipeline().finagleLayerToken != null) {
			ctx.pipeline().finagleLayerToken.link();
		}
		Weaver.callOriginal();
	}
	
	public void channelReadComplete(ChannelHandlerContext_instrumentation ctx) {
		if(ctx.pipeline().finagleLayerToken != null) {
			ctx.pipeline().finagleLayerToken.expire();
			ctx.pipeline().finagleLayerToken = null;
		}
		Weaver.callOriginal();
	}
}
