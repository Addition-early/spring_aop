package com.spring.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 自定义PointCut注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD) //使用范围：方法
public @interface PointCut {

	/**
	 * 全类名_方法名
	 * @return
	 */
	String value();
}
