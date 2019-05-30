package jasmine.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jasmine.orm.enums.IdType;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
	
	/**
	 * 表名
	 * @author hanjiang.Yue
	 */
	String name() default "";
	 
	/**
	 * 主键名称
	 * @author hanjiang.Yue
	 */
	String id() default "id";
	
	/**
	 * 主键类型
	 * @author hanjiang.Yue
	 */
	IdType idType() default IdType.AUTO;
	
	/**
	 * 是否缓存
	 * @return
	 */
	boolean cache() default false;
	
	/**
	 * 缓存时间 单位秒
	 * @return
	 */
	long cacheTime() default -1;
	
	
}
