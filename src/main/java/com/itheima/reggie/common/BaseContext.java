package com.itheima.reggie.common;

/**
 * @Description:
 * @author: jjw
 * @create: 2022-05-02-11:41
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
