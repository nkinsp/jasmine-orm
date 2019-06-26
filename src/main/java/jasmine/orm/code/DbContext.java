package jasmine.orm.code;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jasmine.orm.activerecord.ActiveRecord;
import jasmine.orm.cache.CacheKeyGenerated;
import jasmine.orm.cache.CacheOperation;
import jasmine.orm.cache.DefaultCacheKeyGenerated;
import jasmine.orm.cache.impl.SimpleHashMapCacheOperation;
import jasmine.orm.db.DbOperation;
import jasmine.orm.db.springjdbc.SpringJdbcDbOperation;
import jasmine.orm.query.Query;
import jasmine.orm.query.impl.DB2DialectQueryImpl;
import jasmine.orm.query.impl.H2DialectQueryImpl;
import jasmine.orm.query.impl.MySqlDialectQueryImpl;
import jasmine.orm.query.impl.OracleDialectQueryImpl;
import jasmine.orm.query.impl.PostgreDialectQueryImpl;
import jasmine.orm.query.impl.SQLServerDialectQueryImpl;
import jasmine.orm.query.impl.SQLiteDialectQueryImpl;
import jasmine.orm.table.TableMapping;

public class DbContext{
	
	
	private static final Logger log = LoggerFactory.getLogger(DbContext.class);
	
	private static final Map<String, TableMapping<?>> TABLEMAPPING = new ConcurrentHashMap<>();

	private DbConfig config;
	
	/**
	 * 获取表映射
	 * @author hanjiang.Yue
	 * @param tableClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static synchronized <T> TableMapping<T>  findTableMapping(Class<T> tableClass) {
		String name = tableClass.getName();
		TableMapping<?> mapping = TABLEMAPPING.get(name);
		if(mapping == null) {
			mapping = new TableMapping<>(tableClass);
			TABLEMAPPING.put(name, mapping);
			if(log.isDebugEnabled()) {
				log.debug("==> init table mapping class:[{}] fields:[{}] ",tableClass,mapping.getColumns());
			}
		}
		return (TableMapping<T>) mapping;
	}
	
	/**
	 * 创建
	 * @author hanjiang.Yue
	 * @param tableClass
	 * @return
	 */
	public <M> Query<M> createQuery(Class<M> tableClass) {
		return createQuery(tableClass, config);
	}
	
	/**
	 * 创建query 对象
	 * 
	 * @author hanjiang.Yue
	 * @param dbType
	 * @param tableClass
	 * @param config
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <M> Query<M> createQuery(Class<M> tableClass, DbConfig config) {
		try {
			return (Query<M>) config.getDialectClass().getConstructor(TableMapping.class, DbConfig.class)
					.newInstance(findTableMapping(tableClass), config);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static <M> Query<M> createQuery(Class<M> tableClass,DbContext context) {
		return createQuery(tableClass, context.getConfig());
	}
	
	
	public static <T,Id> DbRepository<T, Id> create(Class<T> tableClass) {
		
		return ActiveRecord.dbContext.table(tableClass);
		
	}
	



	public DbOperation getDbOperation() {
		return this.config.getDbOperation();
	}

	public void setDbOperation(DbOperation dbOperation) {
		this.config.setDbOperation(dbOperation);
	}

	public CacheOperation getCacheOperation() {
		return this.config.getCacheOperation();
	}

	public void setCacheOperation(CacheOperation cacheOperation) {
		this.config.setCacheOperation(cacheOperation);
	}

	public CacheKeyGenerated getCacheKeyGenerated() {
		return this.config.getCacheKeyGenerated();
	}

	public void setCacheKeyGenerated(CacheKeyGenerated cacheKeyGenerated) {
		this.config.setCacheKeyGenerated(cacheKeyGenerated);
	}
	

	public String getOracleSequencesName() {
		return this.config.getOracleSequencesName();
	}

	public void setOracleSequencesName(String oracleSequencesName) {
		this.config.setOracleSequencesName(oracleSequencesName);
	}


	public DbContext(DbOperation dbOperation) {
		this(dbOperation, new SimpleHashMapCacheOperation());
	}

	public DbContext(DbOperation dbOperation, CacheOperation cacheOperation) {
		this(dbOperation,cacheOperation,new DefaultCacheKeyGenerated());
	}

	public DbContext(DbOperation dbOperation, CacheOperation cacheOperation, CacheKeyGenerated cacheKeyGenerated) {
		this.config = new DbConfig(dbOperation,cacheOperation,cacheKeyGenerated);
		this.config.setDialectClass(loadDbDialect());
		ActiveRecord.init(this);
	}
	

	public  <T,Id> DbRepository<T, Id> table(Class<T> tableClass){
		return new DbRepository<T, Id>() {

			@SuppressWarnings("unchecked")
			private Class<Id> idClass = (Class<Id>) findTableMapping(tableClass).getIdClassType();
			
			@Override
			public Class<T> modelClass() {
				return tableClass;
			}

			@Override
			public Class<Id> idClass() {
				return idClass;
			}
			
			@Override
			public DbContext dbContext() {
				return DbContext.this;
			}
		};
	}
	
	
	public DbContext(DataSource dataSource) {
		this(new SpringJdbcDbOperation(dataSource));
	}

	/**
	 * 加载数据库类型 自动匹配
	 * @author hanjiang.Yue
	 */
	public Class<?> loadDbDialect() {
		Connection connection = null;
		try {
			connection = this.config.getDbOperation().getDataSource().getConnection();
			DatabaseMetaData metaData =connection.getMetaData();
			String url = metaData.getURL().toLowerCase();
			Map<String, Class<?>> dbTypeMap = new HashMap<>();
			dbTypeMap.put("jdbc:mysql:",MySqlDialectQueryImpl.class );
			dbTypeMap.put("jdbc:sqlite:", SQLiteDialectQueryImpl.class);
			dbTypeMap.put("jdbc:oracle:",OracleDialectQueryImpl.class);
			dbTypeMap.put("jdbc:postgresql:",PostgreDialectQueryImpl.class);
			dbTypeMap.put("jdbc:db2:",DB2DialectQueryImpl.class);
			dbTypeMap.put("jdbc:sqlserver:",SQLServerDialectQueryImpl.class);
			dbTypeMap.put("jdbc:h2:", H2DialectQueryImpl.class);
			for (Entry<String, Class<?>> en : dbTypeMap.entrySet()) {
				if(url.startsWith(en.getKey())) {
					return en.getValue();
				}
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	public DbConfig getConfig() {
		return config;
	}

	public void setConfig(DbConfig config) {
		this.config = config;
	}
	
	public void setDialectClass(Class<?> dialectClass) {
		this.config.setDialectClass(dialectClass);
	}
	
	
	
}
