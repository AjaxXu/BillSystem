/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.louis.train.main;

import com.louis.container.Container;
import com.louis.container.DefaultContainer;
import com.louis.memdb.MemDB;
import com.louis.memdb.SimpleDB;
import com.louis.train.booking.model.BookingOrder;
import com.louis.train.booking.service.OrderService;
import com.louis.train.booking.service.impl.OrderServiceImpl;
import com.louis.train.mock.MockService;
import com.louis.train.mock.MockUser;
import com.louis.util.LogUtil;
import com.louis.util.RandomUtil;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 *
 * @author dalong.wdl
 * @version $Id: Starter.java, v 0.1 2019年08月29日 9:56 AM dalong.wdl Exp $
 */
public class Starter {
    private static final Logger LOGGER = Logger.getLogger("main");

    public static void main(String[] args) throws InterruptedException {
        LogUtil.initLogger();

        LogUtil.info(LOGGER, "初始化火车票订票模拟系统，服务容器....");
        Container container = DefaultContainer.getInstance();

        final OrderService orderService = container.getBean(OrderServiceImpl.class);
        LogUtil.info(LOGGER, "订单服务加载，bean:" + orderService);

        //初始化10万用户列表
        final MockService mockService = new MockService();
        List<MockUser> userList = mockService.getAllUser();
        LogUtil.info(LOGGER, "初始化用户数量:" + userList.size());

        //执行用户并发模拟
        doUserMockAndWaitDone(orderService, userList);

        //盘点购票订单信息
        doResultCheck(mockService, container);

        LogUtil.info(LOGGER, "火车票订购系统模拟结束.");
        LogUtil.info(LOGGER, "DONE");
    }

    private static void doUserMockAndWaitDone(final OrderService orderService, List<MockUser> userList)
            throws InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 20, 100, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(MockService.MAX_USER_COUNT));

        final AtomicInteger taskCount = new AtomicInteger(0);
        //提交所有购票用户
        for (final MockUser user : userList) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        BookingOrder order = orderService.buy(user.getName(), user.getTicketCount());
                        taskCount.incrementAndGet();
                        if (order != null) {
                            //黄牛50%随机性退票
                            if (order.getUserName().startsWith(MockService.HUANG_NIU) == RandomUtil.randomEvent(50)) {
                                orderService.unbuy(order.getOrderNo());
                            }

                            //正常用户5% 随机退票
                            else if (order.getUserName().startsWith(MockService.USER) == RandomUtil.randomEvent(5)) {
                                orderService.unbuy(order.getOrderNo());
                            }
                        }
                    } catch (Throwable e) {
                        LogUtil.error(LOGGER, e, "用户订票操作异常, 用户账号:" + user.getName() + ", 票张数:" + user.getTicketCount());
                    }
                }
            });
        }

        //等待1分钟购票完成
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        LogUtil.info(LOGGER, "模拟订票操作调用成功次数，taskCount:" + taskCount.get());

    }

    private static void doResultCheck(MockService mockService, Container container) {

        final MemDB memDB = container.getBean(SimpleDB.class);
        List allTicketCount = memDB.allDBObject();

        mockService.printOrderSummary(allTicketCount);

    }

}