package jasmine.orm.query.impl;

import jasmine.orm.code.DbConfig;
import jasmine.orm.code.DbContext;
import jasmine.orm.query.Query;
import jasmine.orm.table.TableMapping;

/**
 * SQLite 数据库分页
 * @author hanjiang.Yue
 * @param <T>
 */
public class SQLiteDialectQueryImpl<T> extends AbstractSupportQueryImpl<T>{


	
	public SQLiteDialectQueryImpl(TableMapping<T> tableMapping, DbConfig config) {
		super(tableMapping, config);
	}
	
	

	public SQLiteDialectQueryImpl(TableMapping<T> tableMapping, DbContext context) {
		super(tableMapping, context);
	}



	@Override
	public Query<T> limit(int pageNo, int pageSize) {
		return addFilter("LIMIT ? OFFSET ? ").addParams(pageSize,(pageNo-1)*pageSize);
	}
	

}
