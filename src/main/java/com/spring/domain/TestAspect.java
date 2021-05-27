package com.spring.domain;

import com.spring.annotion.Aspect;
import com.spring.annotion.PointCut;
import com.spring.proxy.AbsMethodAdvance;

/**
 * 定义切点和切面， 并且继承 AbsMethodAdvance 代理类
 */
@Aspect
public class TestAspect extends AbsMethodAdvance {

	/** 
     * 全类名_方法名 （被拦截的类_被拦截的方法）
     */
    @PointCut("com.spring.util.Test_doSomeThing")
    public void testAspect() {
    }

    @Override
    public void doBefore() {
        System.out.println("do before");
    }

    @Override
    public void doAfter() {
        System.out.println("do after");
    }


}
