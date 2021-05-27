package com.spring.proxy;

import java.lang.reflect.Method;

import com.spring.util.StringUtils;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;


/**
 * 代理类：
 * 在创建代理对象的同时拦截方法的执行实现before和after逻辑
 */
public abstract class AbsMethodAdvance implements MethodInterceptor {

    //被代理的对象，即目标对象
    private Object targetObject;
    //被代理的方法名
    private String proxyMethodName;

    public String getProxyMethodName() {
        return proxyMethodName;
    }

    public void setProxyMethodName(String proxyMethodName) {
        this.proxyMethodName = proxyMethodName;
    }

    //生成代理对象
    public Object createProxyObject(Object target) {
        this.targetObject = target;
        /**
         * 用cglib生成代理对象
         * Enhancer动态的创建给定类的子类并且拦截代理类的所有的方法，
         * 和JDK动态代理不一样的是不管是接口还是类它都能正常工作。
         **/
        Enhancer enhancer = new Enhancer();
        //设置代理目标类
        enhancer.setSuperclass(this.targetObject.getClass());
        //设置单一回调对象，在调用中拦截对目标方法的调用
        enhancer.setCallback(this);
        //设置类加载器
//		enhancer.setClassLoader(classLoader);
        return enhancer.create();
    }

    /**
     * 方法描述 当对基于代理的方法回调时，
     * 在调用原方法之前会调用该方法
     * 拦截对目标方法的调用
     *
     * @param proxy       代理对象
     * @param method      拦截的方法
     * @param args        拦截的方法的参数
     * @param methodProxy 代理
     */
    @Override
    public Object intercept(Object proxy, Method method, Object[] args,
                            MethodProxy methodProxy) throws Throwable {
        Object result;
        String proxyMethod = getProxyMethodName();
        if (StringUtils.isNotBlank(proxyMethod) && proxyMethod.equals(method.getName())) {
            //前置通知
            doBefore();
        }
        //执行拦截的方法
        result = methodProxy.invokeSuper(proxy, args);
        if (StringUtils.isNotBlank(proxyMethod) && proxyMethod.equals(method.getName())) {
            //后置通知
            doAfter();
        }
        return result;
    }

    public abstract void doBefore();

    public abstract void doAfter();
}
