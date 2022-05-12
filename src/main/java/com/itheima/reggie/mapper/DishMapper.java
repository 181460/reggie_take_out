package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

import javax.annotation.ManagedBean;

/**
 * @Description:
 * @author: jjw
 * @create: 2022-05-03-6:52
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
