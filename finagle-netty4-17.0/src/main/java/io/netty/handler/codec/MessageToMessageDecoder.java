package io.netty.handler.codec;

import java.util.List;

//import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.labs.finagle.netty.FinagleNettyUtils;

import io.netty.bootstrap.FinagleNettyDispatcher;
import io.netty.channel.ChannelHandlerContext_instrumentation;

@Weave(type = MatchType.BaseClass)
public class MessageToMessageDecoder<I> {

	protected void decode(ChannelHandlerContext_instrumentation ctx, I i, List<Object> list) {
		Weaver.callOriginal();
		
		for(Object obj : list) {
			if(FinagleNettyUtils.shouldGenerateDispatcher(obj) && ctx.pipeline().finagleLayerToken == null) {
				if(!FinagleNettyDispatcher.instrumented.get()) {
					FinagleNettyDispatcher.get();
				}
				FinagleNettyDispatcher.channelRead(ctx, obj);
			}
		}
	}
}
