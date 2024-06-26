package com.twitter.finagle.builder;

import java.util.logging.Level;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
//import com.newrelic.instrumentation.finagle.core.ServiceNames;
import com.newrelic.instrumentation.finagle.core.ServiceUtils;
import com.twitter.finagle.Name;
import com.twitter.finagle.Service;
import com.twitter.finagle.ServiceFactory;
import com.twitter.finagle.Stack;
import com.twitter.finagle.Stack.Param;
import com.twitter.finagle.Stack.Params;
import com.twitter.finagle.builder.ClientConfig.DestName;
import com.twitter.finagle.param.Label;

import scala.Tuple2;
import scala.collection.Iterator;

@Weave(originalName = "com.twitter.finagle.builder.ClientBuilder")
public abstract class ClientBuilder_instrumentation<Req, Rep, HasCluster, HasCodec, HasHostConnectionLimit> {

	public abstract Stack.Params params();
	
	public ServiceFactory<Req, Rep> buildFactory(ClientConfigEvidence<HasCluster, HasCodec, HasHostConnectionLimit> config) {
		Params params = params();
		String nameToUse = null;
		Name dest_name = null;
		
		if(params != null) {
			DestName destName = null;
			Iterator<Tuple2<Param<?>, Object>> iterator = params.iterator();
			while(iterator.hasNext()) {
				Tuple2<Param<?>, Object> paramTuple = iterator.next();
				Object value = paramTuple._2();
				if(value instanceof DestName) {
					destName = (DestName)value;
				}
				
			}
			if(params.contains(Label.param())) {
				Label label = params.apply(Label.param());
				if(label != null) {
					
					nameToUse = label.label();
				}
			}
			if(destName != null) {
				dest_name = destName.name();
			}
		}
		NewRelic.getAgent().getLogger().log(Level.FINE, "In ClientBuilder.buildFactory, nameToUse is {0}", nameToUse);
//		NewRelic.getAgent().getLogger().log(Level.FINE, "In ClientBuilder.buildFactory, destName is {0}", ServiceNames.getNameString(dest_name));
		ServiceFactory<Req, Rep>  serviceFactory = Weaver.callOriginal();
		if(nameToUse != null) {
			ServiceUtils.getInstance().addServiceFactoryName(serviceFactory, dest_name, nameToUse);
		}
		return serviceFactory;		
	}

	public Service<Req, Rep> build(ClientConfigEvidence<HasCluster, HasCodec, HasHostConnectionLimit> config) {
		
		Params params = params();
		String nameToUse = null;
		Name dest_name = null;
		
		if(params != null) {
			Iterator<Tuple2<Param<?>, Object>> iterator = params.iterator();
			DestName destName = null;
			
			while(iterator.hasNext()) {
				Tuple2<Param<?>, Object> paramTuple = iterator.next();
				Object value = paramTuple._2();
				if(value instanceof DestName) {
					destName = (DestName)value;
				}
			}
			if(params.contains(Label.param())) {
				Label label = params.apply(Label.param());
				if(label != null) {
					
					nameToUse = label.label();
				}
			}
			if(destName != null) {
				dest_name = destName.name();
			}
		}
		NewRelic.getAgent().getLogger().log(Level.FINE, "In ClientBuilder.build, nameToUse is {0}", nameToUse);
//		NewRelic.getAgent().getLogger().log(Level.FINE, "In ClientBuilder.build, dest_name is {0}", ServiceNames.getNameString(dest_name));
		Service<Req, Rep>  service = Weaver.callOriginal();
		if(nameToUse != null) {
			ServiceUtils.getInstance().addServiceName(service,dest_name, nameToUse);
		}
		return service;
		
	}

}
