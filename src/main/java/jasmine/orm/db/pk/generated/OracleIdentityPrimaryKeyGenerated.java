package jasmine.orm.db.pk.generated;


import jasmine.orm.enums.FieldType;
import jasmine.orm.enums.IdType;
import jasmine.orm.query.Field;
import jasmine.orm.table.TableMapping;
import jasmine.orm.util.StrUtils;

/**
 * Oracle 自增 id
 * @author hanjiang.Yue
 *
 */
public class OracleIdentityPrimaryKeyGenerated extends DefaultPrimaryKeyGenerated{

	
	
	@Override
	public Field generated(TableMapping<?> mapping) {
		if(mapping.getIdType() == IdType.AUTO) {
			String keySequence = mapping.getKeySequence();
			if(StrUtils.isEmpty(keySequence)) {
				throw new RuntimeException("Not Found  @KeySequence");
			}
			return new Field(mapping.getPrimaryKey(), keySequence, FieldType.SQL_STRING);
		}
		return super.generated(mapping);
	}
}
