package com.stephendemo.web_server_demo;

import java.io.PrintWriter;

/**
 * @author jmfen
 * date 2020-05-22
 */
public class PrintWriteDemo {

    public static void main(String[] args) {
        PrintWriter writer = new PrintWriter(System.out, true);
        writer.println("come on");
        writer.print("hhh");
        writer.flush();
    }
}