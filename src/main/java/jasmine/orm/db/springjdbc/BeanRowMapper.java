package jasmine.orm.db.springjdbc;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.ClassUtils;

import jasmine.orm.annotation.Table;
import jasmine.orm.code.DbContext;
import jasmine.orm.table.Property;
import jasmine.orm.table.TableMapping;

public class BeanRowMapper<T> extends BeanPropertyRowMapper<T>{

	private static Log log = LogFactory.getLog(BeanRowMapper.class);
	
	private TableMapping<T> tableMapping;
	
	private Class<T> entityClass;


	public BeanRowMapper(Class<T> entityClass) {
		this.entityClass = entityClass;
		Table table = entityClass.getAnnotation(Table.class);
		if(table == null) {
			super.setMappedClass(entityClass);
		}else {
			this.tableMapping = DbContext.findTableMapping(entityClass);
		}
		
	
	}



	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {

		if(tableMapping == null) {
			return super.mapRow(rs, rowNum);
		}
		return getResultObject(rs, entityClass);
	}



	/**
	 * 构建对象
	 * @param rs
	 * @param mappedClass
	 * @return
	 * @throws SQLException
	 */
	private T getResultObject(ResultSet rs, Class<T> mappedClass) throws SQLException {
		T mappedObject = BeanUtils.instantiateClass(mappedClass);
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);
		ResultSetMetaData metaData = rs.getMetaData();
		int count = metaData.getColumnCount();
		for (int index = 1; index <= count; index++) {
			String columnName = JdbcUtils.lookupColumnName(metaData, index);
			Property propertyMapping = tableMapping.getPropertyByColumn(columnName);
			if(propertyMapping == null) {
				continue;
			}
			PropertyDescriptor pd = propertyMapping.getProperty();
			try {
				Object value = JdbcUtils.getResultSetValue(rs, index, pd.getPropertyType());
				bw.setPropertyValue(pd.getName(), value);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				log.error("Mapping column '" + columnName + "' to property '" + pd.getName() +
						"' of type '" + ClassUtils.getQualifiedName(pd.getPropertyType()) + " fail'");
			}
		}
		return mappedObject;
	}

}
