package io.netty.handler.codec;

import java.util.List;
import java.util.logging.Level;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.labs.finagle.netty.FinagleNettyUtils;

import io.netty.bootstrap.FinagleNettyDispatcher;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext_instrumentation;

@Weave(type = MatchType.BaseClass)
public abstract class ByteToMessageDecoder {


	protected void callDecode(ChannelHandlerContext_instrumentation ctx, ByteBuf in, List<Object> out) {
		Weaver.callOriginal();

		for(Object obj : out) {
			if(FinagleNettyUtils.shouldGenerateDispatcher(obj) && ctx.pipeline().finagleLayerToken == null) {
				NewRelic.getAgent().getLogger().log(Level.FINE, "In ByteToMessageDecoder.callDecode, generating FinagleNettyDispatcher based on {0}", obj);
				if(!FinagleNettyDispatcher.instrumented.get()) {
					FinagleNettyDispatcher.get();
				}
				FinagleNettyDispatcher.channelRead(ctx, obj);
			}
		}

	}

}
