package example.model;



import org.springframework.cache.annotation.Cacheable;

import jasmine.orm.activerecord.ConfigColumn;
import jasmine.orm.activerecord.MapActiveRecord;
import jasmine.orm.annotation.Table;

@Table
public class User extends MapActiveRecord<User, Integer>{

	
	@Override
	public void confingColumns(ConfigColumn config) {
		
		//config.a
	}
	
	
	public static final User DAO = new User();
	
	
	public void  UserInfo (String id) {
	
		new User().delete(del->{
			//del.where("", params)
			del.cache();
		});
	}
	
	
	public static void main(String[] args) {
		
		
	
//		new User().find(1000).u
		
	}
	
	
}
