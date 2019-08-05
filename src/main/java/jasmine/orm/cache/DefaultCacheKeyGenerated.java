package jasmine.orm.cache;

import jasmine.orm.table.TableMapping;
import jasmine.orm.util.MD5Utils;

/**
 * 缓存默认key的生成
 * @author hanjiang.Yue
 *
 */
public class DefaultCacheKeyGenerated implements CacheKeyGenerated{

	@Override
	public String generated(TableMapping<?> mapping, Object value) {
		return  new StringBuilder(mapping.getTableName())
				.append(":cache:")
				.append(MD5Utils.md5(String.valueOf(value)))
				.toString();
	}

}
