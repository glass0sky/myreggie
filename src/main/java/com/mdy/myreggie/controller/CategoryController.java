package com.mdy.myreggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mdy.myreggie.common.R;
import com.mdy.myreggie.entity.Category;
import com.mdy.myreggie.entity.Employee;
import com.mdy.myreggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/list")
    public R<List<Category>> list(int type){

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getType,type);
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);

    }
    @DeleteMapping
    public R<String> delete(Long ids){
        log.info("删除信息：{}",ids);
        categoryService.remove(ids);
        return R.success("删除成功");
    }

    /**
     * 更新信息
     * @param
     * @param request
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category , HttpServletRequest request){
        log.info("更新信息..{}",category.toString());
        Long empId = (Long)request.getSession().getAttribute("employee");
        category.setCreateTime(LocalDateTime.now());


        category.setCreateUser(empId);
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(empId);
        categoryService.updateById(category);
        return R.success("信息更新成功！");
    }
    /**
     * 新增菜品或套餐
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category, HttpServletRequest request){
        Integer type = category.getType();
        log.info("新增信息:{}",category.toString());
        log.info("新增类型：{}",type);
        category.setCreateTime(LocalDateTime.now());
        HttpSession session = request.getSession();
        Long empId = (Long) session.getAttribute("employee");
        category.setCreateUser(empId);
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(empId);
        categoryService.save(category);
        log.info("新增信息：{}",category.toString());
        return R.success("新增成功！");
    }

    @RequestMapping("/page")
    public R<Page> page( int page,int pageSize){
        Page<Category> pageInfo = new Page<>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件，根据sort进行排序
        queryWrapper.orderByAsc(Category::getSort);

        categoryService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);

    }

}
