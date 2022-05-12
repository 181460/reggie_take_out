package com.itheima.reggie.controller;

import ch.qos.logback.core.db.dialect.DBUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealService;
import com.itheima.reggie.service.impl.DishFlavorServiceImpl;
import com.sun.org.apache.bcel.internal.generic.NEW;
import javafx.scene.chart.CategoryAxis;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.pattern.PathPattern;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @author: jjw
 * @create: 2022-05-03-15:27
 */
@RestController
@RequestMapping("/dish")
public class DishController {


    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getByIDWithFlavor(id);
        return R.success(dishDto);

    }

    @PutMapping
    public R<String> put(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return R.success("修改成功！");
    }


    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.savaWithFlavor(dishDto);
        return R.success("添加成功");
    }

    @GetMapping("/page")
    public R<Page> save(int page,int pageSize,String name){
        Page<Dish> pageInfo = new Page(page, pageSize);
        Page<DishDto> dtoPageInfo = new Page<>();

        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.like(name != null,Dish::getName,name);
        dishService.page(pageInfo,lqw);
        BeanUtils.copyProperties(pageInfo,dtoPageInfo,"records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> dtoRecords = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(records.get(i),dishDto);
            Long id = dishDto.getCategoryId();
            Category category = categoryService.getById(id);
            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            dtoRecords.add(dishDto);

        }
        dtoPageInfo.setRecords(dtoRecords);
        return R.success(dtoPageInfo);

    }
    @PostMapping("/status/{Status}")
    public R<String> handlerstatus(@PathVariable Integer Status,String ids ){
        String[] split = ids.split(",");
        List<Dish> dishes = new ArrayList<Dish>();
        for (int i = 0; i < split.length; i++) {
            Long id = Long.parseLong(split[i]);
            Dish dish = new Dish();
            dish.setId(id);
            dish.setStatus(Status);
            dishes.add(dish);
        }
        dishService.updateBatchById(dishes);
        return R.success("修改成功");
    }

    @DeleteMapping
    public R<String> delete(String ids){
        String[] split = ids.split(",");
        for (int i = 0; i < split.length; i++) {
            Long id = Long.parseLong(split[i]);
            dishService.deleteWithFlavorById(id);
        }
        return R.success("删除成功");
    }
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){

        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
        lqw.eq(Dish::getStatus,1);
        lqw.like(dish.getName() != null,Dish::getName,dish.getName());
        lqw.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(lqw);
        List<DishDto> dtolist = list.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            dishDto.setCategoryName(category.getName());
            LambdaQueryWrapper<DishFlavor> wapper = new LambdaQueryWrapper<>();
            wapper.eq(DishFlavor::getDishId,item.getId());
            List<DishFlavor> flavors = dishFlavorService.list(wapper);
            dishDto.setFlavors(flavors);
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dtolist);
    }

}
