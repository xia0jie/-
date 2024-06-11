package com.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.common.CustomException;
import com.reggie.common.R;
import com.reggie.entity.Category;
import com.reggie.entity.Dish;
import com.reggie.entity.Setmeal;
import com.reggie.mapper.CategoryMapper;
import com.reggie.service.CategoryService;
import com.reggie.service.DishService;
import com.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServicelmpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    @Override
    public void remove(Long id){
        LambdaQueryWrapper<Dish> DishqueryWrapper = new LambdaQueryWrapper<>();
        DishqueryWrapper.eq(Dish::getCategoryId,id);
        int countDish = dishService.count(DishqueryWrapper);
        if(countDish>0){
            throw new CustomException("当前分类下存在菜品");
        }
        LambdaQueryWrapper<Setmeal> SetmealqueryWrapper = new LambdaQueryWrapper<>();
        SetmealqueryWrapper.eq(Setmeal::getCategoryId,id);
        int countSetmeal = setmealService.count(SetmealqueryWrapper);
        if(countSetmeal>0){
            throw new CustomException("当前分类下存在套餐");
        }
    }
}
