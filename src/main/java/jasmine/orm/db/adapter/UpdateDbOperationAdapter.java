package jasmine.orm.db.adapter;

import java.util.List;

import jasmine.orm.code.DbContext;
import jasmine.orm.query.Query;

public class UpdateDbOperationAdapter<T> extends AbstractDbOperationAdapter<T>{

	public UpdateDbOperationAdapter(DbContext dbContext, Query<T> query) {
		super(dbContext, query);
	}

	@Override
	public Object cacheAdapter() {
		cacheOperation.delete(query.getCacheKey());
		return dbAdapter();
	}

	@Override
	public Object dbAdapter() {
		List<Object> params = query.getParams();
//		params.addAll(query.getParams());
		String sql = query.getQueryBuilder().buildUpdateSQL();
		log.info("==> execute [sql={},params={}]",sql,params);
		return dbOperation.update(sql,params.toArray());
	}

	

	
}
