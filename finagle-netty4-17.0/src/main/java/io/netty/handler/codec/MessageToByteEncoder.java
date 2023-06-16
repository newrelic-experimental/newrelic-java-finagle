package io.netty.handler.codec;

import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext_instrumentation;

@Weave(type = MatchType.BaseClass)
public class MessageToByteEncoder<I> {

	protected void encode(ChannelHandlerContext_instrumentation ctx, I i, ByteBuf buffer) {
		if(ctx.pipeline().finagleLayerToken != null) {
			ctx.pipeline().finagleLayerToken.expire();
		}
		Weaver.callOriginal();
	}
}
