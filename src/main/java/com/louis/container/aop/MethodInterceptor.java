/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.louis.container.aop;

/**
 * aop拦截器接， 参考spring 拦截器定义
 *
 * @author dalong.wdl
 * @version $Id: invocation.java, v 0.1 2019年08月29日 9:22 AM dalong.wdl Exp $
 */
public interface MethodInterceptor {

    /**
     * 方法调用拦截，如果执行原始方法，调用invocation.process()
     *
     * @param invocation - 方法调用上下文
     *
     * @return 返回给调用方结果
     */
    Object invoke(MethodInvocation invocation) throws Throwable;

}