package com.stephendemo.web_server_demo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author jmfen
 * date 2020-05-22
 */
public class HttpServerDemo {
    public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";
    public static final String SHUTDOWN_COMMOND = "/shutdown";

    private boolean shutdown = false;

    public static void main(String[] args) {
        HttpServerDemo httpServer = new HttpServerDemo();
        httpServer.await();
    }

    public void await(){
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8080, 1, InetAddress.getByName("127.0.0.1"));
        }catch (IOException e){
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

                RequestDemo request = new RequestDemo(inputStream);
                request.parse();

                ResponseDemo response = new ResponseDemo(outputStream);
                response.setRequest(request);
                response.sendStaticResource();

                socket.close();
                shutdown = request.getUri().equals(SHUTDOWN_COMMOND);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        System.out.println("server shutdown");
    }
}