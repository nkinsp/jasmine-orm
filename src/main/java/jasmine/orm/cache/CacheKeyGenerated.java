package jasmine.orm.cache;

import jasmine.orm.table.TableMapping;

public interface CacheKeyGenerated {

	String generated(TableMapping<?> mapping,Object value);
}
