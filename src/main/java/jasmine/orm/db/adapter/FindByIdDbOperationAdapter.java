package jasmine.orm.db.adapter;

import jasmine.orm.code.DbContext;
import jasmine.orm.query.Query;

public class FindByIdDbOperationAdapter<T,En> extends FindDbOperationAdapter<T, En>{

	
	public FindByIdDbOperationAdapter(DbContext dbContext, Query<T> query, Class<En> enClass,Object id) {
		super(dbContext, query, enClass);
		this.query.where().idEq(id);
		if(tableMapping.isCache()) {
			this.query.cache(id.toString(),tableMapping.getCacheTime());
		}
	}

}
