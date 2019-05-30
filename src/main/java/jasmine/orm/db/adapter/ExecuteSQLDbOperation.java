package jasmine.orm.db.adapter;

import java.sql.ResultSet;
import java.util.function.Function;

import jasmine.orm.code.DbContext;
import jasmine.orm.db.DbOperationAdapter;

public class ExecuteSQLDbOperation<T> implements DbOperationAdapter{
	
	public DbContext  context;
	
	private String sql;
	
	private Object[] params;
	
	private Function<ResultSet,T> fun;
	
	public ExecuteSQLDbOperation(DbContext context, String sql, Function<ResultSet,T> fun,Object...params) {
		super();
		this.context = context;
		this.sql = sql;
		this.fun = fun;
		this.params = params;
	}



	@SuppressWarnings("unchecked")
	@Override
	public <R> R adapter() {
	
		 return (R) context.getDbOperation().execute(sql, fun, params);

	}

}
