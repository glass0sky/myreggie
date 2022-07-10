package com.mdy.myreggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mdy.myreggie.entity.ShoppingCart;
import com.mdy.myreggie.mapper.ShoppingCartMapper;
import com.mdy.myreggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper , ShoppingCart> implements ShoppingCartService {
}
