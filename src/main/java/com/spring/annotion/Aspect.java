package com.spring.annotion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义 Aspect注解
 */
@Retention(RetentionPolicy.RUNTIME) //生命周期：始终不会丢弃，运行期也保留该注解
@Target(ElementType.TYPE) //使用范围：用于描述类、接口(包括注解类型) 或enum声明
@Documented //简单的Annotations 标记注解，表示是否将注解信息添加在java 文档中
//@Inherited //定义该注释和子类的关系：阐述了某个被标注的类型是被继承的
public @interface Aspect {

}
