package com.stephendemo.web_server_demo.demo1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author jmfen
 * date 2020-05-22
 */
public class ResponseDemo {
    private static final int BUFFER_SIZE = 1024;
    RequestDemo request;
    OutputStream outputStream;
    public ResponseDemo(OutputStream outputStream){
        this.outputStream = outputStream;
    }

    public void setRequest(RequestDemo request){
        this.request = request;
    }

    public void sendStaticResource() throws IOException{
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fis = null;
        try {
            File file = new File(HttpServerDemo.WEB_ROOT, request.getUri());
            if(file.exists()){
                fis = new FileInputStream(file);
                int ch = fis.read(bytes, 0, BUFFER_SIZE);
                while (ch != -1){
                    outputStream.write(bytes, 0, ch);
                    ch = fis.read(bytes, 0, BUFFER_SIZE);
                }
            }else {
                String errorMessage = "HTTP/1.1 404 File NOT FOUND \r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: 23\r\n" +
                        "\r\n" +
                        "<h1>File Not Found</h1>";
                outputStream.write(errorMessage.getBytes());
            }
        }catch (Exception e){
            System.out.println(e.toString());
        }finally {
            if(fis != null){
                fis.close();
            }
        }
    }
}