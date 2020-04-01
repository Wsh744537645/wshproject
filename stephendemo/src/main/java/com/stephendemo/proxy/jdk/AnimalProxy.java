package com.stephendemo.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author jmfen
 * date 2020-03-17
 */
public class AnimalProxy implements InvocationHandler {
    Object object;

    public <T> T getInstance(T object){
        this.object = object;
        return (T)Proxy.newProxyInstance(object.getClass().getClassLoader(), object.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("调用前");
        Object result = method.invoke(this.object, args);
        System.out.println("调用后");
        return result;
    }
}