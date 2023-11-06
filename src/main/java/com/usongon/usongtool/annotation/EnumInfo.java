package com.usongon.usongtool.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author usong
 * @date 2023-11-02 22:30
 */
@Target(ElementType.TYPE) // 表明这个注解只能用在类或者接口上
@Retention(RetentionPolicy.RUNTIME) // 表明这个注解在运行时也是有效的
public @interface EnumInfo {
    String enumName();
    String showCode() default "code";
    String showName() default "desc";

}
