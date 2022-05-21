package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.OrdersDto;
import com.itheima.reggie.entity.OrderDetail;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.service.OrderDetailService;
import com.itheima.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }
    @GetMapping("/userPage")
    public R<Page> userPage(Integer page,Integer pageSize){
        Page<Orders> orderPage = new Page(page, pageSize);
        Page<OrdersDto> orderDtoPage = new Page(page, pageSize);

        LambdaQueryWrapper<Orders> lqw = new LambdaQueryWrapper<>();
        Long currentId = BaseContext.getCurrentId();
        lqw.eq(Orders::getUserId, currentId);
        lqw.orderByDesc(Orders::getOrderTime);
        orderService.page(orderPage, lqw);
        BeanUtils.copyProperties(orderPage,orderDtoPage,"records");
        List<Orders> records = orderPage.getRecords();
        OrdersDto ordersDto = new OrdersDto();
        List<OrdersDto> dtoList = records.stream().map((item)->{
            BeanUtils.copyProperties(item,ordersDto);
            Long id = item.getId();
            LambdaQueryWrapper<OrderDetail> wapper = new LambdaQueryWrapper<>();
            wapper.eq(OrderDetail::getOrderId, id);
            List<OrderDetail> list = orderDetailService.list(wapper);
            ordersDto.setOrderDetails(list);
            return ordersDto;
        }).collect(Collectors.toList());
        orderDtoPage.setRecords(dtoList);
        return R.success(orderDtoPage);

    }
    @GetMapping("/page")
    public R<Page> page(Integer page,Integer pageSize,String number){
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> lqw = new LambdaQueryWrapper<>();
        lqw.like(number != null,Orders::getNumber,number);
        orderService.page(ordersPage, lqw);
        return R.success(ordersPage);

    }

}