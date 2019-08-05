package jasmine.orm.cache;

import jasmine.orm.table.TableMapping;

/**
 * 缓存key的生成
 * @author hanjiang.Yue
 *
 */
public interface CacheKeyGenerated {

	String generated(TableMapping<?> mapping,Object value);
}
