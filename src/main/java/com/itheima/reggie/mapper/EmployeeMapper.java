package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description:
 * @author: jjw
 * @create: 2022-04-27-20:16
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
