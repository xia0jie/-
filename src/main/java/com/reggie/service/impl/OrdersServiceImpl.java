package com.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.common.BaseContext;
import com.reggie.entity.*;
import com.reggie.mapper.OrdersMapper;
import com.reggie.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>implements OrdersService {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Override
    public void submit(Orders orders) {
        //当前用户ID
        Long userId = BaseContext.getCurrentId();
        //得到购物车数据
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        //得到用户信息
        User user = userService.getById(userId);
        //地址
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        //订单号,时间,....
        Long orderid = IdWorker.getId();
        orders.setNumber(String.valueOf(orderid));
        orders.setOrderTime(LocalDateTime.now());
        orders.setUserId(userId);
        //总价
        int amount = 0;
        for (int i = 0; i < list.size(); i++) {
            amount = amount +list.get(i).getAmount().intValue();
        }
        orders.setAmount(BigDecimal.valueOf(amount));
        orders.setCheckoutTime(LocalDateTime.now());
        this.save(orders);
        //订单明细表
        List<OrderDetail> orderDetails = list.stream().map(item->{
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setOrderId(orders.getId());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setNumber(item.getNumber());
            orderDetail.setAmount(item.getAmount());
            return orderDetail;
        }).collect(Collectors.toList());
        orderDetailService.saveBatch(orderDetails);
        //清空购物车
        shoppingCartService.remove(queryWrapper);
    }
}
