package com.twitter.finagle.dispatch

import com.twitter.finagle.stats.StatsReceiver
import com.twitter.finagle.transport.Transport
import com.twitter.util.{Future, Promise, Return, Throw, Try}
import com.newrelic.api.agent.weaver.Weave
import com.newrelic.api.agent.weaver.Weaver
import com.newrelic.api.agent.NewRelic
import com.newrelic.api.agent.Trace

@Weave
class SerialClientDispatcher[Req, Rep](trans: Transport[Req, Rep], statsReceiver: StatsReceiver) {

    @Trace(dispatcher = true)
    protected def dispatch(req: Req, p: Promise[Rep]): Future[Unit] = {
        
        NewRelic.getAgent.getTracedMethod.setMetricName("")
        Weaver.callOriginal()
    }
}