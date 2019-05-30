package jasmine.orm.query.impl;

import jasmine.orm.code.DbConfig;
import jasmine.orm.query.Query;
import jasmine.orm.table.TableMapping;

public class SQLServerDialectQueryImpl<T> extends AbstractSupportQueryImpl<T>{

	public SQLServerDialectQueryImpl(TableMapping<T> tableMapping, DbConfig config) {
		super(tableMapping, config);
	}
	
	@Override
	public Query<T> limit(int pageNo, int pageSize) {
		return condition("OFFSET ? ROWS FETCH NEXT ? ONLY ").addParams(pageNo,pageSize);
	}

}
