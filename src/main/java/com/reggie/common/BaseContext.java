package com.reggie.common;


public class BaseContext {
    private static ThreadLocal<Long> threadLocald = new ThreadLocal<>();

    public static void setCurredentId(Long id){
        threadLocald.set(id);
    }
    public static Long getCurrentId(){
        return threadLocald.get();
    }
}
