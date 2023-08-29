package com.twitter.finagle

import com.newrelic.api.agent.weaver.scala.{ScalaMatchType, ScalaWeave}
import com.newrelic.api.agent.Trace
import com.twitter.util.{Future, Promise}
import com.newrelic.api.agent.weaver.{Weaver,Weave}
import com.newrelic.instrumentation.finagle.http.{NewRelicHolder,NRFutureEventListener,ServiceNames}
import com.twitter.finagle.http.{Fields, Request, Response}
import com.newrelic.api.agent.NewRelic

@ScalaWeave(`type` = ScalaMatchType.Trait, originalName="com.twitter.finagle.HttpRichClient")
class HttpRichClient_instrumentation {

    @Trace(dispatcher = true)
    def fetchUrl(url: java.net.URL): Future[Response] = {
        NewRelic.getAgent().getTracedMethod().addCustomAttribute("URL",url.toString())
        val future : Future[Response] = Weaver.callOriginal()
        val holder = new NewRelicHolder("/ResponseToCall/Http/fetchUrl")
        future.addEventListener(new NRFutureEventListener(holder,null))
    }

}

@ScalaWeave(`type` = ScalaMatchType.Object, originalName="com.twitter.finagle.Http")
class Http_instrumentation {

    def newService(dest: Name, label: String): Service[Request, Response] = {
        val value : Service[Request, Response] = Weaver.callOriginal()
        ServiceNames.addServiceName(value,dest,label)
        value

    }
}
