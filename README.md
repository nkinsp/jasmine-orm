# jasmine-orm

# 特性

* **强大的 CRUD** 内置操作操作模板 可实现大部分的curd 操作
* **支持多种数据库** MySQL、MariaDB、Oracle、DB2,SQLSERVER2012 ,SQLite
* **支持主键自动生成** 支持多达 4 种主键策略（内含分布式唯一 ID 生成器 - Sequence），可自由配置，完美解决主键问题
* **支持 ActiveRecord 模式** 支持 ActiveRecord 形式调用，实体类只需继承 Model 类即可进行强大的 CRUD 操作
* **无xml** 没有xml文件 无需扫描
* **快速启动**
* **内置分页**
* **内置缓存操作** HashMap ,J2ECache,Redis 等 可以自定义缓存

# 快速开始

#### 在spring boot 中使用

```
@Bean
	public DbContext dbContext(DataSource dataSource) {
		return new DbContext(dataSource);
	}
```

#### 新建User 对象

```
@Table
public class User {
	private Integer id;
	private Integer age;
	private String name;
}
```

#### 通用的模板模式\`\`

```
public class UserDao extends SpringJdbcTemplate<User, Integer>{

}
```

#### 基础演示

```
//查询 select id,name,age from user where id = ? 
User user = userDao.find(1);
//条件查询 Lambda  模式
user = userDao.find(query->query.idEq(1));
user = userDao.find(query->query.where("id = ? ", 1));
//条件查询 
userDao.where().idEq(1).find();
//查询 select id,name,age from user where id in (?,?,?)
List<User> users = userDao.where().idIn(1,2,3).findList();
//更新
userDao.update(user);
//删除
userDao.delete(1);
userDao.where().gt("id",100).delete();
```

#### ActiveRecord 模式

```
@Table
public class User  extends ActiveRecord<User, Integer>{
	private Integer id;
	private Integer age;
	private String name;

}
```

#### 示例

```
//查询
User user = new User().find(1);
List<User> findAll = new User().findAll();
List<User> findList = new User().findList(1,2,3);
//更新
User update = new User();
update.setId(100);
update.setName("Jack");
update.update();
//删除 返回受影响行数
new User().where().idEq(100).delete();
new User().delete(100);
new User().delete(1,2,3);
```

#### 普通模式

```
@Autowired
private DbContext dbContext;
	
public void test() {
		//查询
		dbContext.table(User.class).find(1);
		dbContext.table(User.class).select("id,name").findList();
		dbContext.table(User.class).where().idEq(1).find();
		//更新
		dbContext.table(User.class).update(user->{
			user.set("age", 18).where().idEq(1);
		});
		//删除
		dbContext.table(User.class).delete(1);
		dbContext.table(User.class).delete(user->{
			user.where().idIn(1,2,3);
		});
}
```

