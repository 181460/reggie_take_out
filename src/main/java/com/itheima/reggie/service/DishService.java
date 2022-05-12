package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.PrivateKey;

/**
 * @Description:
 * @author: jjw
 * @create: 2022-05-03-6:53
 */
public interface DishService extends IService<Dish> {

    /**
     * 保存菜品信息和口味信息
     * @param dishDto
     */
    void savaWithFlavor(DishDto dishDto);

    /**
     *
     * 根据id查询菜品信息以及口味信息
     * @param id
     * @return
     */
    DishDto getByIDWithFlavor(Long id);

    /**
     * 更新菜品信息同时跟新口味信息
     *
     */
    void updateWithFlavor(DishDto dishDto);

    /**
     * 通过id删除菜品和相关口味信息
     * @param id
     */
    void deleteWithFlavorById(Long id);

}
