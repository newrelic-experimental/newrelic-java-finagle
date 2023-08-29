package com.twitter.finagle.http.codec

import com.newrelic.api.agent.weaver.scala.{ScalaMatchType, ScalaWeave}
import com.newrelic.api.agent.Trace

import com.twitter.finagle.http.{Fields, Request, Response}
import com.twitter.finagle.http.exp.{Multi, StreamTransport}
import com.twitter.util.{Future, Promise}
import com.newrelic.api.agent.weaver.{Weaver, Weave}
import com.twitter.finagle.stats.{CategorizingExceptionStatsHandler, StatsReceiver}
import com.newrelic.instrumentation.finagle.http.{NewRelicHolder,NRFutureEventListener,Utils}
import scala.runtime.BoxedUnit
import com.newrelic.api.agent.{NewRelic,TransactionNamePriority}

@Weave(originalName="com.twitter.finagle.http.codec.HttpServerDispatcher")
class HttpServerDispatcher_instrumentation {

	@Trace(dispatcher=true)
	protected def dispatch(req: Request): Future[Response] = {
	  	val holder = new NewRelicHolder("/ResponseToCall/HttpServerDispatcher/dispatch")
		val future : Future[Response] = Weaver.callOriginal()
		Utils.setTransactionName(req)
		future.addEventListener(new NRFutureEventListener(holder,req))
	}

}