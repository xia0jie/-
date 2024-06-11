package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.reggie.common.BaseContext;
import com.reggie.common.R;
import com.reggie.entity.ShoppingCart;
import com.reggie.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    ShoppingCartService shoppingCartService;
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        //添加用户ID
        Long curId = BaseContext.getCurrentId();
        shoppingCart.setUserId(curId);
        //获取商品ID
        Long dishId = shoppingCart.getDishId();
        //菜肴 or 套餐
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,curId);
        if(dishId != null){
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else{
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        //存在 or 新增
        ShoppingCart shoppingCartOne = shoppingCartService.getOne(queryWrapper);
        if(shoppingCartOne!=null){
            Integer num = shoppingCartOne.getNumber();
            num = num+1;
            shoppingCartOne.setNumber(num);
            shoppingCartService.updateById(shoppingCartOne);
            return R.success(shoppingCartOne);
        }else{
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            return R.success(shoppingCart);
        }
    }
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }
    @DeleteMapping("clean")
    public R<String> clean(){
        Long userid = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userid);
        shoppingCartService.remove(queryWrapper);
        return R.success("清空成功");
    }
}
