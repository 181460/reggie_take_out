package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Category;

/**
 * @Description:
 * @author: jjw
 * @create: 2022-05-02-14:21
 */
public interface CategoryService extends IService<Category> {
    void removeById(Long id);
}
