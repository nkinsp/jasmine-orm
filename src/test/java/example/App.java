package example;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import example.dao.UserDao;
import example.model.NUser;
import example.model.User;
import jasmine.orm.code.DbContext;
import jasmine.orm.query.Query;

public class App {


	@Autowired
	private DbContext dbContext;
	
	@Autowired
	private UserDao userDao;
	
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
		
		
		
		
//		//查询 select id,name,age user where id = ? 
//		User user = userDao.find(1);
//		//条件查询 Lambda  模式
//		user = userDao.find(query->query.idEq(1));
//		user = userDao.find(query->query.where("id = ? ", 1));
//		//条件查询 
//		userDao.where().idEq(1).find();
//		//查询 select id,name,age user where id in (?,?,?)
//		List<User> users = userDao.where().idIn(1,2,3).findList();
//		//更新
//		userDao.update(user);
//		//删除
//		userDao.delete(1);
//		userDao.where().gt("id",100).delete();
		
		
		
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
		
		new User().where().andNotEmptyThen(update, query->query.idEq(100));
		
		
		


		
	}
	
	public static void main(String[] args) {
	}
}
