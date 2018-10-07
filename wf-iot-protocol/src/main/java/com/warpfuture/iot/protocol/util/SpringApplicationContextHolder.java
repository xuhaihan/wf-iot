package com.warpfuture.iot.protocol.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang.Validate.notEmpty;

/** Created by 徐海瀚 on 2018/4/23. */
@Component
public class SpringApplicationContextHolder implements ApplicationContextAware {

  private static ApplicationContext context;

  public static Object getSpringBean(String beanName) {
    notEmpty(beanName, "bean name is required");
    return context == null ? null : context.getBean(beanName);
  }

  public static String[] getBeanDefinitionNames() {
    return context.getBeanDefinitionNames();
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {}
}
