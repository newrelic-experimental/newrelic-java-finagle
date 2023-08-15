package com.newrelic.instrumentation.labs.finagle.mysql;

import com.newrelic.agent.bridge.datastore.DatastoreVendor;
import com.newrelic.agent.bridge.datastore.JdbcDatabaseVendor;

public class Finagle_MySQLVendor extends JdbcDatabaseVendor {

	public Finagle_MySQLVendor() {
		super("MySQL","mysql",false);
	}
	@Override
	public DatastoreVendor getDatastoreVendor() {
		return DatastoreVendor.MySQL;
	}

}
