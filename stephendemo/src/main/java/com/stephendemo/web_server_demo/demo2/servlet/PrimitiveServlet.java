package com.stephendemo.web_server_demo.demo2.servlet;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author jmfen
 * date 2020-05-22
 */
public class PrimitiveServlet implements Servlet {

    String preMessage = "HTTP/1.1 message page \r\n" +
            "Content-Type: text/html\r\n" +
            "Content-Length: 50\r\n" +
            "\r\n" +
            "<h1>";

    String endMessage = "</h1>";

    String errorMessage = "HTTP/1.1 404 File NOT FOUND \r\n" +
            "Content-Type: text/html\r\n" +
            "Content-Length: 50\r\n" +
            "\r\n" +
            "<h1>Hello. Roses and red.</h1>";

    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("servlet init ......");
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        System.out.println("from service....");
        PrintWriter writer = res.getWriter();
//        String message = "Hello. Roses and red.";
//        writer.println(preMessage + message + endMessage);
        writer.println(errorMessage);
        System.out.println("service end....");
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {
        System.out.println("servlet destroy.....");
    }
}