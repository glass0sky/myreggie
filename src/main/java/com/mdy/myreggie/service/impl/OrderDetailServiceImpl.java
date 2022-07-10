package com.mdy.myreggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mdy.myreggie.entity.OrderDetail;
import com.mdy.myreggie.mapper.OrderDetailMapper;
import com.mdy.myreggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
