package com.twitter.finagle.mysql;

import com.newrelic.agent.database.ParsedDatabaseStatement;
import com.newrelic.api.agent.DatastoreParameters;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.newrelic.instrumentation.labs.finagle.mysql.Finagle_MySQLUtils;
import com.newrelic.instrumentation.labs.finagle.mysql.NRFailureFunction;
import com.newrelic.instrumentation.labs.finagle.mysql.NRSuccessFunction;
import com.newrelic.instrumentation.labs.finagle.mysql.NewRelicHolder;
import com.twitter.util.Future;

import scala.Function1;
import scala.collection.immutable.Seq;

@Weave(type = MatchType.Interface)
public abstract class Client {
	
	@NewField
	public String dbHost = null;
	
	@NewField
	public int dbPort = 3306;
	
	@NewField
	public String dbName = null;

	@Trace
	public Future<Result> query(String sql) {
		NewRelic.getAgent().getTracedMethod().addCustomAttribute("SQL", sql);
		ParsedDatabaseStatement pStmt = Finagle_MySQLUtils.parseSQL(sql);
		NewRelic.getAgent().getTracedMethod().addCustomAttribute("ParsedDatabaseStatement", pStmt.toString());
		
		DatastoreParameters params =  Finagle_MySQLUtils.getDBParameters(pStmt, dbHost, dbPort, dbName); //DatastoreParameters.product("MySQL").collection(pStmt.getModel()).operation(pStmt.getOperation()).build();
		NewRelicHolder holder = new NewRelicHolder("MySQL-Query");
		holder.addExternal(params);
		Future<Result>  result = Weaver.callOriginal();
		
		return result.onSuccess(new NRSuccessFunction<Result>(holder)).onFailure(new NRFailureFunction(holder));
	}
	
	@Trace
	public <T> Future<Seq<T>> select(String sql, Function1<Row, T> f) {
		NewRelic.getAgent().getTracedMethod().addCustomAttribute("SQL", sql);
		ParsedDatabaseStatement pStmt = Finagle_MySQLUtils.parseSQL(sql);
		NewRelic.getAgent().getTracedMethod().addCustomAttribute("ParsedDatabaseStatement", pStmt.toString());
		DatastoreParameters params = Finagle_MySQLUtils.getDBParameters(pStmt, dbHost, dbPort, dbName);  //DatastoreParameters.product("MySQL").collection(pStmt.getModel()).operation(pStmt.getOperation()).build();
		NewRelicHolder holder = new NewRelicHolder("MySQL-Select");
		holder.addExternal(params);
		Future<Seq<T>>  result = Weaver.callOriginal();
		
		return result.onSuccess(new NRSuccessFunction<Seq<T>>(holder)).onFailure(new NRFailureFunction(holder));
	}

	
	public PreparedStatement prepare(String sql) {
		PreparedStatement pstmt = Weaver.callOriginal();
		pstmt.sql = sql;
		pstmt.dbHost = dbHost;
		pstmt.dbName = dbName;
		pstmt.dbPort = dbPort;
		return pstmt;
	}
}
