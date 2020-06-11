package com.tomcat.dubug.tomcat_dubug.autoconfig;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.stereotype.Component;

/**
 * @author linsong.chen
 * @date 2020-06-11 22:41
 *
 * 测试ConditionalOnClass和ConditionalOnMissingClass类，只有当前项目中有class时，或者不存在class时，才会注入相应的实例
 */
//@ConditionalOnClass(name = "com.fasterxml.classmate.AnnotationConfiguration")
@ConditionalOnMissingClass(value = "com.fasterxml.classmate.AnnotationConfiguration1111")
@Component
public class ClassPathOnConditionConfig {

    public String grtString(){
        return "hello";
    }

}
