package jasmine.orm.db.adapter;


import java.util.List;

import jasmine.orm.code.DbContext;
import jasmine.orm.query.Query;

public class FindDbOperationAdapter<T> extends FindListByIdsDbOperationAdapter<T>{

	public FindDbOperationAdapter(DbContext dbContext, Query<T> query) {
		super(dbContext, query);
	}

	@Override
	public Object cacheAdapter() {
		Object result = cacheOperation.get(tableMapping.getTableClass(), query.getCacheKey());
		if(result == null) {
			result = dbAdapter();
			if(result != null) {
				cacheOperation.put(query.getCacheKey(),query.getCacheTime(),result);
			}
		}
		return result;
	}

	@Override
	public Object dbAdapter() {
		List<T> list = query(query);
		if(list.size() > 0) {
			return list.get(0);
		}
		return null;
	}


	
	

	


	

}
