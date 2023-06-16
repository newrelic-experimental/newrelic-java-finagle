package io.netty.channel;

import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;

@Weave(type = MatchType.BaseClass, originalName = "io.netty.channel.ChannelPipeline")
public abstract class ChannelPipeline_instrumentation {

	 @NewField
	 public Token finagleLayerToken;
	 
}
