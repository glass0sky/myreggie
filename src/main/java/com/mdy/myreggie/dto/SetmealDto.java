package com.mdy.myreggie.dto;


import com.mdy.myreggie.entity.Setmeal;
import com.mdy.myreggie.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
