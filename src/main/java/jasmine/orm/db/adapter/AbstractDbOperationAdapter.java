package jasmine.orm.db.adapter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jasmine.orm.cache.CacheOperation;
import jasmine.orm.code.DbContext;
import jasmine.orm.db.DbOperation;
import jasmine.orm.db.DbOperationAdapter;
import jasmine.orm.query.Query;
import jasmine.orm.query.QueryBuilder;
import jasmine.orm.table.TableMapping;
/**
 * @author hanjiang.Yue
 * @param <T>
 */
public abstract class AbstractDbOperationAdapter<T> implements DbOperationAdapter{

	public  DbContext context;
	
	public  Query<T> query;
	
	public  CacheOperation cacheOperation;
	
	public  DbOperation dbOperation;
	
	public  TableMapping<T> tableMapping;
	
	public  Logger log = LoggerFactory.getLogger(getClass());
	
	
	/**
	 * 执行缓存操作
	 * @author hanjiang.Yue
	 * @param query
	 * @return
	 */
	public abstract Object cacheAdapter();

	
	
	/**
	 * 执行数据库操作
	 * @author hanjiang.Yue
	 * @param query
	 * @return
	 */
	public  abstract Object dbAdapter();
	
	

	@SuppressWarnings("unchecked")
	@Override
	public <R> R adapter() {
		try {
			if (query.isCache()) {
				log.info("query cache ");
				return (R) cacheAdapter();
			}
			return (R) dbAdapter();
		} finally {
			//释放query
			 QueryBuilder queryBuilder = (QueryBuilder) query;
			 queryBuilder.release();
			 query = null;
		}
	}

	public AbstractDbOperationAdapter(DbContext context, Query<T> query) {
		super();
		this.context = context;
		this.cacheOperation = context.getCacheOperation();
		this.dbOperation = context.getDbOperation();
		this.query = query;
		this.tableMapping = this.query.getTableMapping();
		if(!this.query.isCache()) {
			if(this.tableMapping.isCache()) {
				this.query.cache(this.tableMapping.getCacheTime());
			}
		}
	}


	
	
	
	
}
