package jasmine.orm.db.adapter;

import java.util.List;

import jasmine.orm.code.DbContext;
import jasmine.orm.query.Query;

/**
 * 批量添加
 * @author hanjiangyue
 *
 * @param <T>
 */
public class InsertBatchDbOperationAdapter<T> extends AbstractDbOperationAdapter<T>{

	public InsertBatchDbOperationAdapter(DbContext dbContext, Query<T> query) {
		super(dbContext, query);
	}
	
	

	@Override
	public Object dbAdapter() {
		String sql = query.getQueryBuilder().buildBatchInsertSQL();
		List<Object[]> params = query.getArrayParams();
		log.info("==> execute [sql={}]",sql);
		return dbOperation.batchUpdate(sql, params);
	}



	@Override
	public Object cacheAdapter() {
		
		return null;
	}







	

}
