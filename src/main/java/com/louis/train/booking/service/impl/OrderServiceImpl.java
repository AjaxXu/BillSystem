/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.louis.train.booking.service.impl;

import com.louis.container.annotation.ServiceProvider;
import com.louis.container.annotation.ServiceReference;
import com.louis.memdb.MemDB;
import com.louis.train.booking.model.BookingOrder;
import com.louis.train.booking.model.enums.OrderStatus;
import com.louis.train.booking.service.OrderService;
import com.louis.train.booking.service.interceptor.UserInfoCheck;

import java.util.List;

/**
 *
 * @author dalong.wdl
 * @version $Id: OrderServiceImpl.java, v 0.1 2019年08月29日 9:46 AM dalong.wdl Exp $
 */
@ServiceProvider(id = "orderService", aop = UserInfoCheck.class)
public class OrderServiceImpl implements OrderService {

    @ServiceReference(ref = "simple_db")
    private MemDB db;

    @ServiceReference(ref = "engine")
    private TransactionEngine engine;

    private int i = 0;

    @Override
    public BookingOrder buy(String userName, int ticketCount) {
        BookingOrder order = new BookingOrder();

        order.setOrderNo("biz_" + System.currentTimeMillis() + "_" + userName + "_" + i++);
        order.setUserName(userName);
        order.setTicketCount(ticketCount);
        order.setStatus(OrderStatus.BOOKING_OK);

        return engine.doProcess(db, () -> {

            db.insert(order.getOrderNo(), order);

            return order;
        });
    }

    @Override
    public boolean unbuy(String orderNo) {

        return engine.doProcess(db, () -> db.remove(orderNo));
    }

    @Override
    public List<BookingOrder> listBookingOrder(int pageNo, int pageSize) {
        return null;
    }
}