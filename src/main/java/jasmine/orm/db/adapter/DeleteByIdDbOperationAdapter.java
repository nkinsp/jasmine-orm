package jasmine.orm.db.adapter;

import jasmine.orm.code.DbContext;
import jasmine.orm.query.Query;

public class DeleteByIdDbOperationAdapter<T> extends DeleteDbOperationAdapter<T>{

	public DeleteByIdDbOperationAdapter(DbContext dbContext, Query<T> query,Object id) {
		super(dbContext, query);
		this.query.where().idEq(id);
		if(tableMapping.isCache()) {
			query.cache(id.toString(), tableMapping.getCacheTime());
		}
	
		
	}

}
