package jasmine.orm.db.pk.generated;

import jasmine.orm.db.pk.PrimaryKeyGenerated;
import jasmine.orm.enums.IdType;
import jasmine.orm.query.Field;
import jasmine.orm.table.TableMapping;
import jasmine.orm.util.IdWorkUtils;

/**
 * 生成主键类型
 * @author hanjiang.Yue
 *
 */
public class DefaultPrimaryKeyGenerated implements PrimaryKeyGenerated{
	
	@Override
	public Field generated(TableMapping<?> mapping) {
		IdType idType = mapping.getIdType();
		if(idType == IdType.UUID) {
			return new Field(mapping.getPrimaryKey(),IdWorkUtils.uuid());
		}
		if(idType == IdType.SEQUENCE) {
			return new Field(mapping.getPrimaryKey(), IdWorkUtils.sequenceId());
		}		
		return null;
	}

	public DefaultPrimaryKeyGenerated() {
		super();
	}
	
	

}
