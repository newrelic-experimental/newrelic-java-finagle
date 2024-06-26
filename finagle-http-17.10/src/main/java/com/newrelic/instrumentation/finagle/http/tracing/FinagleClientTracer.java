package com.newrelic.instrumentation.finagle.http.tracing;

import java.util.logging.Level;

import com.newrelic.agent.Transaction;
import com.newrelic.agent.tracers.ClassMethodSignature;
import com.newrelic.agent.tracers.MethodExitTracer;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.instrumentation.finagle.http.ServiceNames;
import com.newrelic.instrumentation.finagle.http.tracing.FinagleCoreClientTracerFactory.MethodType;

public class FinagleClientTracer extends MethodExitTracer {
	
	private String name = null;
	private String label = null;
	private Object nameObj = null;
	private FinagleCoreClientTracerFactory.MethodType methodType = null;

	public FinagleClientTracer(ClassMethodSignature signature, Transaction transaction, FinagleCoreClientTracerFactory.MethodType mType) {
		super(signature, transaction);
		methodType = mType;
	}

	@Override
	protected void doFinish(int opcode, Object returnValue) {
		NewRelic.getAgent().getLogger().log(Level.FINE, "Call to FinagleClientTracer.doFinish({0},{1}), name: {2}, label: {3}, nameObj: {4}", opcode, returnValue, name, label, nameObj);
		if(methodType == MethodType.SERVICEFACTORY) {
			if(name != null) { 
				ServiceNames.addServiceFactoryName(returnValue.getClass(), name, label);
			} else if(nameObj != null) {
				ServiceNames.addServiceFactoryName(returnValue, nameObj, label);
			}
		} else if(methodType == MethodType.SERVICE) {
			if(name != null) {
				ServiceNames.addServiceName(returnValue.getClass(), name, label);
			}
		}
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected void setLabel(String label) {
		this.label = label;
	}

	protected void setNameObj(Object nameObj) {
		this.nameObj = nameObj;
	}

}
