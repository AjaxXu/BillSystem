package com.louis.container.aop;

import com.louis.container.annotation.ServiceProvider;

import java.util.Map;

public class ProxyFactory {
    public static void makeProxyBean(Map<String, Object> map) throws IllegalAccessException, InstantiationException {

        for (String key : map.keySet()) {

            AopProxy aopProxy = new AopProxy();

            Object o = map.get(key);
            Class classes = o.getClass();
            //判断是否有类注解
            if (classes.isAnnotationPresent(ServiceProvider.class)) {

                ServiceProvider service = (ServiceProvider) classes.getAnnotation(ServiceProvider.class);
                if (!service.aop().isInterface()) {
                    Class<? extends MethodInterceptor> aClass = service.aop();
                    MethodInterceptor interceptor = aClass.newInstance();
                    aopProxy.setInterceptor(interceptor);
                    map.put(key, aopProxy.getProxy(map.get(key)));
                }

            }
        }
    }
}