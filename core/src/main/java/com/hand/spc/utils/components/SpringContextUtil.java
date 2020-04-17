package com.hand.spc.utils.components;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
* @author tainmin.wang
* @version date：2019年8月19日 下午2:37:39
* 
*/
@Component("SpringContextUtil")
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        SpringContextUtil.context = context;
    }

    public static Object getSpringBean(String beanName) {
        if (null == beanName || 0 == beanName.length()) {
            throw new IllegalArgumentException("beanName is required!");
        }
        return context == null ? null : context.getBean(beanName);
    }

}
