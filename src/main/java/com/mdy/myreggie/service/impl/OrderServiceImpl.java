package com.mdy.myreggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mdy.myreggie.entity.Orders;
import com.mdy.myreggie.mapper.OrderMapper;
import com.mdy.myreggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper , Orders> implements OrderService {
    @Override
    public void submit(Orders orders) {

    }
}
