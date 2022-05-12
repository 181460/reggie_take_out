package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;

import java.util.List;

/**
 * @Description:
 * @author: jjw
 * @create: 2022-05-03-6:55
 */
public interface SetmealService extends IService<Setmeal> {

    /**
     *
     * 保存套餐信息和套餐内的菜品信息
     * @param setmealDto
     */
    void saveWithDishes(SetmealDto setmealDto);

    /**
     * 获取套餐信息
     * @param id
     * @return
     */
    SetmealDto getWithDishByid(Long id);

    /**
     * 跟新套餐信息
     */
    void updateWithDishes(SetmealDto setmealDto);

    void removeWithDishes(List<Long> ids);
}
