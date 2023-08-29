package com.twitter.finagle.http

import com.newrelic.api.agent.weaver.{Weaver,Weave}
import com.newrelic.instrumentation.finagle.http.{ServiceNames}
import com.twitter.finagle.Service

@Weave(originalName="com.twitter.finagle.http.MethodBuilder")
class MethodBuilder_instrumentation {

    def newService(methodName: String): Service[Request, Response] = {
        val value : Service[Request,Response] = Weaver.callOriginal()
        ServiceNames.addServiceName(value,methodName,null)
        value
    }

}