package example.dao;

import example.model.User;
import jasmine.orm.db.springjdbc.SpringJdbcTemplate;

public class UserDao extends SpringJdbcTemplate<User, Integer>{

}
