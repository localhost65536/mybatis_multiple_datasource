package cn.king.annotation;


import cn.king.enumeration.DataSourceType;

import java.lang.annotation.*;

/**
 * @author: wjl@king.cn
 * @time: 2020/5/13 17:03
 * @version: 1.0.0
 * @description: 。。
 * <p>
 * Retention注解的value值：
 * 1、SOURCE：该注解只在".java"中存在，编译成".class"后注解消失。
 * 2、CLASS：该注解可以存活到".class"文件中，但是运行时不会在JVM中保存（默认）。
 * 3、RUNTIME：该注解可以存活到JVM中。可以使用反射读取。
 */
@Target(ElementType.METHOD)           // 指定该注解只能注释在方法上。
@Retention(RetentionPolicy.RUNTIME)   // 指定注解的生命周期。
@Documented                           // 表示该注解注解应该被javadoc工具记录，默认情况下，javadoc是不包括注解的。
public @interface DataSource {
    DataSourceType value() default DataSourceType.MASTER;
}
