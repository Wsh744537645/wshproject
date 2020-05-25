package com.stephendemo.web_server_demo.demo1;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author jmfen
 * date 2020-05-22
 */
public class RequestDemo {
    private InputStream inputStream;
    private String uri;

    public RequestDemo(InputStream inputStream){
        this.inputStream = inputStream;
    }

    public void parse(){
        StringBuffer requst = new StringBuffer(2048);
        int i;
        byte[] buffer = new byte[2048];
        try {
            i = inputStream.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            i = -1;
        }
        for(int j = 0; j < i; j++){
            requst.append((char) buffer[j]);
        }
        System.out.println("request parse data: " + requst.toString());
        uri = parseUri(requst.toString());
    }

    public String parseUri(String requestString){
        int index1, index2;
        index1 = requestString.indexOf(' ');
        if(index1 != -1){
            index2 = requestString.indexOf(' ', index1 + 1);
            if(index2 > index1){
                return requestString.substring(index1 + 1, index2);
            }
        }
        return null;
    }

    public String getUri(){
        return uri;
    }
}