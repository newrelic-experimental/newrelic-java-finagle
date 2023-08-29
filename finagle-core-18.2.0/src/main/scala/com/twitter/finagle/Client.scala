package com.twitter.finagle

import com.newrelic.api.agent.weaver.scala.{ScalaMatchType, ScalaWeave}
import com.newrelic.api.agent.Trace
import com.newrelic.instrumentation.finagle.core.ServiceNames
import com.newrelic.api.agent.weaver.Weaver

@ScalaWeave(`type` = ScalaMatchType.Trait, originalName="com.twitter.finagle.Client")
class Client_instrumentation[Req,Rep] {

    def newService(dest: Name, label: String): Service[Req, Rep] = {
        val nameStr = ServiceNames.getNameString(dest)
        val value : Service[Req, Rep] = Weaver.callOriginal()
        ServiceNames.addServiceName(value,nameStr,label)
        value
    }

    def newService(dest: String): Service[Req, Rep] = { 
        val value : Service[Req, Rep] = Weaver.callOriginal()
        ServiceNames.addServiceName(value,dest,null)
        value
    }

    def newService(dest: String, label: String): Service[Req, Rep] = {
        val value : Service[Req, Rep] = Weaver.callOriginal()
        ServiceNames.addServiceName(value,dest,label)
        value
    }

    def newClient(dest: Name, label: String): ServiceFactory[Req, Rep] = {
        val sf : ServiceFactory[Req, Rep] = Weaver.callOriginal()
        ServiceNames.addServiceFactoryName(sf,dest,label)
        sf
    }

    def newClient(dest: String): ServiceFactory[Req, Rep] = {
        val sf : ServiceFactory[Req, Rep] = Weaver.callOriginal()
        ServiceNames.addServiceFactoryName(sf,dest,null)
        sf
    }

    def newClient(dest: String, label: String): ServiceFactory[Req, Rep] = {
        val sf : ServiceFactory[Req, Rep] = Weaver.callOriginal()
        ServiceNames.addServiceFactoryName(sf,dest,null)
        sf
    }
}