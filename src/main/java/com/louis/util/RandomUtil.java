/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.louis.util;

import java.util.Random;

/**
 *
 * @author dalong.wdl
 * @version $Id: RandomUtil.java, v 0.1 2019年08月29日 4:15 PM dalong.wdl Exp $
 */
public class RandomUtil {
    private final static Random RANDOM = new Random();

    /**
     * 按照区间生成随机数
     *
     * @param min
     * @param max
     * @return
     */
    public static int genNumBetween(int min, int max) {
        return RANDOM.nextInt(max - min + 1) + min;
    }

    /**
     * 随机返回事件
     *
     * @param percent
     * @return
     */
    public static boolean randomEvent(int percent){
        return genNumBetween(1, 100) < percent;
    }
}