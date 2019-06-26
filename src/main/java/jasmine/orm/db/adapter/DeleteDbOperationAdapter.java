package jasmine.orm.db.adapter;


import jasmine.orm.code.DbContext;
import jasmine.orm.query.Query;

public class DeleteDbOperationAdapter<T> extends AbstractDbOperationAdapter<T>{

	public DeleteDbOperationAdapter(DbContext dbContext, Query<T> query) {
		super(dbContext, query);
	}

	@Override
	public Object cacheAdapter() {
		cacheOperation.delete(query.getCacheKey());
		return dbAdapter();
	}

	@Override
	public Object dbAdapter() {
		String sql = query.getQueryBuilder().buildDeleteSQL();
		if(log.isInfoEnabled()) {
			log.info("==> execute DELETE [sql={},params={}]",sql,query.getParams());
		}
		return  dbOperation.update(sql, query.getParams().toArray());
	}









	

}
