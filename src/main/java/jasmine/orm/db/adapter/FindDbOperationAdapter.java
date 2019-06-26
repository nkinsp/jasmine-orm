package jasmine.orm.db.adapter;


import java.util.List;

import jasmine.orm.code.DbContext;
import jasmine.orm.query.Query;

public class FindDbOperationAdapter<T,En> extends FindBeanListDbOperationAdapter<T,En>{


	public FindDbOperationAdapter(DbContext dbContext, Query<T> query, Class<En> enClass) {
		super(dbContext, query, enClass);
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

	@SuppressWarnings("unchecked")
	@Override
	public Object dbAdapter() {
		List<En> list = (List<En>) super.dbAdapter();
		if(list.size() > 0) {
			return list.get(0);
		}
		return null;
	}


	
	

	


	

}
