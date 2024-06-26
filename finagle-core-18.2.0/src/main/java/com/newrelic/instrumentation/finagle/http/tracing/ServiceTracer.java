package com.newrelic.instrumentation.finagle.http.tracing;

import com.newrelic.agent.Transaction;
import com.newrelic.agent.TransactionActivity;
import com.newrelic.agent.tracers.ClassMethodSignature;
import com.newrelic.agent.tracers.DefaultTracer;
import com.newrelic.agent.tracers.metricname.MetricNameFormat;
import com.newrelic.agent.tracers.metricname.SimpleMetricNameFormat;
import com.newrelic.instrumentation.finagle.core.NewRelicHolder;
import com.newrelic.instrumentation.finagle.core.Utils;

public class ServiceTracer extends DefaultTracer {
	
	private NewRelicHolder holder = null;
	private Object request = null;

	public ServiceTracer(NewRelicHolder h, Transaction transaction, ClassMethodSignature sig, Object object,MetricNameFormat metricNameFormatter, int tracerFlags) {
		super(transaction, sig, object, metricNameFormatter, tracerFlags);
		holder = h;
		request = object;
	}

	public ServiceTracer(NewRelicHolder h, Transaction transaction, ClassMethodSignature sig, Object object,
			MetricNameFormat metricNameFormatter) {
		super(transaction, sig, object, metricNameFormatter);
		holder = h;
		request = object;
	}

	public ServiceTracer(NewRelicHolder h, Transaction transaction, ClassMethodSignature sig, Object object) {
		this(h, transaction, sig, object,new SimpleMetricNameFormat("Custom/Finagle/Service/" + object.getClass().getSimpleName()));
		holder = h;
		request = object;
	}

	public ServiceTracer(NewRelicHolder h, TransactionActivity txa, ClassMethodSignature sig, Object object,
			MetricNameFormat metricNameFormatter, int tracerFlags, long pStartTime) {
		super(txa, sig, object, metricNameFormatter, tracerFlags, pStartTime);
		holder = h;
		request = object;
	}

	public ServiceTracer(NewRelicHolder h, TransactionActivity txa, ClassMethodSignature sig, Object object,
			MetricNameFormat metricNameFormatter, int tracerFlags) {
		super(txa, sig, object, metricNameFormatter, tracerFlags);
		holder = h;
	}

	public ServiceTracer(NewRelicHolder h, TransactionActivity txa, ClassMethodSignature sig, Object object,
			MetricNameFormat metricNameFormatter, long pStartTime) {
		super(txa, sig, object, metricNameFormatter, pStartTime);
		holder = h;
		request = object;
	}

	@Override
	public void finish(Throwable throwable) {
		if(holder != null) {
			Utils.addEventListener(null, holder, request);
		}
		super.finish(throwable);
	}

	@Override
	public void finish(int opcode, Object returnValue) {
		if(holder != null) {
			Utils.addEventListener(returnValue, holder, request);
		}
		super.finish(opcode, returnValue);
	}
	
	

}
