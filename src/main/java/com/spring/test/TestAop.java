package com.spring.test;

import java.util.concurrent.ConcurrentHashMap;

import com.spring.context.ApplicationContext;
import com.spring.util.Test;

public class TestAop {
	public static void main(String[] args) {
		//模拟容器初始化
//		ApplicationContext context = new ApplicationContext();
		ConcurrentHashMap<String,Object> proxyBeanMap = ApplicationContext.proxyBeanMap;
        //5、调用的时候 将代理对象 转换成需要的对象
		//生成代理对象，默认为该类名的小写
		Test test =(Test)proxyBeanMap.get("test");
        test.doSomeThing();
        System.out.println("------------");
        test.doWithNotProxy();
        System.out.println("TEst".toLowerCase());
        System.exit(0);
	}

}
