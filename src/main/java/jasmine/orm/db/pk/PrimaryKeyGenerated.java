package jasmine.orm.db.pk;

import jasmine.orm.query.Field;
import jasmine.orm.table.TableMapping;

/**
 * 主键生成
 * @author hanjiang.Yue
 *
 */
public interface PrimaryKeyGenerated {

	/**
	 * 生成主键id
	 * @param idType
	 * @return
	 */
	Field generated(TableMapping<?> mapping);
}
