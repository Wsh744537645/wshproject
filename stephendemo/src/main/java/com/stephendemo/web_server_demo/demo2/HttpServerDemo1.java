package com.stephendemo.web_server_demo.demo2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * How Tomcat works 第二章例子
 *  实现简易的web server，增加servlet，实现请求动态处理
 * @author jmfen
 * date 2020-05-22
 */
public class HttpServerDemo1 {
    private static final String SHUTDOWN_COMMAND = "/shutdown";
    private boolean shutdown = false;

    public static void main(String[] args) {
        new HttpServerDemo1().await();
    }

    public void await(){
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8080, 1, InetAddress.getByName("127.0.0.1"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        while (!shutdown){
            Socket socket = null;
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                socket = serverSocket.accept();
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();

                ServletRequestDemo1 request = new ServletRequestDemo1(inputStream);
                request.parse();

                ServletResponseDemo1 response = new ServletResponseDemo1(outputStream);
                response.setRequest(request);

                if(request.getUri().startsWith("/servlet/")){
                    ServletProcessor1 processor1 = new ServletProcessor1();
                    processor1.process(request, response);
                }else {
                    StaticResourceProcessor processor = new StaticResourceProcessor();
                    processor.process(request, response);
                }

                socket.close();
                shutdown = request.getUri().equals(SHUTDOWN_COMMAND);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}