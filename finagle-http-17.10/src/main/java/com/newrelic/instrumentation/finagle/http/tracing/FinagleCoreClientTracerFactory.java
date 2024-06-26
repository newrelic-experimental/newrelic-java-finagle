package com.newrelic.instrumentation.finagle.http.tracing;

import java.util.logging.Level;

import com.newrelic.agent.Transaction;
import com.newrelic.agent.tracers.ClassMethodSignature;
import com.newrelic.agent.tracers.Tracer;
import com.newrelic.agent.tracers.TracerFactory;
import com.newrelic.api.agent.NewRelic;

public class FinagleCoreClientTracerFactory implements TracerFactory {
	
	protected static final String TRACER_FACTORY_NAME = "FINAGLE_CLIENT_FACTORY";
	public enum MethodType {SERVICEFACTORY, SERVICE};


	@Override
	public Tracer getTracer(Transaction transaction, ClassMethodSignature sig, Object object, Object[] args) {
		NewRelic.getAgent().getLogger().log(Level.FINE, "Call to FinagleCoreClientTracerFactory.getTracer, sig: {0}, object: {1}", sig,object);
		String methodName = sig.getMethodName();
		MethodType mt = null;
		String name = null;
		String label = null;
		Object nameObj = null;
		if(args.length == 1) {
			name = args[0].toString();
		} else if(args.length == 2) {
			if(args[0] instanceof String) {
				name =(String)args[0];
			} else {
				nameObj = args[0];
			}
			label = args[1].toString();
		}
		if(methodName.equalsIgnoreCase("newService")) {
			mt = MethodType.SERVICE;
		} else if(methodName.equalsIgnoreCase("newClient")) {
			mt = MethodType.SERVICEFACTORY;
		}
		if(mt != null) {
			FinagleClientTracer tracer = new FinagleClientTracer(sig, transaction, mt);
			tracer.setName(name);
			tracer.setLabel(label);
			tracer.setNameObj(nameObj);
			return tracer;
		}
		return null;
	}

}
