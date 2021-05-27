package com.spring.context;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.spring.annotion.Aspect;
import com.spring.annotion.PointCut;
import com.spring.proxy.AbsMethodAdvance;
import com.spring.util.ClassUtil;
import com.spring.util.ReflectionUtil;

/**
 * 找到切点切面设置代理初始化容器类
 * 手写 spring aop 的思路：
 * 1 扫描 aop 包， 获取 aspect 的类
 * 2 根据 切点 获取该切点的 类 和 方法
 * 3 根据配置的 类 和 方法 为该类生成一个代理对象
 * 4 将改代理对象放入 bean Map 中
 * 5 调用的时候 将代理对象 转换成需要的对象
 *
 */
public class ApplicationContext {

	//存放代理类的集合
	public static ConcurrentHashMap<String,Object> proxyBeanMap = new ConcurrentHashMap<String,Object>();
	static{
		initAopBeanMap("com.spring.domain");
	}
	public static void initAopBeanMap(String basePath){
		try {
			//1、 扫描 aop 包， 获取 aspect 的类
			//获得加载类 的集合
			Set<Class<?>> classSet = ClassUtil.getClassSet(basePath);
			for(Class<?> clazz:classSet){
				//判断@Aspect的注解是否在clazz类上
				if(clazz.isAnnotationPresent(Aspect.class)){
					Method[] methods = clazz.getMethods();
					for(Method method:methods){
						//判断@pointCut的注解是否在method方法上
						if(method.isAnnotationPresent(PointCut.class)){
							//2、 根据 切点 获取该切点的 类 和 方法
							//找到切点？？？为什么是0
							PointCut pointCut = (PointCut) method.getAnnotations()[0];
							String pointCutStr = pointCut.value();
							System.out.println("pointCutStr:"+pointCutStr);
							String[] pointCutArr = pointCutStr.split("_");
							//被代理的类名
							String className = pointCutArr[0];
							//被代理的方法名
	                        String methodName = pointCutArr[1];
	                        //3、根据配置的 类 和 方法 为该类生成一个代理对象
	                        //根据切点 创建被代理对象
	                        Object targetObj = ReflectionUtil.newInstance(className);
	                        //根据切面类创建代理者
	                        AbsMethodAdvance proxyer = (AbsMethodAdvance)ReflectionUtil.newInstance(clazz);
	                        //设置要代理的方法
                            proxyer.setProxyMethodName(methodName);

                            // 生动动态代理对象
                            Object object = proxyer.createProxyObject(targetObj);
                            System.out.println("targetObject:"+targetObj.getClass().getSimpleName());
                            if(object!=null){
                            	//4、将改代理对象放入 bean Map 中
                            	proxyBeanMap.put(targetObj.getClass().getSimpleName().toLowerCase(),object);
                            }
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
