package jasmine.orm.db.adapter;

import jasmine.orm.code.DbContext;
import jasmine.orm.query.Query;

public class DeleteByIdsDbOperationAdapter<T> extends DeleteBatchDbOperationAdapter<T>{

	public DeleteByIdsDbOperationAdapter(DbContext dbContext, Query<T> query,Object[] ids) {
		super(dbContext, query);
		this.query.where().idIn(ids);
		if(tableMapping.isCache()) {
			this.query.cache("",tableMapping.getCacheTime());
		}
	}

}
