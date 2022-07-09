package com.mdy.myreggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdy.myreggie.common.R;
import com.mdy.myreggie.dto.DishDto;
import com.mdy.myreggie.entity.Dish;
import com.mdy.myreggie.entity.Setmeal;
import com.mdy.myreggie.service.DishService;
import com.mdy.myreggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    DishService dishService;
    @Autowired
    SetmealService setmealService;

    @GetMapping("/page")
    public R<Page> page(int  page, int pageSize,String name){
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name),Dish::getName,name);
        dishService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);

    }

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        return R.success("新增成功...");
    }


    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return R.success("修改成功...");
    }
    @GetMapping("/{id}")
    public R<Dish> getDishDtoById(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 修改状态
     * @param ids
     * @param statusId
     * @return
     */
    @PostMapping("/status")
    public R<String> updateStatus(Long[] ids, int statusId){
        log.info("修改状态 ：{}",statusId);
        for (int i = 0; i < ids.length; i++) {
            Dish dish = new Dish();
            dish.setStatus(statusId);
            dish.setId(ids[i]);
            dishService.updateById(dish);
        }


        return R.success("修改状态正确！");
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long[] ids){
        for (int i = 0; i < ids.length; i++) {
            dishService.removeById(ids[i]);
        }
        return R.success("成功删除...");
    }

    /**
     * 根据条件查询菜品数据
     *
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        return R.success(list);
    }

}
