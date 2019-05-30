package jasmine.orm.query.impl;


import jasmine.orm.code.DbConfig;
import jasmine.orm.code.DbContext;
import jasmine.orm.table.TableMapping;

/**
 * mysql  数据库分页
 * @author hanjiang.Yue
 *
 * @param <T>
 */
public class MySqlDialectQueryImpl<T> extends AbstractSupportQueryImpl<T>{

	public MySqlDialectQueryImpl(TableMapping<T> tableMapping,DbConfig config) {
		super(tableMapping, config);
	}

	public MySqlDialectQueryImpl(TableMapping<T> tableMapping, DbContext context) {
		super(tableMapping, context);
	}

	
	




}
