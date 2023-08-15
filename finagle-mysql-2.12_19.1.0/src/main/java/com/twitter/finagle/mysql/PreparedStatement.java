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
import scala.collection.Seq;

@Weave(type = MatchType.Interface)
public abstract class PreparedStatement {
	
	@NewField
	String sql = null;
	
	@NewField
	public String dbHost = null;
	
	@NewField
	public int dbPort = 3306;
	
	@NewField
	public String dbName = null;

	@Trace(dispatcher = true)
	public Future<ResultSet> read(Seq<Parameter> params) {
		NewRelic.getAgent().getTracedMethod().addCustomAttribute("SQL", sql);
		NewRelicHolder holder = null;
		if(sql != null) {
			ParsedDatabaseStatement pStmt = Finagle_MySQLUtils.parseSQL(sql);
			NewRelic.getAgent().getTracedMethod().addCustomAttribute("ParsedDatabaseStatement", pStmt.toString());
			DatastoreParameters dbparams = Finagle_MySQLUtils.getDBParameters(pStmt, dbHost, null, dbName); //DatastoreParameters.product("MySQL").collection(pStmt.getModel()).operation(pStmt.getOperation()).build();
			holder = new NewRelicHolder("MySQL-Prepared-Read");
			holder.addExternal(dbparams);
			
		}
		Future<ResultSet> result = Weaver.callOriginal();
		
		if(holder == null) {
			return result;
		} else {
			return result.onFailure(new NRFailureFunction(holder)).onSuccess(new NRSuccessFunction<>(holder));
		}
	}
	
	@Trace(dispatcher = true)
	public Future<OK> modify(Seq<Parameter> params) {
		NewRelic.getAgent().getTracedMethod().addCustomAttribute("SQL", sql);
		NewRelicHolder holder = null;
		if(sql != null) {
			ParsedDatabaseStatement pStmt = Finagle_MySQLUtils.parseSQL(sql);
			NewRelic.getAgent().getTracedMethod().addCustomAttribute("ParsedDatabaseStatement", pStmt.toString());
			DatastoreParameters dbparams = Finagle_MySQLUtils.getDBParameters(pStmt, dbHost, null, dbName); //DatastoreParameters.product("MySQL").collection(pStmt.getModel()).operation(pStmt.getOperation()).build();
			holder = new NewRelicHolder("MySQL-Prepared-Modify");
			holder.addExternal(dbparams);
			
		}
		Future<OK> result = Weaver.callOriginal();
		
		if(holder == null) {
			return result;
		} else {
			return result.onFailure(new NRFailureFunction(holder)).onSuccess(new NRSuccessFunction<>(holder));
		}
	}
	
	@Trace(dispatcher = true)
	public <T> Future<Seq<T>> select(Seq<Parameter> params, Function1<Row, T> f) {
		NewRelic.getAgent().getTracedMethod().addCustomAttribute("SQL", sql);
		
		NewRelicHolder holder = null;
		if(sql != null) {
			ParsedDatabaseStatement pStmt = Finagle_MySQLUtils.parseSQL(sql);
			NewRelic.getAgent().getTracedMethod().addCustomAttribute("ParsedDatabaseStatement", pStmt.toString());
			DatastoreParameters dbparams = Finagle_MySQLUtils.getDBParameters(pStmt, dbHost, null, dbName); //DatastoreParameters.product("MySQL").collection(pStmt.getModel()).operation(pStmt.getOperation()).build();
			holder = new NewRelicHolder("MySQL-Prepared-Select");
			holder.addExternal(dbparams);
			
		}
		Future<Seq<T>> result = Weaver.callOriginal();
		
		if(holder == null) {
			return result;
		} else {
			return result.onFailure(new NRFailureFunction(holder)).onSuccess(new NRSuccessFunction<>(holder));
		}
	}
	
}
