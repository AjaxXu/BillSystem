/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.louis.container.aop;

import java.lang.reflect.Method;

/**
 * 拦截器器上下文。参考spring 拦截器定义
 *
 * @author dalong.wdl
 * @version $Id: MethodInvocation.java, v 0.1 2019年08月29日 9:23 AM dalong.wdl Exp $
 */
public interface MethodInvocation {

    /**
     * 调用原始方法
     *
     * @return
     */
    Object process();

    /**
     * 被拦截的方法定义
     *
     * @return
     */
    Method getMethod();

    /**
     * 返回当前拦截的，调用参数。
     *
     * @return
     */
    Object[] getArguments();
}