package jasmine.orm.db.adapter;

import java.sql.ResultSet;
import java.util.function.Function;

import jasmine.orm.code.DbContext;
import jasmine.orm.query.Query;

public class ExecuteResultSetDbOperationAdapter<T,R> extends AbstractDbOperationAdapter<T>{

	private Function<ResultSet, R> callback;
	
	public ExecuteResultSetDbOperationAdapter(DbContext context, Query<T> query) {
		super(context, query);
	}
	

	public ExecuteResultSetDbOperationAdapter(DbContext context, Query<T> query, Function<ResultSet, R> callback) {
		super(context, query);
		this.callback = callback;
	}




	@Override
	public Object cacheAdapter() {
		return null;
	}

	@Override
	public Object dbAdapter() {
		return dbOperation.execute(query.getQueryBuilder().buildSelectSQL(), this.callback, query.getParams());
		
	}


	

}
