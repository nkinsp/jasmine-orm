package jasmine.orm.code;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import jasmine.orm.db.DbOperation;
import jasmine.orm.db.DbOperationAdapter;
import jasmine.orm.db.adapter.DeleteBatchDbOperationAdapter;
import jasmine.orm.db.adapter.DeleteByIdDbOperationAdapter;
import jasmine.orm.db.adapter.DeleteByIdsDbOperationAdapter;
import jasmine.orm.db.adapter.DeleteDbOperationAdapter;
import jasmine.orm.db.adapter.FindBeanListDbOperationAdapter;
import jasmine.orm.db.adapter.FindByIdDbOperationAdapter;
import jasmine.orm.db.adapter.FindDbOperationAdapter;
import jasmine.orm.db.adapter.FindListByIdsDbOperationAdapter;
import jasmine.orm.db.adapter.FindMapDbOperationAdapter;
import jasmine.orm.db.adapter.FindMapListDbOperationAdapter;
import jasmine.orm.db.adapter.FindObjectDbOperationAdapter;
import jasmine.orm.db.adapter.FindObjectListDbOperationAdapter;
import jasmine.orm.db.adapter.FindPageDbOperationAdapter;
import jasmine.orm.db.adapter.InsertBatchDbOperationAdapter;
import jasmine.orm.db.adapter.InsertDbOperationAdapter;
import jasmine.orm.db.adapter.PagingDbOperationAdapter;
import jasmine.orm.db.adapter.UpdateBatchDbOperationAdapter;
import jasmine.orm.db.adapter.UpdateDbOperationAdapter;
import jasmine.orm.db.adapter.UpdateEntityMapDbOperationAdapter;
import jasmine.orm.query.Query;
import jasmine.orm.result.Page;
import jasmine.orm.table.TableMapping;
import jasmine.orm.util.EntityUtils;
import jasmine.orm.util.StrUtils;

/** 
 * @author hanjiang.Yue
 * @param <M>
 * @param <Id>
 */
public interface DbRepository<M,Id> {

/**
	 * 实体类型
	 * @author hanjiang.Yue
	 * @return
	 */
	Class<M> modelClass();
	
	/**
	 * 主键类型
	 * @author hanjiang.Yue
	 * @return
	 */
	Class<Id> idClass();
	
	/**
	 * 获取dbContext 对象
	 * @author hanjiang.Yue
	 * @return
	 */
	DbContext dbContext();
	
	
	/**
	 * db 查询原生sql
	 * @return
	 */
	default DbOperation db() {
		return dbContext().getDbOperation();
	}
	
	/**
	 * 创建query执行对象
	 * @author hanjiang.Yue
	 * @param consumer
	 * @return
	 */
	default Query<M> createQuery(Consumer<Query<M>> consumer){
		Query<M> query = createQuery();
		consumer.accept(query);
		return query;
	}
	
	default Query<M>  createQuery(){
		return dbContext().createQuery(modelClass());
	}
	
	/**
	 * 执行存储过程
	 * @author hanjiang.Yue
	 * @param call
	 * @param callableStatementConsumer
	 * @param resultSetFun
	 * @return
	 */
	default <T> T call(String call,Consumer<CallableStatement> callableStatementConsumer,Function<ResultSet,T> resultSetFun) {
		return db().prepareCall(call, callableStatementConsumer, resultSetFun);
	}
	
	/**
	 * 执行
	 * @author hanjiang.Yue
	 * @param adapter
	 * @return
	 */
	default <T> T execute(DbOperationAdapter adapter) {
		try {
			return adapter.adapter();
		} finally {
			adapter = null;
		}
	}
	
	default Query<M>  where(){
		return dbContext().createQuery(modelClass()).where();
	}
	
	/**
	 * 通过id获取
	 * @author hanjiang.Yue
	 * @param id
	 * @return
	 */
	default M find(Id id) {
		return execute(new FindByIdDbOperationAdapter<>(dbContext(), createQuery(), modelClass(), id));
	}
	
	/**
	 * Find Map 
	 * @author hanjiang.Yue
	 * @param consumer
	 * @return
	 */
	default Map<String, Object> findMap(Consumer<Query<M>> consumer){
		return execute(new FindMapDbOperationAdapter<>(dbContext(), createQuery(consumer)));
	}
	
	/**
	 * Find List<Map>
	 * @author hanjiang.Yue
	 * @param consumer
	 * @return
	 */
	default List<Map<String, Object>> findMapList(Consumer<Query<M>> consumer){
		return execute(new FindMapListDbOperationAdapter<>(dbContext(),createQuery(consumer)));
	}
	
	/**
	 * ids 主键
	 * @author hanjiang.Yue
	 * @param ids
	 * @return
	 */
	default List<M> findList(@SuppressWarnings("unchecked") Id...ids) {
		return findList(Arrays.asList(ids));
	}
	
	/**
	 * List id 主键
	 * @author hanjiang.Yue
	 * @param ids
	 * @return
	 */
	default List<M> findList(List<Id> ids) {
		if(ids == null || ids.size() == 0) {
			return Arrays.asList();
		}
		return execute(new FindListByIdsDbOperationAdapter<M>(dbContext(), createQuery(),ids.toArray()));
	}
	
	/**
	 * 获取所有数据
	 * @author hanjiang.Yue
	 * @return
	 */
	default List<M> findAll(){
		return findList(query->{});
	}
	
	
	
	/**
	 * 分页查询
	 * @author hanjiang.Yue
	 * @param pageNo
	 * @param pageSize
	 * @param query
	 * @return
	 */
	default List<M> findList(Integer pageNo,Integer pageSize,Consumer<Query<M>> query){
		return findList(pageNo, pageSize, modelClass(), query);
	}
	
	/**
	 * 分页查询
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	default List<M> findList(Integer pageNo,Integer pageSize){
		return findList(pageNo, pageSize, modelClass(), query->{});
	}
	
	/**
	 * 分页查询
	 * @param pageNo
	 * @param pageSize
	 * @param enClass
	 * @param query
	 * @return
	 */
	default <En> List<En> findList(Integer pageNo,Integer pageSize,Class<En> enClass,Consumer<Query<M>> query){
		return execute(new PagingDbOperationAdapter<>(dbContext(), createQuery(query), enClass, pageNo, pageSize));
	}
	
	/**
	 * 返回实体对象 
	 * @author hanjiang.Yue
	 * @param query
	 * @return
	 */
	default M find(Consumer<Query<M>> query) {
		return execute(new FindDbOperationAdapter<>(dbContext(), createQuery(query),modelClass()));
	}
	
	/**
	 * 
	 * @param enClass
	 * @param query
	 * @return
	 */
	default <En> En find(Class<En> enClass,Consumer<Query<M>> query) {
		return execute(new FindDbOperationAdapter<>(dbContext(), createQuery(query),enClass));
	}
	
	/**
	 * 
	 * @param queryMap
	 * @return
	 */
	default M find(Map<String, Object> queryMap) {
		return find(query->{
			query.where(queryMap);
		});
	}
	
	
	
	/**
	 * 返回列表 数据
	 * @author hanjiang.Yue
	 * @param query
	 * @return
	 */
	 default List<M>  findList(Consumer<Query<M>> consumer){
		 return findList(modelClass(), consumer);
	 }
	 
	 /**
	  * find List<T>
	  * @author hanjiang.Yue
	  * @param classType
	  * @param consumer
	  * @return
	  */
	 default <T> List<T> findList(Class<T> classType,Consumer<Query<M>> consumer){
		 return execute(new FindBeanListDbOperationAdapter<>(dbContext(), createQuery(consumer),classType));
	 }

	
	/**
	 * 返回唯一数据
	 * @author hanjiang.Yue
	 * @param typeClass
	 * @param query
	 * @return
	 */
	default <T> T findUnique(Class<T> typeClass,Consumer<Query<M>> query) {
		return execute(new FindObjectDbOperationAdapter<>(dbContext(), createQuery(query), typeClass));
	}
	
	/**
	 * 返回唯一的列表数据
	 * @author hanjiang.Yue
	 * @param typeClass
	 * @param query
	 * @return
	 */
	default <T> List<T> findUniqueList(Class<T> typeClass,Consumer<Query<M>> query){
		return execute(new FindObjectListDbOperationAdapter<>(dbContext(), createQuery(query), typeClass));
	}
	
	/**
	 * 
	 * @author hanjiang.Yue
	 * @param query
	 * @return
	 */
	default Integer findToInteger(Consumer<Query<M>> consumer) {
		return findUnique(Integer.class, consumer);
	}
	
	default Double findToDouble(Consumer<Query<M>> consumer) {
		return findUnique(Double.class, consumer);
	}
	
	/**
	 * 
	 * @author hanjiang.Yue
	 * @param query
	 * @return
	 */
	default Long findToLong(Consumer<Query<M>> query) {
		return findUnique(Long.class, query);
	}
	
	/**
	 * 
	 * @author hanjiang.Yue
	 * @param query
	 * @return
	 */
	default Integer findCount(Consumer<Query<M>> query) {
		Query<M> createQuery = createQuery(q->{});
		query.accept(createQuery);
		return findToInteger(e->{
			e.count("1");
			String conditions = createQuery.getConditions();
			int indexOf = conditions.toUpperCase().indexOf("ORDER BY");
			if(indexOf !=  -1) {
				conditions = conditions.substring(0, indexOf);
			}
			e.addFilter(conditions);
			e.addParams(createQuery.getParams().toArray());
		});
	}
	
	/**
	 * 
	 * @author hanjiang.Yue
	 * @param query
	 * @return
	 */
	default String findToString(Consumer<Query<M>> query) {
		return findUnique(String.class, query);
	}
	
	/**
	 * 返回 List<String>
	 * @author hanjiang.Yue
	 * @param consumer
	 * @return
	 */
	default List<String> findStringList(Consumer<Query<M>> consumer){
		return findUniqueList(String.class, consumer);
	}
	
	/**
	 * 返回 List<Integer>
	 * @author hanjiang.Yue
	 * @param consumer
	 * @return
	 */
	default List<Integer> findIntegerList(Consumer<Query<M>> consumer){
		return findUniqueList(Integer.class, consumer);
	}
	
	/**
	 * 返回 List<Long>
	 * @author hanjiang.Yue
	 * @param consumer
	 * @return
	 */
	default List<Long> findLongList(Consumer<Query<M>> consumer){
		return findUniqueList(Long.class, consumer);
	}
	
	/**
	 * 返回 List<Double>
	 * @author hanjiang.Yue
	 * @param consumer
	 * @return
	 */
	default List<Double> findDoubleList(Consumer<Query<M>> consumer){
		return findUniqueList(Double.class, consumer);
	}
	
	
	/**
	 * 分页查询
	 * @author hanjiang.Yue
	 * @param pageNo
	 * @param pageSize
	 * @param query
	 * @return
	 */
	default Page<M> findPage(Integer pageNo,Integer pageSize,Consumer<Query<M>> query){
		return findPage(pageNo, pageSize, modelClass(),query);
	}
	
	/**
	 * 分页查询
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	default Page<M> findPage(Integer pageNo,Integer pageSize){
		return findPage(pageNo, pageSize, query->{}); 
	}
	
	/**
	 * 分页查询
	 * @param pageNo
	 * @param pageSize
	 * @param entityClass
	 * @param query
	 * @return
	 */
	default <T> Page<T> findPage(Integer pageNo,Integer pageSize,Class<T> entityClass,Consumer<Query<M>> query){
		return execute(new FindPageDbOperationAdapter<>(dbContext(), createQuery(query), entityClass, pageNo, pageSize));
	}
	
	/**
	 * 条件删除
	 * @author hanjiang.Yue
	 * @param query
	 * @return
	 */
	default int delete(Consumer<Query<M>> consumer) {
		Query<M> query = createQuery(consumer);
		return execute(new DeleteDbOperationAdapter<M>(dbContext(), query));
	}
	
	/**
	 * 通过主键删除
	 * @author hanjiang.Yue
	 * @param id
	 * @return
	 */
	default int delete(Id id) {
		return execute(new DeleteByIdDbOperationAdapter<>(dbContext(),createQuery(),id));
	}
	
	/**
	 * 批量删除
	 * @author hanjiang.Yue
	 * @param ids
	 * @return
	 */
	default int delete(@SuppressWarnings("unchecked") Id...ids) {
		return delete(Arrays.asList(ids));
	}
	
	default int delete(List<Id> ids) {
		return execute(new DeleteByIdsDbOperationAdapter<>(dbContext(), createQuery(), ids.toArray()));
	}
	
	
	
	/**
	 * 添加
	 * @param model  实体对象
	 * @return 返回主键id
	 */
	default Id save(M model) {
		return save(EntityUtils.entityToMap(model));
	}
	
	/**
	 * 添加
	 * @param modelMap
	 * @return
	 */
	default Id save(Map<String, Object> modelMap) {
		Query<M> query = createQuery(q->q.set(modelMap));
		return  execute(new InsertDbOperationAdapter<M>(dbContext(), query));
	}
	
	/**
	 * 批量添加
	 * @param models 插入的实体列表 所有的实体 字段个数 要保持一致
	 */
	default void save(List<M> models) {
		 execute(
				new InsertBatchDbOperationAdapter<M>(
						dbContext(),
						createQuery(q->q.batch(EntityUtils.entityListToMapList(models)))
				)				
		);
	}
	
	/**
	 * 更新
	 * @author hanjiang.Yue
	 * @param model
	 * @return
	 */
	 default int update(M model) {
		return update(EntityUtils.entityToMap(model));
	 }
	
	/**
	 * 批量更新
	 * @author hanjiang.Yue
	 * @param models
	 */
	default void update(List<M> models) {
		if(models == null || models.size() == 0) {
			return;
		}
		execute(
				new UpdateBatchDbOperationAdapter<M>(
						dbContext(),
						createQuery(q->{
							q.batch(EntityUtils.entityListToMapList(models));
						})
				)
		);
	}
	
	/**
	 * 更新
	 * @author hanjiang.Yue
	 * @param query
	 * @return
	 */
	default int update(Consumer<Query<M>> consumer) {
		return execute(new UpdateDbOperationAdapter<M>(dbContext(),createQuery(consumer)));
	}
	
	/**
	 * 更新数据
	 * @author hanjiang.Yue
	 * @param modelMap
	 * @return
	 */
	default int update(Map<String, Object> modelMap) {
		return execute(new UpdateEntityMapDbOperationAdapter<>(dbContext(), createQuery(), modelMap));
	}
	
	
	/**
	 * 添加或者更新
	 * @author hanjiang.Yue
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	default Id saveOrUpdate(M model) {
		TableMapping<M> mapping = DbContext.findTableMapping(modelClass());
		Map<String, Object> entityToMap = EntityUtils.entityToMap(model);
		Id id = null;
		if(entityToMap.containsKey(mapping.getPrimaryKey())) {
			id = (Id) entityToMap.get(mapping.getPrimaryKey());
			update(entityToMap);
		}else {
			id = save(model);
		}
		return id;
	}
	
	
	/**
	 * 
	 * @param fields
	 * @return
	 */
	default Query<M> select(String...fields){
		return createQuery().select(fields);
	}
	
	/**
	 * 执行sql 操作
	 * @param sql
	 * @param params
	 * @return
	 */
	default int execute(String sql,Object...params) {
		return db().update(sql, params);
	}
	
	/**
	 * 执行查询
	 * @param sql
	 * @param fun
	 * @param params
	 * @return
	 */
	default <T> List<T> executeQuery(String sql,Function<ResultSet, T> fun,Object...params) {
		 return db().execute(sql, fun, params);
	}
	
	
}
