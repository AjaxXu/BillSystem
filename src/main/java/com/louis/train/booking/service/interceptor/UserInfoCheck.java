/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.louis.train.booking.service.interceptor;

import com.louis.container.aop.MethodInterceptor;
import com.louis.container.aop.MethodInvocation;

import static com.louis.train.mock.MockService.NO_LOGIN_USER;

/**
 *
 * @author dalong.wdl
 * @version $Id: UserInfoCheck.java, v 0.1 2019年08月29日 9:51 AM dalong.wdl Exp $
 */
public class UserInfoCheck implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (invocation.getMethod().getName().equals("buy")) {
            if (invocation.getArguments()[0].equals(NO_LOGIN_USER)) {
                return null;
            }
        }
        return invocation.process();
    }
}