package com.spring.util;

/**
 * 字符串处理类
 */
public class StringUtils {

	public static boolean isNotBlank(String str){
		if(!"".equals(str)&&null!=str){
			return true;
		}else{
			return false;
		}
	}
	public static boolean isBlank(String str){
		return !isNotBlank(str);
	}
}
