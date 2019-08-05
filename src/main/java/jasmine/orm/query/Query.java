package jasmine.orm.query;


import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import jasmine.orm.code.DbContext;
import jasmine.orm.db.DbOperationAdapter;
import jasmine.orm.db.adapter.DeleteDbOperationAdapter;
import jasmine.orm.db.adapter.FindBeanListDbOperationAdapter;
import jasmine.orm.db.adapter.FindDbOperationAdapter;
import jasmine.orm.db.adapter.FindPageDbOperationAdapter;
import jasmine.orm.db.adapter.InsertDbOperationAdapter;
import jasmine.orm.db.adapter.PagingDbOperationAdapter;
import jasmine.orm.db.adapter.UpdateDbOperationAdapter;
import jasmine.orm.result.Page;
import jasmine.orm.table.TableMapping;
import jasmine.orm.util.StrUtils;

public interface Query<T> {

	/**
	 * 获取表映射
	 * @author hanjiang.Yue
	 * @return
	 */
	TableMapping<T> getTableMapping();

	/**
	 * @return
	 */
	DbContext getDbContext();
	
	/**
	 * 添加
	 * @author hanjiang.Yue
	 * @param condition
	 * @return
	 */
	Query<T>  condition(String condition);
	
	default Query<T> addFilter(String filter){
		return condition(filter);
	}
	
	String getConditions();
	
	List<String> getColumns();
	
	/**
	 * 添加参数
	 * @author hanjiang.Yue
	 * @param param
	 * @return
	 */
	Query<T> addParam(Object param);
	
	/**
	 * 添加参数 
	 * @author hanjiang.Yue
	 * @param params
	 * @return
	 */
	default Query<T> addParams(Object...params){
		for (Object object : params) {
			addParam(object);
		}
		return this;
	}
	
	/**
	 * AND 
	 * @author hanjiang.Yue
	 * @return
	 */
	default Query<T> and(){
		return condition(" AND ");
	}
	
	/**
	 * AND condition ({sql })
	 * @author hanjiang.Yue
	 * @param sql
	 * @param clasz 
	 * @param consumer
	 * @return
	 */
	 default <R> Query<T> and(String condition, Class<R> tableClass, Consumer<Query<R>> consumer){
		 Query<R> query = getDbContext().createQuery(tableClass);
		 consumer.accept(query);
		 return and().
				      condition(condition).
				      condition(" (").
				      condition(query.getQueryBuilder().buildSelectSQL()).
				      condition(") ").
				      addParams(query.getParams());
	 }
	

	 /**
	  * AND condition ?
	  * @author hanjiang.Yue
	  * @param condition
	  * @param params
	  * @return
	  */
	default Query<T> and(String condition, Object...params){
		return and().condition(condition).condition(" ").addParams(params);
	}
	
	/**
	 * AND field = ? 
	 * @author hanjiang.Yue
	 * @param field
	 * @param value
	 * @return
	 */
	default Query<T> andEq(String field, Object value){
		return and().eq(field, value);
	}
	
	/**
	 * AND field > ?
	 * @author hanjiang.Yue
	 * @param field
	 * @param value
	 * @return
	 */
	default Query<T> andGt(String field, Object value){
		return and().gt(field, value);
	}
	
	/**
	 * field > ?
	 * @author hanjiang.Yue
	 * @param field
	 * @param value
	 * @return
	 */
	default Query<T> gt(String field, Object value){
		return condition(field).condition(" > ? ").addParam(value);
	}
	
	/**
	 * field >= ?
	 * @author hanjiang.Yue
	 * @param field
	 * @param value
	 * @return
	 */
	default Query<T> ge(String field,Object value){
		return condition(field).condition(" >= ? ").addParam(value);
	}
	
	/**
	 * AND field 
	 * @author hanjiang.Yue
	 * @param field
	 * @param values
	 * @return
	 */
	default Query<T> andIn(String field, Object[] values){
		return and().in(field, values);
	}
	
	
	/**
	 * AND (conditions)
	 * @author hanjiang.Yue
	 * @param consumer
	 * @return
	 */
	 default Query<T> and(Consumer<Query<T>> consumer){
		 and();
		 condition(" (");
		 consumer.accept(this);
		 condition(") ");
		 return this;
	 }
		
	
	
	/**
	 * 
	 * @author hanjiang.Yue
	 * @param field
	 * @param value
	 * @return
	 */
	default Query<T> andLike(String field, Object value){
		return and("LIKE ? ", value);
	}
	
	/**
	 * 
	 * @author hanjiang.Yue
	 * @param field
	 * @param value
	 * @return
	 */
	default Query<T> like(String field,Object value){
		return condition("LIKE ? ").addParam(value);
	}
	
	/**
	 * 
	 * @author hanjiang.Yue
	 * @param field
	 * @param value
	 * @return
	 */
	default Query<T> andLt(String field, Object value){
		return and().lt(field, value);
	}
	
	/**
	 * field < ?
	 * @author hanjiang.Yue
	 * @param field
	 * @param value
	 * @return
	 */
	default Query<T> lt(String field,Object value){
		return condition(field).condition(" <  ? ").addParam(value);
	}
	
	/**
	 * field <= ?
	 * @author hanjiang.Yue
	 * @param field
	 * @param value
	 * @return
	 */
	default Query<T> le(String field,Object value){
		return condition(field).condition(" <=  ? ").addParam(value);
	}
	
	/**
	 * exists ()
	 * @param sqlstr
	 * @param params
	 * @return
	 */
	default Query<T> exists(String sqlstr,Object...params){
		return condition("EXISTS (").condition(sqlstr).condition(") ").addParams(params);
	}
	
	/**
	 * exists
	 * @param query
	 * @return
	 */
	default <En> Query<T> exists(Query<En> query){
		return exists(query.getQueryBuilder().buildSelectSQL(), query.getParams().toArray());
	}
	
	/**
	 * exists
	 * @param tableClass
	 * @param consumer
	 * @return
	 */
	default <En> Query<T> exists(Class<En> tableClass,Consumer<Query<En>> consumer ){
		Query<En> query = getDbContext().createQuery(tableClass);
		consumer.accept(query);
		return exists(query.getQueryBuilder().buildSelectSQL(), query.getParams().toArray());
	}
	
	default Query<T> not(){
		return condition(" NOT ");
	}
	
	/**
	 * AND field NOT IN (?,?,?)
	 * @author hanjiang.Yue
	 * @param field
	 * @param values
	 * @return
	 */
	default Query<T> andNotIn(String field, Object...values){
		return and().notIn(field, values);
	}
	
	/**
	 *  field not in (?,?,?) 
	 * @author hanjiang.Yue
	 * @param field
	 * @param values
	 * @return
	 */
	default Query<T> notIn(String field,Object...values){
		return condition(" NOT ").in(field, values);
	}
	
	
	
	/**
	 * field = ?
	 * @author hanjiang.Yue
	 * @param field
	 * @param value
	 * @return
	 */
	default Query<T> eq(String field, Object value){
		return condition(field).condition(" = ? ").addParam(value);
	}
	
	/**
	 * field != ?
	 * @param field
	 * @param value
	 * @return
	 */
	default Query<T> ne(String field,Object value){
		return condition(field).condition(" != ? ").addParam(value);
	}
	
	/**
	 * and field != ? 
	 * @param field
	 * @param value
	 * @return
	 */
	default Query<T> andNe(String field,Object value){
		return and().ne(field, value);
	}
	
	/**
	 * 获取参数
	 * @return
	 */
	List<Object> getParams();
	
	default Query<T> group(String group){
		return condition("GROUP BY ").condition(group).condition(" ");
	}
	
	/**
	 * id = ?
	 * @author hanjiang.Yue
	 * @param value
	 * @return
	 */
	default Query<T> idEq(Object value){
		return eq(getTableMapping().getPrimaryKey(), value);
	}
	
	
	/**
	 * field IN (?,?)
	 * @author hanjiang.Yue
	 * @param field
	 * @param values
	 * @return
	 */
	default Query<T> in(String field, Object...values){
		if(values.length == 0 ) {
			return this;
		}
		condition(field);
		condition(" IN (");
		for (int i = 0; i < values.length; i++) {
			if (i > 0) {
				condition(",");
			}
			condition("?");
		}
		condition(") ");
		addParams(values);
		return this;
	}
	
	/**
	 * in
	 * @param field
	 * @param tableClass
	 * @param consumer
	 * @return
	 */
	default <R> Query<T> in(String field,Class<R> tableClass,Consumer<Query<R>> consumer){
		Query<R> query = getDbContext().createQuery(tableClass);
		consumer.accept(query);
		return in(field, query);
	}
	
	
	default <R> Query<T> in(String field,Query<R> query){
		return condition(field).
					condition(" IN (").
					condition(query.getQueryBuilder().buildSelectSQL()).
					condition(") ").
					addParams(query.getParams().toArray());
	}
	
	default <R> Query<T> andIn(String field,Class<R> tableClass,Consumer<Query<R>> query){
		return and().in(field, tableClass, query);
	}
	
	default Query<T> innerjoin(Class<?> table, String condition){
		TableMapping<?> mapping = DbContext.findTableMapping(table);
		return innerjoin(mapping.getTableName(), condition).condition(" ");
	}
	
	default Query<T> innerjoin(String table, String condition){
		return condition(" INNER JOIN ").condition(table).on(condition).condition(" ");
	}
	
	default <R>  Query<T> leftjoin(String conditions){
		return  condition(" LEFT JOIN ").condition("("+conditions+") ");
	}
	
	default <R> Query<T> leftjoin(Class<R> tableClass, String condition){
		TableMapping<R> mapping = DbContext.findTableMapping(tableClass);
		return leftjoin(mapping.getTableName(), condition);
	} 
	
	
	default <R> Query<T> leftjoin(Class<R> tableClass,String asStr,String condition){
		TableMapping<R> mapping = DbContext.findTableMapping(tableClass);
		return leftjoin(mapping.getTableName(), asStr,condition);
	}
	
	default Query<T> leftjoin(String table, String condition){
		return condition(" LEFT JOIN ").condition(table).on(condition);
	}
	
	default Query<T> leftjoin(String table,String asStr,String condition){
		return condition(" LEFT JOIN ").
				    condition(table).
				    condition(" AS ").
				    condition(asStr).
				    condition(" ").
				    on(condition);
	}
	
	default Query<T> on(String condition){
		return condition(" ON ").condition(condition);
	}
	
	
	/**
	 * Limit 默认分页
	 * @author hanjiang.Yue
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	default Query<T> limit(int pageNo, int pageSize){
		return condition("LIMIT ? , ? ").addParams(pageNo,pageSize);
	}
	
	
	/**
	 * order by 
	 * @author hanjiang.Yue
	 * @param orderBy
	 * @return
	 */
	default Query<T> order(String orderBy){
		return condition("ORDER BY ").condition(orderBy).condition(" ");
	}
	
	default Query<T> rightjoin(Class<?> table, String condition){
		TableMapping<?> mapping = DbContext.findTableMapping(table);
		return rightjoin(mapping.getTableName(), condition);
	}
	
	default Query<T> rightjoin(String table, String condition){
		return condition("RIGHT JOIN ").condition(table).on(condition);
	}
	
	default  Query<T> join(Class<?>...tableClass){
		for (Class<?> table : tableClass) {
			TableMapping<?> mapping = DbContext.findTableMapping(table);
			condition(",").
			condition(mapping.getTableName()).
			condition(" ");
		}
		return this;
	}
	
	default  Query<T> join(String...tables){
		for (String table : tables) {
			condition(",").
			condition(table).
			condition(" ");
		}
		return this;
	}
	
	/**
	 * 查询的字段 
	 * @author hanjiang.Yue
	 * @param fields
	 * @return
	 */
	 Query<T> select(List<String> fields);
	
	/**
	 * 查询的字段 
	 * @author hanjiang.Yue
	 * @param field
	 * @return
	 */
	default Query<T> select(String field){
		return select(field.split(","));
	}
	
	/**
	 * @author hanjiang.Yue
	 * @param fields
	 * @return
	 */
	default Query<T> select(String...fields){
		return select(Arrays.asList(fields));
	}
	
	/**
	 * 查询 
	 * @author hanjiang.Yue
	 * @param alias 别名
	 * @param fields 字段名称
	 * @return
	 */
	default Query<T> select(String alias,List<String> fields){
		return select(fields.stream().map(str->alias+"."+str).collect(Collectors.toList()));
	}
	
	/**
	 * 不需要查询的列名
	 * @author hanjiang.Yue
	 * @param fields
	 * @return
	 */
	Query<T> selectExcludes(String...fields);
	
	/**
	 * 更新的字段
	 * @author hanjiang.Yue
	 * @param fields
	 * @return
	 */
	Query<T> set(Map<String, Object> fields);
	
	/**
	 * 更新字段
	 * @author hanjiang.Yue
	 * @param t
	 * @return
	 */
	Query<T> set(T t);
	
	/**
	 * 更新的字段
	 * @author hanjiang.Yue
	 * @param field
	 * @param value
	 * @return
	 */
	Query<T> set(String field, Object value);
	
	/**
	 * 更新字段 ex (set age = (age + ?) )  .. 1
	 * @author hanjiang.Yue
	 * @param field
	 * @param sql
	 * @param params
	 * @return
	 */
	Query<T> setSQLStr(String field,String sql,Object...params);
	
	
	
	/**
	 * 批量更新或者新增的数据
	 * @author hanjiang.Yue
	 * @param batch
	 * @return
	 */
	Query<T> batch(List<Map<String, Object>> batch);
	
	/**
	 *  Table AS alias
	 * @author hanjiang.Yue
	 * @param alias
	 * @return
	 */
	default Query<T> alias(String alias){
		return condition(" AS ").condition(alias);
	}
	
	/**
	 * 构建一个空的 where
	 * @author hanjiang.Yue
	 * @return
	 */
	default Query<T> where(){
		return condition("WHERE ");
	}
	
	
	/**
	 *  where 
	 * @author hanjiang.Yue
	 * @param condition
	 * @param params
	 * @return
	 */
	default Query<T> where(String condition, Object...params){
		return 
				where().
				condition(condition).
				condition(" ").
				addParams(params)
			;
	}
	
	<R> Query<T> where(String condition,Class<R> tableClass,Consumer<Query<R>> where);
	
	
	
	/**
	 * where map
	 * @author hanjiang.Yue
	 * @param whereMap
	 * @return
	 */
	default Query<T> where(Map<String, Object> whereMap){
		where();
		int i = 0;
		for (Entry<String, Object> en : whereMap.entrySet()) {
			if(i > 0) {
				and();
			}
			eq(en.getKey(), en.getValue());
			i++;
		}
		return this;
	}

	/**
	 * id in (?,?,?)
	 * @author hanjiang.Yue
	 * @param ids
	 * @return
	 */
	default Query<T> idIn(Object...ids){
		return in(getTableMapping().getPrimaryKey(), ids);
	}
	
	default <R> Query<T> idIn(Class<R> tableClass,Consumer<Query<R>> query){
		return in(getTableMapping().getPrimaryKey(), tableClass, query);
	}
	
	default Query<T> between(String field,Object start,Object end){
		return condition(field).condition(" BETWEEN  ? AND ? ").addParams(start,end);
	}
	
	default Query<T> andBetween(String field,Object start,Object end){
		return and().between(field, start, end);
	}
	
	default Query<T> isNull(String field){
		return condition(field).condition(" is NULL ");
	}
	
	default Query<T> isNotNull(String field){
		return condition(field).condition(" is NOT NULL ");
	}
	
	default Query<T> andIsNotNull(String field){
		return and().isNotNull(field);
	}
	
	default Query<T> andIsNull(String field){
		return and().isNull(field);
	}
	
	default Query<T> andThen(boolean eq,Consumer<Query<T>> query){
		if(eq) {
			and();
			query.accept(this);
		}
		return this;
	}

	
	default Query<T> andNotEmptyThen(Object value,Consumer<Query<T>> query){
		if(!StrUtils.isEmpty(value)) {
			and();
			query.accept(this);
		}
		return this;
	}
	
	
	/**
	 * 是否缓存
	 * @author hanjiang.Yue
	 * @return
	 */
	boolean isCache();
	
	String  getCacheKey();
	
	String  getCacheKey(String value);
	
	long getCacheTime();
	
	default Query<T>  cache(){
		return cache(-1);
	}
	
	Query<T>  cache(String key,long second);
	
	default Query<T>  cache(String key){
		return cache(key, -1);
	}
	
	
	
	default Query<T>  cache(long second) {
		if(getParams().size() > 1) {
			Object[] paramArray = getParams().toArray();
			Arrays.sort(paramArray);
			String key = Arrays.asList(paramArray).stream().map(param->param.toString()).collect(Collectors.joining(","));
			return cache(key,second);
		}
		if(getParams().size() == 1) {
			return cache(getParams().get(0).toString(), second);
		}
		return this;
	}
	
	default Query<T> having(String having){
		return condition("HAVING ").condition(having).condition(" ");
	}

	default Query<T> count(String count){
		this.select("COUNT("+count+")");
		return this;
	}
	
	default Query<T> or(String orStr){
		return this.condition(" (")
			   .condition(orStr)
			   .condition(") ");
	}
	
	default Query<T> or(Consumer<Query<T>> query){
		condition("(");
		query.accept(this);
		condition(") ");
		return this;
	}
	
	List<Map<String, Field>> getFiledBatch();
	
	Map<String, Field> getFieldMap();
	
	QueryBuilder getQueryBuilder();
	
	default List<Object[]> getArrayParams(){
		return getParams().stream().map(object->(Object[])object).collect(Collectors.toList());
	}
	
	default T find() {
		return find(getTableMapping().getTableClass());
	}
	
	default <En> En find(Class<En> entityClass) {
		return execute(new FindDbOperationAdapter<>(getDbContext(), this,entityClass));
	}
	
	default List<T> findList() {
		return findList(getTableMapping().getTableClass());
	}
	
	default <En> List<En> findList(Class<En> entityClass){
		return execute(new FindBeanListDbOperationAdapter<>(getDbContext(),this,entityClass));
	}
	
	default List<T> findList(int pageNo,int pageSize){
		return findList(pageNo, pageSize, getTableMapping().getTableClass());
	}
	
	default <En> List<En> findList(int pageNo,int pageSize,Class<En> enClass){
		return execute(new PagingDbOperationAdapter<>(getDbContext(), this,enClass, pageNo, pageSize));
	} 
	
	default Page<T> findPage(int pageNo,int pageSize){
		return findPage(pageNo, pageSize, getTableMapping().getTableClass());
	}
	
	default <En> Page<En> findPage(int pageNo,int pageSize,Class<En> enClass){
		return execute(new FindPageDbOperationAdapter<>(getDbContext(), this, enClass, pageNo, pageSize));
	}
	
	default int delete() {
		return execute(new DeleteDbOperationAdapter<>(getDbContext(), this));
	}
	
	default int update() {
		return execute(new UpdateDbOperationAdapter<>(getDbContext(), this));
	}
	
	default int insert() {
		return execute(new InsertDbOperationAdapter<>(getDbContext(), this));
	}
	
	default  <R> R execute(DbOperationAdapter adapter) {
		return adapter.adapter();
	}
	
	
	 
}
