package com.hand.custom.annotation;

import java.lang.annotation.*;
/**
* @author tainmin.wang
* @version date：2019年9月16日 下午2:10:47
* 
*/
@Inherited
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface LogCustom {

}
