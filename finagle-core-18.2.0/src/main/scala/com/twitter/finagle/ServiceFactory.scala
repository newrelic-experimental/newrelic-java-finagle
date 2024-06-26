package com.twitter.finagle

import com.newrelic.api.agent.weaver.{Weaver,Weave}
import com.newrelic.instrumentation.finagle.core.ServiceUtils

@Weave(originalName="com.twitter.finagle.ServiceFactory")
class ServiceFactory_instrumentation[Req, Rep] {

    def toService: Service[Req, Rep] = {
        val service : Service[Req, Rep] = Weaver.callOriginal()
        // val serviceName : String = ServiceUtils.getServiceFactoryName(this)
        // if(serviceName != null) {
        //     ServiceUtils.getInstance().addServiceName(service,serviceName,null)
        // }
        service
    }
}