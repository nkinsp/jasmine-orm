package jasmine.orm.db.adapter;

import java.util.List;
import java.util.stream.Collectors;

import jasmine.orm.activerecord.MapActiveRecord;
import jasmine.orm.code.DbContext;
import jasmine.orm.query.Query;
import jasmine.orm.util.ClassUtils;

public class FindBeanListDbOperationAdapter<T,En> extends AbstractDbOperationAdapter<T>{

	private Class<En> enClass;
	
	public FindBeanListDbOperationAdapter(DbContext dbContext, Query<T> query,Class<En> enClass) {
		super(dbContext, query);
		this.enClass = enClass;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object cacheAdapter() {
		String cacheKey = query.getCacheKey();
		log.info("cacheKey == > [{}]",cacheKey);
		List<En> list = cacheOperation.list(enClass, cacheKey);
		if(list == null || list.size() == 0) {
			synchronized (query) {
				list = (List<En>) dbAdapter();
				if(list != null && list.size() > 0) {
					cacheOperation.put(cacheKey,list);
				}
			}
		}
		return list;
	}
	
	protected String builderQuerySql() {
		String sql = query.getQueryBuilder().buildSelectSQL();
		return sql;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object dbAdapter() {
		String sql = builderQuerySql();
		log.info("==> execute [sql={},params={}]",sql,query.getParams());
		if(MapActiveRecord.class.isAssignableFrom(enClass)) {
			return dbOperation.queryListMap(sql, query.getParams().toArray()).stream().map(map->{
				MapActiveRecord activeRecord = (MapActiveRecord) ClassUtils.newInstance(enClass);
				activeRecord.putAll(map);
				return activeRecord;
			}).collect(Collectors.toList());
		}
		return dbOperation.queryBeanList(enClass, sql, query.getParams().toArray());
	}




}
