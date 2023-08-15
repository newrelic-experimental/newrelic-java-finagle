package com.newrelic.instrumentation.labs.finagle.mysql;

import com.newrelic.agent.bridge.datastore.DatabaseVendor;
import com.newrelic.agent.database.DatabaseService;
import com.newrelic.agent.database.DefaultDatabaseStatementParser;
import com.newrelic.agent.database.ParsedDatabaseStatement;
import com.newrelic.agent.service.ServiceFactory;
import com.newrelic.api.agent.DatastoreParameters;
import com.newrelic.api.agent.DatastoreParameters.CollectionParameter;
import com.newrelic.api.agent.DatastoreParameters.DatabaseParameter;
import com.newrelic.api.agent.DatastoreParameters.InstanceParameter;

public class Finagle_MySQLUtils {

	public static boolean initialized = false;
	private static DatabaseVendor vendor;

	private static DefaultDatabaseStatementParser parser = null;


	public static void init() {
		DatabaseService dbService = ServiceFactory.getDatabaseService();
		if(dbService != null) {
			parser = (DefaultDatabaseStatementParser) dbService.getDatabaseStatementParser();
			initialized = true;
		}
		vendor = new Finagle_MySQLVendor();
	}

	public static ParsedDatabaseStatement parseSQL(String sql) {
		if(!initialized) {
			init();
		}

		if(initialized) {

			ParsedDatabaseStatement statement = parser.getParsedDatabaseStatement(vendor, sql, null);

			return statement;
		}

		return null;
	}

	public static DatastoreParameters getDBParameters(ParsedDatabaseStatement stmt, String host, Integer port, String dbname) {
		DatastoreParameters params = null;
		if(stmt != null) {
			CollectionParameter cparam = DatastoreParameters.product("MySQL");
			String collection = stmt.getModel();
			String operation = stmt.getOperation();
			
			if(collection != null && operation != null) {
				InstanceParameter iparam = cparam.collection(collection).operation(operation);
				if(host != null && port != null && !host.isEmpty()) {
					DatabaseParameter dbParm = iparam.instance(host, port);
					if(dbname != null && !dbname.isEmpty()) {
						params = dbParm.databaseName(dbname).build();
					} else {
						params = dbParm.build();
					}
				} else {
					params = iparam.build();
				}
			}
		}
		return params;
	}
	
	public static DBInfo getDBInfo(String hostString) {
		if(hostString != null && !hostString.isEmpty()) {
			int index = hostString.indexOf(':');
			if(index > -1) {
				String host = hostString.substring(0, index);
				String portStr = hostString.substring(index+1);
				int port = Integer.parseInt(portStr);
				return new DBInfo(host, port);
			} else {
				return new DBInfo(hostString, 3306);
			}
		}
		
		return null;
	}
	
	
	public static class DBInfo {
		
		private String host = null;
		private int port;
		
		public DBInfo(String h, int p) {
			host = h;
			port = p;
		}

		public String getHost() {
			return host;
		}

		public int getPort() {
			return port;
		}

		@Override
		public String toString() {
			return "host: " + host + ", port: " + port;
		}
		
	}

}
