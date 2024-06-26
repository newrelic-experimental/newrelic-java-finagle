package com.newrelic.instrumentation.finagle.http.tracing;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import com.newrelic.agent.deps.org.objectweb.asm.commons.Method;
import com.newrelic.agent.instrumentation.methodmatchers.ExactMethodMatcher;
import com.newrelic.agent.instrumentation.methodmatchers.MethodMatcher;
import com.newrelic.agent.instrumentation.methodmatchers.OrMethodMatcher;
import com.newrelic.api.agent.NewRelic;

public class ClientMethodMatcher implements MethodMatcher {
	
	private MethodMatcher matcher = null;
	protected static final String newClientNameDescriptor;
	protected static final String newClientNameLabelDescriptor;
	protected static final String newClientNameObjLabelDescriptor;
	protected static final String newServiceNameDescriptor;
	protected static final String newServiceNameLabelDescriptor;
	protected static final String newServiceNameObjLabelDescriptor;
	
	//(Ljava/lang/String;)Lcom/twitter/finagle/ServiceFactory; 
	static {
		newClientNameDescriptor = "(Ljava/lang/String;Ljava/lang/String;)Lcom/twitter/finagle/ServiceFactory;";
		newClientNameLabelDescriptor = "(Ljava/lang/String;)Lcom/twitter/finagle/ServiceFactory;";
		newClientNameObjLabelDescriptor = "(Lcom/twitter/finagle/Name;Ljava/lang/String;)Lcom/twitter/finagle/ServiceFactory;";;
		newServiceNameDescriptor = "(Ljava/lang/String;Ljava/lang/String;)Lcom/twitter/finagle/Service; ";
		newServiceNameLabelDescriptor = "(Ljava/lang/String;)Lcom/twitter/finagle/Service;";
		newServiceNameObjLabelDescriptor = "(Lcom/twitter/finagle/Name;Ljava/lang/String;)Lcom/twitter/finagle/Service;";
	}
	
	public ClientMethodMatcher() {
		List<MethodMatcher> matchers = new ArrayList<>();
		matchers.add(new ExactMethodMatcher("newClient", newClientNameDescriptor));
		matchers.add(new ExactMethodMatcher("newClient", newClientNameLabelDescriptor));
		matchers.add(new ExactMethodMatcher("newClient", newClientNameObjLabelDescriptor));
		matchers.add(new ExactMethodMatcher("newService", newServiceNameDescriptor));
		matchers.add(new ExactMethodMatcher("newService", newServiceNameLabelDescriptor));
		matchers.add(new ExactMethodMatcher("newService", newServiceNameObjLabelDescriptor));
		
		matcher = OrMethodMatcher.getMethodMatcher(matchers);
	}

	@Override
	public boolean matches(int access, String name, String desc, Set<String> annotations) {
		boolean b = matcher.matches(access, name, desc, annotations);
		if(b) {
			NewRelic.getAgent().getLogger().log(Level.FINE, "ClientMethodMatcher.matches found match with name: {0} and desc {1} is returning {2}", name,desc,b);
		}
		return b;
	}

	@Override
	public Method[] getExactMethods() {
		return matcher.getExactMethods();
	}

}
