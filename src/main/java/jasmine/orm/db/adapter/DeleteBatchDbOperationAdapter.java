package jasmine.orm.db.adapter;

import java.util.List;
import java.util.stream.Collectors;

import jasmine.orm.code.DbContext;
import jasmine.orm.query.Query;

public class DeleteBatchDbOperationAdapter<T> extends DeleteDbOperationAdapter<T> {

	public DeleteBatchDbOperationAdapter(DbContext dbContext, Query<T> query) {
		super(dbContext, query);
	}

	@Override
	public Object cacheAdapter() {
		List<String> keys = query.getParams()
				.stream()
				.map(object->object.toString())
				.map(str->query.getCacheKey(str))
				.collect(Collectors.toList());
		cacheOperation.delete(keys);
		return dbAdapter();
	}

	@Override
	public Object dbAdapter() {
		return super.dbAdapter();
	}






}
