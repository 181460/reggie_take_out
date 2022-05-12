package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @Description:
 * @author: jjw
 * @create: 2022-04-27-20:22
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;


    @GetMapping("/{id}")
    public R<Employee> update(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        return R.success(employee);
    }

    @PutMapping
    public R<String> handlestatus(@RequestBody Employee employee,HttpServletRequest request){
//        Long updateuser = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateUser(updateuser);
//        employee.setUpdateTime(LocalDateTime.now());
        boolean update = employeeService.updateById(employee);
        if(update){
            return R.success("修改成功！");
        }else{
            return R.error("修改失败！");
        }
    }

    @GetMapping("/page")
    public R<Page> page(int pageSize,int page,String name){
        Page pageinfo = new Page(page, pageSize);
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.like(name != null,Employee::getName,name);
        lqw.orderByDesc(Employee::getCreateTime);
        employeeService.page(pageinfo,lqw);
        return R.success(pageinfo);
    }

    @PostMapping
    private R<String> addEmployee(@RequestBody Employee employee,HttpServletRequest request){
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
//        employee.setCreateUser((Long) request.getSession().getAttribute("employee"));
        employeeService.save(employee);
        return R.success("添加员工信息成功");

    }

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(lqw);

        if(emp == null){
            return R.error("登陆失败");
        }
        if(!emp.getPassword().equals(password)){
            return R.error("登陆失败");
        }
        if(emp.getStatus() == 0){
            return R.error("该账号已经禁用！");
        }
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);

    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("推出成功");
    }


}
