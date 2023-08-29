package com.twitter.finagle.builder

import com.newrelic.api.agent.weaver.scala.{ScalaMatchType, ScalaWeave}
import com.newrelic.api.agent.Trace
import com.newrelic.instrumentation.finagle.core.ServiceNames
import com.newrelic.api.agent.weaver.Weaver
import com.twitter.finagle.{Name, Service,ServiceFactory}
import com.twitter.finagle.client.StackBasedClient

@ScalaWeave(`type` = ScalaMatchType.Object, originalName="com.twitter.finagle.builder.ClientBuilderClient")
class ClientBuilderClient_instrumentation {

    def newService[Req, Rep](client0: StackBasedClient[Req, Rep],dest: Name,label: String  ): Service[Req, Rep] = {
      val value : Service[Req, Rep] = Weaver.callOriginal()
      ServiceNames.addServiceName(value,dest,label)
      value
  }

   def newClient[Req, Rep](client: StackBasedClient[Req, Rep], dest: Name, label: String): ServiceFactory[Req, Rep] = {
      val value : ServiceFactory[Req, Rep]  = Weaver.callOriginal()
      ServiceNames.addServiceFactoryName(value,dest,label)
      value
   }
}