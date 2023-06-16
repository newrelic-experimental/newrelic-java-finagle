package io.netty.bootstrap;

import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.WeaveAllConstructors;

@Weave
public abstract class AbstractBootstrap {

    @WeaveAllConstructors
    AbstractBootstrap() {
        // initialize NettyDispatcher here to avoid classloader deadlocks
        FinagleNettyDispatcher.get();
    }


}
