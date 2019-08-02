package jasmine.orm.cache.impl;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jasmine.orm.cache.CacheOperation;

/**
 * 简单的HashMap 缓存
 * @author hanjiang.Yue
 *
 */
public class SimpleHashMapCacheOperation implements CacheOperation{

	
	private final Map<String, Cache>  cache;
	
	
	public SimpleHashMapCacheOperation(int cacheSize) {
		cache = new LinkedHashMap<String,Cache>() {

			private static final long serialVersionUID = -7807878587778419878L;

			@Override
			protected boolean removeEldestEntry(java.util.Map.Entry<String, Cache> eldest) {
				// TODO Auto-generated method stub
				return cache.size() >= cacheSize;
			}
		};
	}
	
	public SimpleHashMapCacheOperation() {
		this(100000);
	}

	
	
	class Cache {
		
		private Object value;
		
		private long timeOut = -1;

		public Object getValue() {
			return value;
		}

		public long getTimeOut() {
			return timeOut;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		public void setTimeOut(long timeOut) {
			this.timeOut = timeOut;
		}

		public Cache(Object value, long timeOut) {
			super();
			this.value = value;
			if(timeOut > 0) {
				this.timeOut = System.currentTimeMillis()+(timeOut*1000);
			}
			
		}

		public Cache(Object value) {
			super();
			this.value = value;
		}
		
		
		
	}



	@SuppressWarnings("unchecked")
	@Override
	public <T>  T get(Class<T> typeClass, String cacheKey) {
		Cache value = cache.get(cacheKey);
		if(value != null) {
			synchronized (value) {
				long currentTimeMillis = System.currentTimeMillis();
				if(value.getTimeOut() < currentTimeMillis) {
					this.delete(cacheKey);
					return null;
				}
			}
			return (T) value.getValue();
		}
		return null;
	}



	@Override
	public synchronized void put(String cacheKey, Object value) {
		cache.put(cacheKey, new Cache(value));
	}






	@Override
	public synchronized void delete(String cacheKey) {
		cache.remove(cacheKey);
	}



	@Override
	public synchronized void delete(Collection<String> cacheKeys) {
		cacheKeys.forEach(key->delete(key));
	}



	@Override
	public synchronized void put(String cacheKey, long expiryTime, Object value) {
		cache.put(cacheKey, new Cache(value, expiryTime));
	}


	@Override
	public <T> List<T> list(Class<T> type, Collection<String> keys) {
		return keys.stream().map(key->get(type, key)).filter(data->data != null).collect(Collectors.toList());
	}



	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> list(Class<T> type, String cacheKey) {
		Cache cacheData = cache.get(cacheKey);
		if(cacheData != null) {
			synchronized (cacheData) {
				long currentTimeMillis = System.currentTimeMillis();
				if(cacheData.getTimeOut() > currentTimeMillis) {
					this.delete(cacheKey);
					return null;
				}
				
			}
			return (List<T>) cacheData.getValue();
		}
		return null;
	}



	@Override
	public synchronized void put(Map<String, Object> elements, long expiryTime) {
		elements.forEach((k,v)->{
			put(k, expiryTime, v);
		});
	}


}
