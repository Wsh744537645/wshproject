package com.stephendemo;

/**
 * @author jmfen
 * date 2019-10-24
 */
public class YoungGcDemo {
    private static void alloc() {
        byte[] bytes = new byte[2];
        bytes[0] = 1;
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        System.out.println(start);
        for (int i = 0; i < 1000000000; i++) {
            alloc();
        }
        System.out.println(System.currentTimeMillis() - start);
    }
}