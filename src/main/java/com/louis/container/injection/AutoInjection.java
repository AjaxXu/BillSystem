package com.louis.container.injection;

import com.louis.container.annotation.ServiceProvider;
import com.louis.container.annotation.ServiceReference;
import com.louis.util.ClassUtil;
import com.louis.util.EnhancedServiceLoader;
import com.louis.util.LogUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 自动注入类
 *
 * @author Louis
 */
public class AutoInjection {
    private static Logger LOGGER = Logger.getLogger(AutoInjection.class.getName());
    private static final String SERVICES_DIRECTORY = "META-INF/service/";

    public static void autoInjection(String resourceName, ClassLoader classLoader, Map<String, Object> beans) {
        List<Class> extensions = new ArrayList<>();
        try {
            EnhancedServiceLoader.loadFile(resourceName, SERVICES_DIRECTORY, classLoader, extensions);
            for (Class cl: extensions) {
                try {
                    injection(cl, beans);
                } catch (Exception e) {
                    LogUtil.error(LOGGER, e, String.format("Injection %s class fail.", cl.getName()));
                }
            }
        } catch (IOException e) {
            LogUtil.nothing();
        }
    }

    /**
     * 把ServiceProvider注解的类存入内存中
     * @param cl 类
     * @param beans bean存储容器
     * @throws Exception
     */
    private static void injection(Class cl, Map<String, Object> beans) throws Exception {
        if (cl.isAnnotationPresent(ServiceProvider.class)) {
            ServiceProvider provider = (ServiceProvider) cl.getAnnotation(ServiceProvider.class);
            if (beans.containsKey(provider.id()) && beans.get(provider.id()) != null) {
                throw new Exception("Circular dependency");
            }

            Object instance = cl.newInstance();
            // 下面是ServiceRefrence的注入
            Field[] fields = cl.getDeclaredFields();
            boolean refExist;
            for (Field field : fields) {
                refExist = field.isAnnotationPresent(ServiceReference.class);

                if (refExist) {
                    String classType = field.getGenericType().toString();
                    Class fieldClass = null;
                    try {
                        fieldClass = Class.forName(classType.substring(6));
                    } catch (ClassNotFoundException e) {
                        try {
                            fieldClass = Class.forName(classType.substring(10));
                        } catch (ClassNotFoundException ex) {
                            LogUtil.error(LOGGER, ex, String.format("Can't find class: %s", classType));
                        }
                    }

                    //强制设置值 破坏了封装性
                    field.setAccessible(true);
                    List<Class> classes = ClassUtil.getAllClassByInterface(fieldClass);
                    boolean flag = false;
                    for (Class c: classes) {
                        if (c.isAnnotationPresent(ServiceProvider.class)) {
                            ServiceProvider refBean = (ServiceProvider) c.getAnnotation(ServiceProvider.class);
                            injection(c, beans);
                            field.set(instance, beans.get(refBean.id()));
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        Object object = fieldClass.newInstance();
                        field.set(instance, object);
                    }
                }
            }
            beans.put(provider.id(), instance);
        }
    }
}
