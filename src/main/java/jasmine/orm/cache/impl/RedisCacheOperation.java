package jasmine.orm.cache.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.alibaba.fastjson.JSON;

import jasmine.orm.cache.CacheOperation;
import jasmine.orm.util.StrUtils;

/**
 *  redis 缓存操作
 * @author hanjiang.Yue
 *
 */
@SuppressWarnings("unchecked")
public class RedisCacheOperation implements CacheOperation{

	private RedisTemplate<String,Object> redisTemplate;

	@Override
	public <T> T get(Class<T> typeClass, String cacheKey) {
		
		return (T) redisTemplate.opsForValue().get(cacheKey);
	}

	@Override
	public void put(String cacheKey, Object value) {
		redisTemplate.opsForValue().set(cacheKey, JSON.toJSONString(value));
	}

	public String serialize(Object value) {
		return JSON.toJSONString(value);
	}

	public <T> T deserialize(Class<T> classType, String value) {
		 	return JSON.parseObject(value, classType);
	}

	@Override
	public void put(String cacheKey, long expiryTime, Object value) {
		redisTemplate.opsForValue().set(cacheKey, value, expiryTime,TimeUnit.SECONDS);
		
	}

	@Override
	public void delete(String cacheKey) {
		redisTemplate.delete(cacheKey);
	}

	@Override
	public void delete(Collection<String> cacheKeys) {
		redisTemplate.delete(cacheKeys);
	}


	@Override
	public <T> List<T> list(Class<T> type, Collection<String> keys) {
		return (List<T>) redisTemplate.
				opsForValue().
				multiGet(keys).
				stream().
				filter(data->!StrUtils.isEmpty(data)).
				collect(Collectors.toList());
	}

	@Override
	public <T> List<T> list(Class<T> type, String cacheKey) {
		return (List<T>) redisTemplate.opsForValue().get(cacheKey);
	}


	@Override
	public void put(Map<String, Object> elements, long expiryTime) {
		RedisSerializer<String> keySerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
		RedisSerializer<Object> valueSerializer = (RedisSerializer<Object>) redisTemplate.getValueSerializer();
		redisTemplate.executePipelined(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				elements.forEach((key,value)->{
					connection.setEx(keySerializer.serialize(key), expiryTime,valueSerializer.serialize(value));
				});
				return null;
			}
		});
		
	}


	public RedisCacheOperation(RedisTemplate<String,Object> redisTemplate) {
		super();
		this.redisTemplate = redisTemplate;
	}

	
	
}
