package com.mdy.myreggie.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mdy.myreggie.entity.Category;

public interface CategoryService extends IService<Category> {

    public void remove(Long id);

}
