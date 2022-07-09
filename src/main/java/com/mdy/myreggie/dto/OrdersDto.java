package com.mdy.myreggie.dto;


import com.mdy.myreggie.entity.OrderDetail;
import com.mdy.myreggie.entity.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
