/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.louis.train.booking.service.impl;

import com.louis.container.annotation.ServiceProvider;
import com.louis.memdb.MemDB;

import java.util.concurrent.Callable;

/**
 *
 * @author dalong.wdl
 * @version $Id: TransactionEngine.java, v 0.1 2019年09月03日 9:23 AM dalong.wdl Exp $
 */
@ServiceProvider(id = "engine")
public class TransactionEngine {

    public <T> T doProcess(MemDB db, Callable<T> callable) {

        T ret = null;
        try {
            db.begin();

            ret = callable.call();

            db.commit();
        } catch (Throwable e) {
            db.rollback();

            throw new RuntimeException(e.getMessage(), e);
        }

        return ret;
    }
}