package jasmine.orm.query.impl;

import jasmine.orm.code.DbConfig;
import jasmine.orm.table.TableMapping;

/**
 * h2database 
 * @author  hanjiang.Yue
 *
 * @param <T>
 */
public class H2DialectQueryImpl<T> extends AbstractSupportQueryImpl<T>{

	public H2DialectQueryImpl(TableMapping<T> tableMapping, DbConfig config) {
		super(tableMapping, config);
	}

}
