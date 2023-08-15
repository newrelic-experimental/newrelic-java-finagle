package com.twitter.finagle;

import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.labs.finagle.mysql.Finagle_MySQLUtils;
import com.newrelic.instrumentation.labs.finagle.mysql.Finagle_MySQLUtils.DBInfo;
import com.twitter.finagle.mysql.Request;
import com.twitter.finagle.mysql.Result;

@Weave
public abstract class Mysql {
	
	@Weave
	public abstract static class Client {
		
		@NewField
		private String dbName = null;
		
		public Client(Stack<ServiceFactory<Request, Result>> factory, Stack.Params params) {
			
		}
		
		public Client withDatabase(String db) {
			dbName = db;
			return Weaver.callOriginal();
		}
		
		public com.twitter.finagle.mysql.Client newRichClient(Name dest, String label) {
			
			String hostStr = Name.showable().show(dest);
			DBInfo dbInfo = Finagle_MySQLUtils.getDBInfo(hostStr);
			com.twitter.finagle.mysql.Client client = Weaver.callOriginal();
			if(dbInfo != null) {
				client.dbHost = dbInfo.getHost();
				client.dbPort = dbInfo.getPort();
			}
			if(dbName != null) {
				client.dbName = dbName;
			}
			
			return client;
		}
		
		public com.twitter.finagle.mysql.Client newRichClient(String dest) {
			
			String hostStr = dest;
			DBInfo dbInfo = Finagle_MySQLUtils.getDBInfo(hostStr);
			com.twitter.finagle.mysql.Client client = Weaver.callOriginal();
			if(dbInfo != null) {
				client.dbHost = dbInfo.getHost();
				client.dbPort = dbInfo.getPort();
			}
			if(dbName != null) {
				client.dbName = dbName;
			}
			
			return client;
		}
	}
}
