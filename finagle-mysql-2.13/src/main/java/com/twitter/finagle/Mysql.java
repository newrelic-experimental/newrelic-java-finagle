package com.twitter.finagle;

import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.labs.finagle.mysql.Finagle_MySQLUtils;
import com.newrelic.instrumentation.labs.finagle.mysql.Finagle_MySQLUtils.DBInfo;
import com.twitter.finagle.mysql.param.Database;

import scala.Option;

@Weave
public abstract class Mysql {
	
	@Weave
	public abstract static class Client {
		
		public abstract Stack.Params params();
		
		public com.twitter.finagle.mysql.Client newRichClient(Name dest, String label) {
			
			Stack.Params params = params();
			String dbName = null;
			boolean b = params.contains(Database.param());
			if(b) {
				Database dbParam = params.apply(Database.param());
				if(dbParam != null) {
					Option<String> dbOption = dbParam.db();
					if(dbOption.isDefined()) {
						dbName = dbOption.get();
					}
				}
			}
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
			
			Stack.Params params = params();
			String dbName = null;
			boolean b = params.contains(Database.param());
			if(b) {
				Database dbParam = params.apply(Database.param());
				if(dbParam != null) {
					Option<String> dbOption = dbParam.db();
					if(dbOption.isDefined()) {
						dbName = dbOption.get();
					}
				}
			}
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
