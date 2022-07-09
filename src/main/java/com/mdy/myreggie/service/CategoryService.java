package com.mdy.myreggie.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mdy.myreggie.entity.Category;

import java.util.HashMap;

public interface CategoryService extends IService<Category> {

    public void remove(Long id);


}
