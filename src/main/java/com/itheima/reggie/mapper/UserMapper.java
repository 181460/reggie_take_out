package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description:
 * @author: jjw
 * @create: 2022-05-07-16:45
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
