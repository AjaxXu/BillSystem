/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.louis.train.booking.service.interceptor;

import com.louis.container.annotation.IgnoreCondition;
import com.louis.container.aop.MethodInterceptor;
import com.louis.container.aop.MethodInvocation;

/**
 *
 * @author dalong.wdl
 * @version $Id: UserInfoCheck.java, v 0.1 2019年08月29日 9:51 AM dalong.wdl Exp $
 */
public class UserInfoCheck implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (invocation.getMethod().isAnnotationPresent(IgnoreCondition.class)) {
            IgnoreCondition condition = invocation.getMethod().getAnnotation(IgnoreCondition.class);
            if (((String)invocation.getArguments()[condition.ordinal()]).startsWith(condition.value())) {
                return null;
            }
        }
        return invocation.process();
    }
}