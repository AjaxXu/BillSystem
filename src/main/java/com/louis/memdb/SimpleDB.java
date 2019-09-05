/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.louis.memdb;

import com.louis.container.annotation.ServiceProvider;
import com.louis.util.LogUtil;
import com.louis.util.RandomUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author dalong.wdl
 * @version $Id: SimpleDB.java, v 0.1 2019年08月29日 10:20 AM dalong.wdl Exp $
 */
@ServiceProvider(id = "simple_db")
public class SimpleDB implements MemDB {
    private Map<String, Object> data = new ConcurrentHashMap<String, Object>();

    @Override
    public boolean insert(String key, Object row) {
        mockSleep();

        data.put(key, row);


        return true;
    }

    @Override
    public boolean update(String key, Object row) {
        mockSleep();

        data.put(key, row);

        return true;
    }

    @Override
    public boolean remove(String key) {
        mockSleep();

        data.remove(key);

        return true;
    }

    @Override
    public <T> T getObject(String key) {
        mockSleep();

        return (T) data.get(key);
    }

    @Override
    public <T> List<T> scan(String prefix, int limit) {
        return null;
    }

    public List allDBObject() {
        Collection values = data.values();
        return new ArrayList(values);
    }

    @Override
    public void commit() {

    }

    @Override
    public void begin() {

    }

    @Override
    public void rollback() {

    }

    private void mockSleep(){

        if(RandomUtil.randomEvent(10)){
            try {
                Thread.sleep(RandomUtil.genNumBetween(1, 3));
            } catch (InterruptedException e) {
                LogUtil.nothing();
            }
        }else if(RandomUtil.randomEvent(2)){
            throw new RuntimeException("dbError");
        }
    }
}