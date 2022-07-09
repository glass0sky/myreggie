package com.mdy.myreggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mdy.myreggie.common.CustomException;
import com.mdy.myreggie.entity.Category;
import com.mdy.myreggie.entity.Dish;
import com.mdy.myreggie.entity.Setmeal;
import com.mdy.myreggie.mapper.CategoryMapper;
import com.mdy.myreggie.service.CategoryService;
import com.mdy.myreggie.service.DishService;
import com.mdy.myreggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category > implements CategoryService {
    @Autowired
    DishService dishService;
    @Autowired
    SetmealService setmealService;
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId,id);
        int count = dishService.count(queryWrapper);
        if (count>0){
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }
        LambdaQueryWrapper<Setmeal> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Setmeal::getCategoryId,id);
         count = setmealService.count(queryWrapper1);
        if (count>0){
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }

        super.removeById(id);

    }
}
