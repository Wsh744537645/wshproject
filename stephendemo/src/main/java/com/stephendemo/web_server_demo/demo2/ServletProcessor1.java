package com.stephendemo.web_server_demo.demo2;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

/**
 * 处理servlet的http请求
 * @author jmfen
 * date 2020-05-22
 */
public class ServletProcessor1 {

    public void process(ServletRequestDemo1 request, ServletResponseDemo1 response){
        String uri = request.getUri();
        String servletName = uri.substring(uri.lastIndexOf("/") + 1);
        URLClassLoader loader = null;
        try {
            URL[] urls = new URL[1];
            URLStreamHandler streamHandler = null;
            File classPath = new File(Constants.WEB_ROOT);
            String repository = (new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString();
            urls[0] = new URL(null, repository, streamHandler);
            loader = new URLClassLoader(urls);
        }catch (IOException e){
            System.out.println(e.toString());
        }

        Class myClass = null;
        try {
            myClass = loader.loadClass("com.stephendemo.web_server_demo.demo2.servlet." + servletName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

//        PrintWriter writer = null;
//        String preMessage = "HTTP/1.1 message page \r\n" +
//                "Content-Type: text/html\r\n" +
//                "Content-Length: 23\r\n" +
//                "\r\n";
//        String errorMessage = "HTTP/1.1 404 File NOT FOUND \r\n" +
//                "Content-Type: text/html\r\n" +
//                "Content-Length: 50\r\n" +
//                "\r\n" +
//                "<h1>Hello. Roses and red.</h1>";
//
//        try {
//            writer = response.getWriter();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String message = "<h1>File Not Found</h1>";
//        writer.println(errorMessage);


        Servlet servlet = null;
        try {
            servlet = (Servlet) myClass.newInstance();
            servlet.service(request, response);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}