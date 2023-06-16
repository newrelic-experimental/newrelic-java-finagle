package io.netty.channel;

import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

@Weave(type = MatchType.Interface, originalName = "io.netty.channel.Channel")
public abstract class Channel_instrumentation {

    public ChannelPipeline_instrumentation pipeline() {
        return Weaver.callOriginal();
    }

}
