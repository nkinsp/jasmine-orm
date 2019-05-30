package jasmine.orm.db.adapter;

import jasmine.orm.code.DbContext;
import jasmine.orm.query.Query;

public class DbOperationAdapterProxy<T> extends AbstractDbOperationAdapter<T>{

	public DbOperationAdapterProxy(DbContext context, Query<T> query) {
		super(context, query);
	}


	private AbstractDbOperationAdapter<T> dbOperationAdapter;
	
	
	@Override
	public <R> R adapter() {
		return dbOperationAdapter.adapter();
	}


	@Override
	public Object cacheAdapter() {
		return null;
	}


	@Override
	public Object dbAdapter() {
		return dbOperationAdapter.dbAdapter();
	}



	

}
