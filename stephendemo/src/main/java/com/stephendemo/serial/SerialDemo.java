package com.stephendemo.serial;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.*;

/**
 * @author jmfen
 * date 2020-04-20
 */

@Data
public class SerialDemo implements Serializable {
    private String password;

    private void writeObject(ObjectOutputStream outputStream){
        try {
            ObjectOutputStream.PutField putField = outputStream.putFields();
            String tmpPassword = StringUtils.reverse(password);
            putField.put("password", tmpPassword);
            System.out.println("writeObject: tmpPassword=" + tmpPassword + "  password=" + password);
            outputStream.writeFields();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readObject(ObjectInputStream inputStream){
        try {
            ObjectInputStream.GetField getField = inputStream.readFields();
            Object object = getField.get("password", "");
            password = StringUtils.reverse(object.toString());
            System.out.println("readObject: object=" + object + "  password=" + password);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        SerialDemo demo = new SerialDemo();
        demo.setPassword("123456");
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("test.aa"));
            outputStream.writeObject(demo);
            outputStream.close();

            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("test.aa"));
            SerialDemo demo1 = (SerialDemo) inputStream.readObject();
            inputStream.close();

            System.out.println("result: ");
            System.out.println(demo);
            System.out.println(demo1);
            System.out.println(demo == demo1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}