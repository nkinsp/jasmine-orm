# jasmine-orm

# 特性

* **强大的 CRUD** 内置操作操作模板 可实现大部分的curd 操作
* **支持多种数据库** MySQL、MariaDB、Oracle、DB2,SQLSERVER2012 ,SQLite,H2
* **支持主键自动生成** 支持多达 4 种主键策略（内含分布式唯一 ID 生成器 - Sequence），可自由配置，完美解决主键问题
* **支持 ActiveRecord 模式** 支持 ActiveRecord 形式调用，实体类只需继承 Model 类即可进行强大的 CRUD 操作
* **无xml** 没有xml文件 无需扫描
* **快速启动**
* **内置分页**
* **内置缓存操作** HashMap ,J2ECache,Redis 等 可以自定义缓存

# 使用方式

#### 在springboot 中使用

```java
@Bean
public DbContext dbContext(DataSource dataSource) {
	return new DbContext(dataSource);
}
```
#### 在springmvc 中使用
```xml
<bean id="dbContext" class="jasmine.orm.code.DbContext">
	<property name="dataSource" ref="dataSource"></property>
</bean>
```
# CRUD操作
#### 传统Dao 使用方式
```java
@Table
public class User{
	
	private Integer id;
	private Integer age;
	private String name;
	//省略get set
}
@Repository
public class UserRepository extends SpringJdbcTemplate<User, Integer>{

}

//查询单个对象 select id,age,name from user where id = ? [100]
User user = userRepository.find(100);
//多个查询 select id,age,name from user where id in (?,?,?) [100,100]
List<User> users = userRepository.findList(100,100);
//条件查询 select id,age,name from user where age > ? [18]
List<User> users = userRepository.findList(1, 20, query->query.where().gt("age", 18));

//添加数据
Integer id = userRepository.save(new User(20, "李四"));
//批量添加数据
userRepository.save(Arrays.asList(
	new User(20, "李四"),
	new User(25, "张三"),
	new User(20, "王五")
));

//删除
userRepository.delete(100);
//批量删除
userRepository.delete(100,101,102);

//更新
userRepository.update(user);
```
### ActiveRecord
```java
@Table
public class User  extends ActiveRecord<User, Integer>{
	
	private Integer id;
	private Integer age;
	private String name;
	//省略get set
	
}
```
### 直接使用
```java
@Autowired
private DbContext dbContext;

public void test() {
	
	dbContext.table(User.class);
}
```
### 添加
```java
/**
* 批量添加
* @param models 插入的实体列表 所有的实体 字段个数 要保持一致
*/
void save(List<M> models); 
/**
* 添加
* @param model  实体对象
* @return 返回主键id
*/
Id save(M model);
/**
* 添加
* @param modelMap
* @return
*/
Id save(Map<String, Object> modelMap)；    
```
### 修改
```java
/**
*更新数据
*@param consumer lambda Query对象 
*@return 受影响行数
*/
int update(Consumer<Query<M>> consumer);
/**
*批量更新数据
*@params models 所有实体字段个数保持一致
*/
void update(List<M> models);
/**
*@params model 实体对象
*@return 受影响行数
*/
int update(M model);
/**
*@params model Map 对象
*@return 受影响行数
*/
int update(Map<String, Object> modelMap);
```
### 删除
```java
/**
* 条件删除
* @param consumer lambda Query对象 
* @return 受影响行数
*/
int delete(Consumer<Query<M>> consumer);

/**
* 通过主键删除
* @param id
* @return 受影响行数
*/
int delete(Id id);

/**
* 批量删除
* @param ids
* @return 受影响行数
*/
int delete(Id...ids);

/**
* 批量删除
* @param ids
* @return 受影响行数
*/
int delete(List<Id> ids);
```
### 查询

* [find](#find)
* [findMap](#findmap)
* [findMapList](#findmaplist)
* [findList](#findlist)
* [findAll](#findall)
* [findUnique](#findunique)
* [findUniqueList](#finduniquelist)
* [findToDouble](#findtodouble)
* [findToInteger](#findtointeger)
* [findToLong](#findtolong)
* [findToString](#findtostring)
* [findStringList](#findstringlist)
* [findIntegerList](#findintegerlist)
* [findLongList](#findlonglist)
* [findDoubleList](#finddoublelist)


### find ###

```java
/**
* 通过id获取
* @param id
* @return 实体对象
*/
M find(Id id);

/**
* 条件查询 返回实体对象
* @param  consumer lambda Query对象 
* @return
*/
M find(Consumer<Query<M>> consumer);

/**
*  条件查询 返回实体对象
* @param enClass 实体对象的class
* @param query consumer  lambda Query对象 
* @return
*/
<En> En find(Class<En> enClass,Consumer<Query<M>> consumer)；

/**
*  根据Map 生成条件  key=? and key=?
* @param queryMap
* @return
*/
M find(Map<String, Object> queryMap)；

```

### findMap ###

```java
/**
* 条件查询返回Map
* @param consumer lambda Query对象 
* @return Map
*/
Map<String, Object> findMap(Consumer<Query<M>> consumer)；

```
### findMapList ###
```java
/**
* 条件查询返回多个Map对象
* @param consumer lambda Query对象 
* @return List<Map>
*/
List<Map<String, Object>> findMapList(Consumer<Query<M>> consumer);
```
### findList ###

```java
/**
* 通过主键批量获取
* @param ids
* @return
*/
List<M> findList(Id...ids);

/**
* 通过主键批量获取
* @param ids
* @return
*/
List<M> findList(List<Id> ids);

/**
* 分页查询
* @param pageNo
* @param pageSize
* @param consumer lambda Query对象 
* @return
*/
List<M> findList(Integer pageNo,Integer pageSize,Consumer<Query<M>> consumer);

/**
* 分页查询
* @param pageNo
* @param pageSize
* @return
*/
List<M> findList(Integer pageNo,Integer pageSize);

/**
* 分页查询
* @param pageNo
* @param pageSize
* @param enClass 返回实体对象的Class
* @param consumer lambda Query对象 
* @return
*/
<En> List<En> findList(Integer pageNo,Integer pageSize,Class<En> enClass,Consumer<Query<M>> consumer);

/**
*  返回列表数据
* @param consumer lambda Query对象 
* @return
*/
List<M> fndList(Consumer<Query<M>> consumer);

/**
* 返回列表数据
* @param enClass 实体对象的class
* @param consumer lambda Query对象
* @return
*/
<T> List<T> findList(Class<T> enClass,Consumer<Query<M>> consumer)；
```
### findAll ###
```java
/**
* 获取所有数据
* @return
*/
List<M> findAll();
```
### findUnique ###
```java
/**
* 返回唯一数据
* @param typeClass
* @param query
* @return
*/
<T> T findUnique(Class<T> typeClass,Consumer<Query<M>> consumer)；
```
### findUniqueList ###
```java
/**
* 返回唯一的列表数据
* @param typeClass
* @param consumer
* @return
*/
<T> List<T> findUniqueList(Class<T> typeClass,Consumer<Query<M>> consumer)；
```
### findToDouble ###
```java
/**
* 返回单个 Double 对象
* @param consumer
* @return
*/
Integer findToDouble(Consumer<Query<M>> consumer);
```
### findToInteger ###
```java
/**
* 返回单个 Integer 对象
* @param consumer
* @return
*/
Integer findToInteger(Consumer<Query<M>> consumer);
```
### findToLong ###
```java
/**
* 返回单个 Long 对象
* @param consumer
* @return
*/
Long findToLong(Consumer<Query<M>> consumer)；
```
### findToString ###
```java
/**
* 返回单个 String 对象
* @param consumer
* @return
*/
String findToString(Consumer<Query<M>> consumer)；
```
### findStringList ###
```java
/**
* 返回 List<String>
* @param consumer
* @return
*/
List<String> findStringList(Consumer<Query<M>> consumer)；
```
```java
/**
* findCount
* @param query
* @return
*/
Integer findCount(Consumer<Query<M>> consumer)；





/**
* 返回 List<Integer>
* @param consumer
* @return
*/
List<Integer> findIntegerList(Consumer<Query<M>> consumer)；

/**
* 返回 List<Long>

* @param consumer
* @return
*/
List<Long> findLongList(Consumer<Query<M>> consumer)；

/**
* 返回 List<Double>
* @param consumer
* @return
*/
List<Double> findDoubleList(Consumer<Query<M>> consumer)；


/**
* 分页查询
* @param pageNo
* @param pageSize
* @param consumer
* @return
*/
Page<M> findPage(Integer pageNo,Integer pageSize,Consumer<Query<M>> consumer)；

/**
* 分页查询
* @param pageNo
* @param pageSize
* @return
*/
Page<M> findPage(Integer pageNo,Integer pageSize)；

/**
* 分页查询
* @param pageNo
* @param pageSize
* @param entityClass
* @param consumer
* @return
*/
<T> Page<T> findPage(Integer pageNo,Integer pageSize,Class<T> entityClass,Consumer<Query<M>> consumer)；
```
### 其他操作
```java
/**
* 添加或者更新

* @param model
* @return 主键id
*/
Id saveOrUpdate(M model);

/**
* 执行存储过程
* @param call
* @param callableStatementConsumer
* @param resultSetFun
* @return
*/
<T> T call(String call,Consumer<CallableStatement> callableStatementConsumer,Function<ResultSet,T> resultSetFun);

/**
* 执行sql 操作
* @param sql
* @param params
* @return 受影响行数
*/
int execute(String sql,Object...params);

/**
* 执行查询
* @param sql
* @param fun
* @param params
* @return
*/
<T> List<T> executeQuery(String sql,Function<ResultSet, T> fun,Object...params);

```

# Query 对象 条件构造器
```java

/**
* AND 
*/
Query<T> and();

/**
* AND condition ({sql })
* @param sql
* @param tableClass 
* @param consumer
*/
<R> Query<T> and(String condition, Class<R> tableClass, Consumer<Query<R>> consumer);

/**
* AND condition ?
* @param condition
* @param params
*/
Query<T> and(String condition, Object...params);

/**
* AND field = ? 
* @param field
* @param value
*/
Query<T> andEq(String field, Object value);

/**
* AND field > ?
* @param field
* @param value
*/
Query<T> andGt(String field, Object value);

/**
* field > ?
* @param field
* @param value
*/
Query<T> gt(String field, Object value);

/**
* field >= ?
* @param field
* @param value
*/
Query<T> ge(String field,Object value);

/**
* AND field IN (?,?,?) values 
* @param field
* @param values
*/
Query<T> andIn(String field, Object[] values);


/**
* AND (conditions)
* @param consumer
*/
Query<T> and(Consumer<Query<T>> consumer);

/**
* AND LIKE ? 
* @param field
* @param value
*/
Query<T> andLike(String field, Object value);

/**
* LIKE ? 
* @param field
* @param value
*/
Query<T> like(String field,Object value);

/**
* AND field < ?
* @param field
* @param value
*/
Query<T> andLt(String field, Object value);

/**
* field < ?
* @param field
* @param value
*/
Query<T> lt(String field,Object value);

/**
* field <= ?
* @param field
* @param value
*/
Query<T> le(String field,Object value);

/**
* exists ({sqlstr})
* @param sqlstr sql语句
* @param params 参数
*/
Query<T> exists(String sqlstr,Object...params);

/**
* exists
* @param query
*/
<En> Query<T> exists(Query<En> query);

/**
* exists
* @param tableClass
* @param consumer
*/
<En> Query<T> exists(Class<En> tableClass,Consumer<Query<En>> consumer );

/**
* NOT 
*/
Query<T> not();

/**
* AND field NOT IN (?,?,?)
* @param field
* @param values
*/
Query<T> andNotIn(String field, Object...values);

/**
*  field not in (?,?,?) 
* @param field
* @param values
*/
Query<T> notIn(String field,Object...values);

/**
* field = ?
* @param field
* @param value
*/
Query<T> eq(String field, Object value);

/**
* field != ?
* @param field
* @param value
*/
Query<T> ne(String field,Object value);

/**
* and field != ? 
* @param field
* @param value
*/
Query<T> andNe(String field,Object value);


/**
* GROUP BY {group}
*/
Query<T> group(String group);

/**
* id = ?
* @param value
* @return
*/
Query<T> idEq(Object value);


/**
* field IN (?,?)
* @param field
* @param values
* @return
*/
Query<T> in(String field, Object...values)

/**
* field IN ()
* @param field
* @param tableClass
* @param consumer
* @return
*/
<R> Query<T> in(String field,Class<R> tableClass,Consumer<Query<R>> consumer)


<R> Query<T> in(String field,Query<R> query);

 <R> Query<T> andIn(String field,Class<R> tableClass,Consumer<Query<R>> query);

 Query<T> innerjoin(Class<?> table, String condition);

 Query<T> innerjoin(String table, String condition);
 <R>  Query<T> leftjoin(String conditions);

 <R> Query<T> leftjoin(Class<R> tableClass, String condition);


 <R> Query<T> leftjoin(Class<R> tableClass,String asStr,String condition);

 Query<T> leftjoin(String table, String condition);

 Query<T> leftjoin(String table,String asStr,String condition);

 Query<T> on(String condition);


/**
* Limit 默认分页

* @param pageNo
* @param pageSize
* @return
*/
 Query<T> limit(int pageNo, int pageSize);


/**
* order by 

* @param orderBy
* @return
*/
 Query<T> order(String orderBy);

 Query<T> rightjoin(Class<?> table, String condition);

 Query<T> rightjoin(String table, String condition);

  Query<T> join(Class<?>...tableClass);

  Query<T> join(String...tables);

/**
* 查询的字段 

* @param fields
* @return
*/
Query<T> select(List<String> fields);

/**
* 查询的字段 

* @param field
* @return
*/
 Query<T> select(String field);

/**
* 需要查询的字段
* @param fields
*/
Query<T> select(String...fields);

/**
*  查询 
* @param alias 别名
* @param fields 字段名称
*/
Query<T> select(String alias,List<String> fields);

/**
* 不需要查询的列名
* @param fields
*/
Query<T> selectExcludes(String...fields);

/**
*  更新的字段
* @param fields
*/
Query<T> set(Map<String, Object> fields);

/**
* 更新字段
* @param t
*/
Query<T> set(T t);

/**
*  更新的字段
* @param field
* @param value
*/
Query<T> set(String field, Object value);

/**
*  更新字段 ex (set age = (age + ?) )  .. 1
* @param field
* @param sql
* @param params
*/
Query<T> setSQLStr(String field,String sql,Object...params);

/**
* 批量更新或者新增的数据
* @param batch
*/
Query<T> batch(List<Map<String, Object>> batch);

/**
* Table AS alias
* @param alias
*/
Query<T> alias(String alias);

/**
* 构建一个空的 where
*/
Query<T> where();


/**
*  where 
* @param condition
* @param params
* @return
*/
Query<T> where(String condition, Object...params);

<R> Query<T> where(String condition,Class<R> tableClass,Consumer<Query<R>> where);

/**
* where map
* @param whereMap
*/
Query<T> where(Map<String, Object> whereMap);

/**
* id in (?,?,?)
* @param ids 主键id
*/
Query<T> idIn(Object...ids);

<R> Query<T> idIn(Class<R> tableClass,Consumer<Query<R>> query);

Query<T> between(String field,Object start,Object end);

Query<T> andBetween(String field,Object start,Object end);
Query<T> isNull(String field);

Query<T> isNotNull(String field);
Query<T> andIsNotNull(String field);

Query<T> andIsNull(String field);

Query<T> andThen(boolean eq,Consumer<Query<T>> query);

Query<T> andNotEmptyThen(Object value,Consumer<Query<T>> query);

Query<T> cache();

Query<T> cache(String key,long second);

Query<T> cache(String key);

Query<T> cache(long second);

Query<T> having(String having);

Query<T> count(String count);

Query<T> or(String orStr);

Query<T> or(Consumer<Query<T>> query);
```

