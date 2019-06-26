package jasmine.orm.db.adapter;

import java.util.List;
import java.util.stream.Collectors;

import jasmine.orm.code.DbContext;
import jasmine.orm.query.Query;

public class UpdateBatchDbOperationAdapter<T> extends AbstractDbOperationAdapter<T>{

	public UpdateBatchDbOperationAdapter(DbContext dbContext, Query<T> query) {
		super(dbContext, query);
		if(tableMapping.isCache()) {
			this.query.cache("",tableMapping.getCacheTime());
		}
	}

	@Override
	public Object cacheAdapter() {
		List<String> keys = query.getFiledBatch()
				.stream()
				.map(data->query.getCacheKey(data.get(tableMapping.getPrimaryKey()).toString()))
				.distinct()
				.collect(Collectors.toList());
		cacheOperation.delete(keys);
		return dbAdapter();
	}

	@Override
	public Object dbAdapter() {
		List<Object[]> params = query.getArrayParams();
		String updateSQL = query.getQueryBuilder().buildBatchUpdateSQL();
		if(log.isInfoEnabled()) {
			log.info("==> execute [sql={},params={}]",updateSQL,params);
		}
		return dbOperation.batchUpdate(updateSQL,params);
	}




}
