package com.mdy.myreggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mdy.myreggie.entity.DishFlavor;
import com.mdy.myreggie.mapper.DishFlavorMapper;
import com.mdy.myreggie.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
