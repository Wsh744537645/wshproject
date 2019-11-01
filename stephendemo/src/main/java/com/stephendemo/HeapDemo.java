package com.stephendemo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jmfen
 * date 2019-10-22
 */
public class HeapDemo {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        int count = 0;
        while (true){
            list.add(new String(count + "").intern());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}