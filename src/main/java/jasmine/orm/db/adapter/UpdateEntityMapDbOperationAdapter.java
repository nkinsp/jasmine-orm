package jasmine.orm.db.adapter;

import java.util.Map;

import jasmine.orm.code.DbContext;
import jasmine.orm.query.Query;
import jasmine.orm.util.StrUtils;

public class UpdateEntityMapDbOperationAdapter<T> extends UpdateDbOperationAdapter<T> {

	public UpdateEntityMapDbOperationAdapter(DbContext dbContext, Query<T> query,Map<String, Object> entityMap) {
		super(dbContext, query);
		String idname = tableMapping.getPrimaryKey();
		Object id = entityMap.get(idname);
		if(StrUtils.isEmpty(id)) {
			throw new RuntimeException("No ID found ");
		}
		entityMap.forEach((k,v)->{
			if(!idname.equals(k)) {
				this.query.set(tableMapping.getColumnName(k), v);
			}
		});
		if(tableMapping.isCache()) {
			this.query.cache(id.toString(), tableMapping.getCacheTime());
		}
		this.query.where().idEq(id);
	}

}
