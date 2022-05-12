package com.itheima.reggie.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

/**
 * @Description:
 * @author: jjw
 * @create: 2022-04-29-21:33
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLException.class)
    public R<String> exceptionHandler(SQLException exception){
        if(exception.getMessage().contains("Duplicate entry")){
            String[] s = exception.getMessage().split(" ");
            String message = "账户 "+s[2]+"已存在";
            return R.error(message);
        }
        return R.error("发生错误");
    }

    @ExceptionHandler(CustomerException.class)
    public R<String> exceptionHandler(CustomerException exception) {
        return R.error(exception.getMessage());
    }



}
