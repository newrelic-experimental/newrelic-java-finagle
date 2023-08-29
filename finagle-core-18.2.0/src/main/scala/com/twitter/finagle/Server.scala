package com.twitter.finagle

import com.newrelic.api.agent.weaver.scala.{ScalaMatchType, ScalaWeave}
import com.newrelic.api.agent.Trace
import com.newrelic.instrumentation.finagle.core.ServiceNames
import com.newrelic.api.agent.weaver.Weaver
import java.net.SocketAddress

@ScalaWeave(`type` = ScalaMatchType.Trait, originalName="com.twitter.finagle.Server")
class Server_instrumentation[Req,Res] {

    def serve(addr: SocketAddress, service: Service[Req, Res]): ListeningServer = {
        ServiceNames.addServiceName(service,service.getClass.getName,null)
        val value : ListeningServer = Weaver.callOriginal()
        value
    }

    def serve(addr: String, service: Service[Req, Res]): ListeningServer = {
        ServiceNames.addServiceName(service,service.getClass.getName,null)
        val value : ListeningServer = Weaver.callOriginal()
        value
    }

    def serveAndAnnounce(name: String, addr: String, service: Service[Req, Res]): ListeningServer = {
        ServiceNames.addServiceName(service,name,null)
        val value : ListeningServer = Weaver.callOriginal()
        value
    }

    def serveAndAnnounce(name: String, service: Service[Req, Res]): ListeningServer = {
        ServiceNames.addServiceName(service,name,null)
        val value : ListeningServer = Weaver.callOriginal()
        value
    }

    def serveAndAnnounce(name: String, addr: SocketAddress, service: Service[Req, Res]): ListeningServer = {
        ServiceNames.addServiceName(service,name,null)
        val value : ListeningServer = Weaver.callOriginal()
        value
    }


}
