package example;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import example.dao.UserDao;
import example.model.NUser;
import example.model.User;
import jasmine.orm.code.DbContext;

public class App {


	@Autowired
	private DbContext dbContext;
	
	@Autowired
	private UserDao userDao;
	
	public void test() {
		
////		dbContext.table(User.class).find(100);
////		List<App> findList = userDao.where().findList(App.class);
//		
////		userDao.where().cache().
		
//		userDao.where().idEq(1).findPage(1, pageSize)
		
//			new User().set("id", 100).set("name", 100).;
		
//		NUser.dao.f

		
	}
}
