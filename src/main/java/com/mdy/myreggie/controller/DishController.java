package com.mdy.myreggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdy.myreggie.common.R;
import com.mdy.myreggie.dto.DishDto;
import com.mdy.myreggie.entity.Category;
import com.mdy.myreggie.entity.Dish;
import com.mdy.myreggie.entity.DishFlavor;
import com.mdy.myreggie.entity.Setmeal;
import com.mdy.myreggie.service.CategoryService;
import com.mdy.myreggie.service.DishFlavorService;
import com.mdy.myreggie.service.DishService;
import com.mdy.myreggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    DishService dishService;
    @Autowired
    SetmealService setmealService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    DishFlavorService dishFlavorService;
    @Autowired
    RedisTemplate redisTemplate;

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
        //1、删除所有的菜品缓存数据
//        Set keys = redisTemplate.keys("dish_*");
//        redisTemplate.delete(keys);
        //2、精确清除某个分类下的菜品
        String key= "dish_"+dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);
        return R.success("新增成功...");
    }


    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        //1、删除所有的菜品缓存数据
//        Set keys = redisTemplate.keys("dish_*");
//        redisTemplate.delete(keys);
        //2、精确清除某个分类下的菜品
       String key= "dish_"+dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);
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
    public R<List<DishDto>> list(Dish dish){

        List<DishDto> dishDtoList =null;

        //redis缓存
        String key="dish_"+dish.getCategoryId()+"_"+dish.getStatus();
        ValueOperations ops = redisTemplate.opsForValue();
        dishDtoList= (List<DishDto>) ops.get(key);
        if (dishDtoList!=null){
            return R.success(dishDtoList);
        }

        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);

        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

         dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            //当前菜品的id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            //SQL:select * from dish_flavor where dish_id = ?
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());
        ops.set(key,dishDtoList,60, TimeUnit.MINUTES);
        return R.success(dishDtoList);
    }

}
