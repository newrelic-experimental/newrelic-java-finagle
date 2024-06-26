package com.newrelic.instrumentation.finagle.http.tracing;

import com.newrelic.agent.Transaction;
import com.newrelic.agent.tracers.ClassMethodSignature;
import com.newrelic.agent.tracers.DefaultTracer;
import com.newrelic.agent.tracers.Tracer;
import com.newrelic.agent.tracers.TracerFactory;
import com.newrelic.agent.tracers.metricname.SimpleMetricNameFormat;
import com.newrelic.api.agent.TransactionNamePriority;
import com.newrelic.instrumentation.finagle.core.NewRelicHolder;
import com.newrelic.instrumentation.finagle.core.ServiceUtils;

public class FinagleCoreServiceFactory implements TracerFactory {

	protected static final String TRACER_FACTORY_NAME = "FINAGLE_SERVICE_FACTORY";

	@Override
	public Tracer getTracer(Transaction transaction, ClassMethodSignature sig, Object object, Object[] args) {
		String serviceName = ServiceUtils.getInstance().getServiceName(object);
		if(serviceName != null) {
			NewRelicHolder holder = new NewRelicHolder("Custom/Finagle/Service/" + serviceName);
			transaction.setTransactionName(TransactionNamePriority.FRAMEWORK_HIGH, false,  "Finagle-Service", new String[] { "Finagle", "Service", serviceName });
			SimpleMetricNameFormat format = new SimpleMetricNameFormat("Custom/Finagle/Service/"+serviceName+"/apply");
			return new ServiceTracer(holder, transaction, sig, object,format);
		}
		SimpleMetricNameFormat format = new SimpleMetricNameFormat("Custom/Finagle/Service/"+object.getClass().getSimpleName()+"/apply");
		return new DefaultTracer(transaction, sig, object, format);
	}

}
