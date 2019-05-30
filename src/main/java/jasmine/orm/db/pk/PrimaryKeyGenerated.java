package jasmine.orm.db.pk;

import jasmine.orm.query.Field;
import jasmine.orm.table.TableMapping;

public interface PrimaryKeyGenerated {

	/**
	 * 生成主键id
	 * @author hanjiang.Yue
	 * @param idType
	 * @return
	 */
	Field generated(TableMapping<?> mapping);
}
