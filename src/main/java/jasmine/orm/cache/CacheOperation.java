package jasmine.orm.cache;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 缓存操作
 * @author hanjiang.Yue
 *
 */
public interface CacheOperation {

	/**
	 * 获取单个对象
	 * @param typeClass
	 * @param cacheKey
	 * @return
	 */
	<T> T get(Class<T> typeClass, String cacheKey);
	
	/**
	 * 获取对象集合
	 * @param typeClass
	 * @param cacheKey
	 * @return
	 */
	<T> List<T> list(Class<T>  typeClass,String cacheKey);
	
	/**
	 * 批量获取对象集合
	 * @param typeClass
	 * @param keys
	 * @return
	 */
	<T> List<T> list(Class<T>  typeClass,Collection<String> keys);

	/**
	 * 添加缓存
	 * @param cacheKey
	 * @param value
	 */
	void put(String cacheKey, Object value);
	
	/**
	 * 添加缓存
	 * @param cacheKey
	 * @param expiryTime 过期时间 单位秒
	 * @param value
	 */
	void put(String cacheKey,long expiryTime,Object value);
	
	/**
	 * 添加多个
	 * @param elements
	 * @param expiryTime
	 */
	void put(Map<String,Object> elements,long expiryTime);
	
	/**
	 * 删除缓存
	 * @param cacheKey
	 */
	void delete(String cacheKey);
	
	/**
	 * 批量删除
	 * @param cacheKeys
	 */
	void delete(Collection<String> cacheKeys);
	

}
