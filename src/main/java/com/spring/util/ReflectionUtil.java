package com.spring.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 通过反射机制创建实例调用方法和设置成员变量的值
 */
public class ReflectionUtil {

	//创建实例
	public static Object newInstance(Class<?> cls){
		Object instance;
		try {
			instance = cls.newInstance();
		} catch (Exception e) {
			throw new RuntimeException();
		} 
		return instance;
	}
	//创建实例（根据类名）
	public static Object newInstance(String className){
		Class<?> cls = ClassUtil.loadClass(className);
		return newInstance(cls);
	}
	/**
	 * 调用方法
	 * @param obj 实例对象
	 * @param method 实例方法
	 * @param args 方法参数
	 * @return
	 */
	public static Object invokeMethod(Object obj,Method method,Object... args){
		Object result;
		try {
			//setAccessible(true)其作用就是反射时 可以 访问私有方法
			method.setAccessible(true);
			result = method.invoke(obj, args);
		} catch (Exception e) {
			throw new RuntimeException();
		} 
		return result;
	}
	/**
	 * 设置成员变量的值
	 * @param obj  实例对象
	 * @param field  成员变量
	 * @param value
	 */
	public static void setField(Object obj,Field field,Object value){
		try {
			field.setAccessible(true); //可以 访问私有变量
			field.set(obj, value);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}
}
