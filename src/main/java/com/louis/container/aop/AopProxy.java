package com.louis.container.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Louis
 */
public class AopProxy {
    private MethodInterceptor interceptor;

    public void setInterceptor(MethodInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public Object getProxy(Object object) {
        return Proxy.newProxyInstance(object.getClass().getClassLoader(),
            object.getClass().getInterfaces(), new InvocationHandler() {
                //在下面的invoke方法里面写我们的业务
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return interceptor.invoke(new MethodInvocation() {
                        @Override
                        public Object process() {
                            try {
                                return method.invoke(object, args);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }

                        @Override
                        public Method getMethod() {
                            return method;
                        }

                        @Override
                        public Object[] getArguments() {
                            return args;
                        }
                    });
                }
            });

    }
}
