package jasmine.orm.db.springjdbc;

import org.springframework.beans.factory.annotation.Autowired;

import jasmine.orm.code.DbContext;
import jasmine.orm.code.DbRepositoryTemplate;

/**
 * @author hanjiang.Yue
 *
 * @param <M>
 * @param <Id>
 */
public abstract class SpringJdbcTemplate<M,Id> extends DbRepositoryTemplate<M, Id>{
	
	@Autowired
	 public DbContext dbContext;
	
	@Override
	public DbContext dbContext() {
		return dbContext;
	}
}
