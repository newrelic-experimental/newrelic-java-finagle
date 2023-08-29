package com.twitter.finagle.http.codec

import java.util.Map
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.{Future, Promise}
import com.newrelic.api.agent.weaver.{Weaver,Weave}
import com.newrelic.instrumentation.finagle.http.{NewRelicHolder,NRFutureEventListener,Utils}
import com.newrelic.api.agent.{NewRelic,Trace}

@Weave( originalName="com.twitter.finagle.http.codec.HttpClientDispatcher")
class HttpClientDispatcher_instrumentation {

  @Trace(dispatcher = true)
  def dispatch(req: Request, p: Promise[Response]): Future[Unit] = {
  	val holder = new NewRelicHolder("/ResponseToCall/HttpClientDispatcher/dispatch")
    val attributes : Map[String,Object] = Utils.getAttributes(req)
    NewRelic.getAgent.getTracedMethod.addCustomAttributes(attributes)
  	val future : Future[Unit] = Weaver.callOriginal()
  	future.addEventListener(new NRFutureEventListener(holder,req))
  }

}