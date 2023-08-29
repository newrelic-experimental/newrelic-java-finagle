package com.twitter.finagle

import com.newrelic.api.agent.weaver.{Weaver,Weave}
import com.newrelic.instrumentation.finagle.core.ServiceNames

@Weave(originalName="com.twitter.finagle.ServiceFactory")
class ServiceFactory_instrumentation[Req, Rep] {

    def toService: Service[Req, Rep] = {
        val service : Service[Req, Rep] = Weaver.callOriginal()
        val serviceName : String = ServiceNames.getServiceFactoryName(getClass)
        ServiceNames.addServiceName(service,serviceName,null)
        service
    }
}