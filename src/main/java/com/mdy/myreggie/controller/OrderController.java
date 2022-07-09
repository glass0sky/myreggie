package com.mdy.myreggie.controller;


import com.baomidou.mybatisplus.core.conditions.interfaces.Func;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdy.myreggie.common.R;
import com.mdy.myreggie.dto.OrdersDto;
import com.mdy.myreggie.entity.Orders;
import com.mdy.myreggie.service.OrderService;
import com.sun.org.apache.xpath.internal.operations.Or;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/page")
    public R<Page> page(int page , int pageSize, Long number, String beginTime,String endTime){
        log.info("时间信息{}",beginTime);
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(number!=null,Orders::getNumber,number);
        queryWrapper.ge(StringUtils.isNotEmpty(beginTime),Orders::getOrderTime,beginTime).le(StringUtils.isNotEmpty(beginTime),Orders::getOrderTime,endTime);

        orderService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);

    }

    /**
     * 订单状态 1待付款，2待派送，3已派送，4已完成，5已取消
     * @return
     */
    @PutMapping
    public R<String> updateStatus(@RequestBody Orders orders){
        log.info("信息：{}",orders.toString());
        orderService.updateById(orders);
        return R.success("状态更新正常");

    }


}
