package com.twitter.finagle;

import java.net.SocketAddress;
import java.util.logging.Level;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.twitter.finagle.http.Request;
import com.twitter.finagle.http.Response;

@Weave
public abstract class Http {

	public static ListeningServer serve(String addr, Service<Request, Response> service) {
		return Weaver.callOriginal();
	}
	
	public static ListeningServer serve(SocketAddress addr, Service<Request, Response> service) {
		return Weaver.callOriginal();
	}
	
	public static ListeningServer serveAndAnnounce(String name, Service<Request, Response> service) {
		service.label = name;
		return Weaver.callOriginal();
	}
	
	public static ListeningServer serveAndAnnounce(String name, String addr, Service<Request, Response> service) {
		service.label = name;
		return Weaver.callOriginal();
	}
	
	public static ListeningServer serveAndAnnounce(String name, SocketAddress addr, Service<Request, Response> service) {
		service.label = name;
		return Weaver.callOriginal();
	}
	
	@Weave
	public static abstract class Client {

		public Service<Request, Response> newService(Name name, String label) {
			Service<Request, Response> service = Weaver.callOriginal();
			StringBuffer sb = new StringBuffer();
			if(name != null) {
				String nameString = Name.showable().show(name);
				if(nameString != null && !nameString.isEmpty()) {
					sb.append(nameString);
					sb.append('/');
				}
				
			}
			if(label != null && !label.isEmpty()) {
				sb.append(label);
			}
			if(sb.length() > 0) {
				service.label = sb.toString();
			}
			return service;

		}
		
		public Service<Request, Response> newService(String label) {
			Service<Request, Response> service = Weaver.callOriginal();
			service.label = label;
			return service;
		}

		public Service<Request, Response> newService(String name, String label) {
			Service<Request, Response> service = Weaver.callOriginal();
			StringBuffer sb = new StringBuffer();
			if(name != null && !name.isEmpty()) {
				sb.append(name);
				sb.append('/');
			}
			if(label != null && !label.isEmpty()) {
				sb.append(label);
			}
			if(sb.length() > 0) {
				service.label = sb.toString();
			}
			return service;
		}


	}

	@Weave
	public static abstract class Server {
		
		public ListeningServer serve(SocketAddress addr, Service<Request, Response> service) {
			NewRelic.getAgent().getLogger().log(Level.FINE, new Exception("Finagle-Tracking"),"Call to Http.Server.serveAndAnnounce({0},{1})", addr, service);
			return Weaver.callOriginal();
		}

		public ListeningServer serve(String addr, Service<Request, Response> service) {
			NewRelic.getAgent().getLogger().log(Level.FINE, new Exception("Finagle-Tracking"),"Call to Http.Server.serveAndAnnounce({0},{1})", addr, service);
			return Weaver.callOriginal();
		}
		
		public ListeningServer serveAndAnnounce(String name, SocketAddress addr, Service<Request, Response> service) {
			NewRelic.getAgent().getLogger().log(Level.FINE, new Exception("Finagle-Tracking"),"Call to Http.Server.serveAndAnnounce({0},{1},{2})", name, addr, service);
			service.label = name;
			return Weaver.callOriginal();
		}
		
		public ListeningServer serveAndAnnounce(String name, String addr, Service<Request, Response> service) {
			NewRelic.getAgent().getLogger().log(Level.FINE, new Exception("Finagle-Tracking"),"Call to Http.Server.serveAndAnnounce({0},{1},{2})", name, addr, service);
			service.label = name;
			return Weaver.callOriginal();
		}
		
		public ListeningServer serveAndAnnounce(String name, Service<Request, Response> service) {
			NewRelic.getAgent().getLogger().log(Level.FINE, new Exception("Finagle-Tracking"),"Call to Http.Server.serveAndAnnounce({0},{1})", name, service);
			service.label = name;
			return Weaver.callOriginal();
		}

	}
	
	

}
