package Iocdemo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jmfen
 * date 2020-01-17
 */
public class TestApp {
    interface IHello{
        void sayHello();
    }

    static class Hello implements IHello{
        @Override
        public void sayHello() {
            System.out.println("hello world");
        }
    }

    static class HelloProxy implements InvocationHandler{
        private Object object;

        public Object bind(IHello hello){
            object = hello;
            return Proxy.newProxyInstance(hello.getClass().getClassLoader(), hello.getClass().getInterfaces(), this);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("hehe");
            return method.invoke(object, args);
        }
    }

    public static void main(String[] args) {
        IHello hello = (IHello) new HelloProxy().bind(new Hello());
        hello.sayHello();
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", true);
    }
}