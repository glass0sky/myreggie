package com.mdy.myreggie.controller;


import com.baomidou.mybatisplus.core.conditions.interfaces.Func;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdy.myreggie.common.R;
import com.mdy.myreggie.dto.OrdersDto;
import com.mdy.myreggie.entity.OrderDetail;
import com.mdy.myreggie.entity.Orders;
import com.mdy.myreggie.service.OrderDetailService;
import com.mdy.myreggie.service.OrderService;
import com.sun.org.apache.xpath.internal.operations.Or;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    OrderDetailService orderDetailService;

    //15077562543
    @RequestMapping("/userPage")
    public R<Page> page(int page, int pageSize, HttpServletRequest request){
        log.info("page = {},pageSize = {},name = {}" ,page,pageSize);

        //构造分页构造器
        Page<OrderDetail> pageInfo = new Page<>(page,pageSize);

        //构造条件构造器
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        Long userId = (Long) request.getSession().getAttribute("user");
        queryWrapper.eq("user_id",userId);

        //执行查询,拿到用户订单号
        List<Orders> list = orderService.list(queryWrapper);
        //根据订单号查询订单细节
        for (int i = 0; i < list.size(); i++) {
            LambdaQueryWrapper<OrderDetail> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(OrderDetail::getOrderId,list.get(i).getId());
            orderDetailService.page(pageInfo, queryWrapper1);
        }

        List records = pageInfo.getRecords();
        System.out.println(records);
        log.info("分页信息：{}",records);

        return R.success(pageInfo);
    }

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

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }

}
