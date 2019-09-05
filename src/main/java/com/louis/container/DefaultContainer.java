/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.louis.container;

import com.louis.container.annotation.ServiceProvider;
import com.louis.container.injection.AutoInjection;
import com.louis.util.EnhancedServiceLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 简单服务容器
 *
 * @author dalong.wdl
 * @version $Id: DefaultContainer.java, v 0.1 2019年08月29日 10:03 AM dalong.wdl Exp $
 */
public class DefaultContainer implements Container {
    private static Logger LOGGER = Logger.getLogger(DefaultContainer.class.getName());

    private static String resourceName = "beans.properties";

    private Map<Class, Object> localCache;
    private Map<String, Object> idToBean; // 只在第一次加载时扫描，不存在并发问题

    private static Container ins;

    private DefaultContainer() {
        localCache = new ConcurrentHashMap<>();
        idToBean = new HashMap<>();
        // 初始化bean
        AutoInjection.autoInjection(resourceName, findClassLoader(), idToBean);
    }

    @Override
    public <T> T getBean(String beanId) {
        return (T)idToBean.get(beanId);
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        if (localCache.containsKey(clazz)) {
            return (T) localCache.get(clazz);
        }

        if (clazz.isAnnotationPresent(ServiceProvider.class)) {
            ServiceProvider provider = (ServiceProvider) clazz.getAnnotation(ServiceProvider.class);
            if (idToBean.containsKey(provider.id()) && idToBean.get(provider.id()) != null) {
                T obj = (T)idToBean.get(provider.id());
                localCache.put(clazz, obj);
                return obj;
            }
        }

        if (!clazz.isInterface()) {
            try {
                localCache.put(clazz, clazz.newInstance());
                return (T) localCache.get(clazz);
            } catch (Exception e) {
                LOGGER.log(Level.FINEST, e.getMessage(), e);
            }
        }

        return null;
    }

    private static ClassLoader findClassLoader() {
        return EnhancedServiceLoader.class.getClassLoader();
    }

    public static void setResourceName(String resourceName) {
        DefaultContainer.resourceName = resourceName;
    }

    private static class ContainerHolder {
        private static DefaultContainer instance = new DefaultContainer();
    }

    public static Container getInstance() {
        return ContainerHolder.instance;
    }

}