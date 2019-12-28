package Iocdemo;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author jmfen
 * date 2019-12-27
 */
public class ClassPathXmlApplicationContext implements ApplicationContext {
    private Map<String, Object> ioc = new HashMap<>();

    public ClassPathXmlApplicationContext(String path){
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read("./src/main/resources/" + path);
            Element root = document.getRootElement();
            Iterator<Element> iterator = root.elementIterator();
            while (iterator.hasNext()){
                Element element = iterator.next();
                String id = element.attributeValue("id");
                String className = element.attributeValue("class");
                Class clazz = Class.forName(className);
                //无参构造
                Constructor constructor = clazz.getConstructor();
                Object object = constructor.newInstance();
                Iterator<Element> beanIter = element.elementIterator();
                while (beanIter.hasNext()){
                    Element property = beanIter.next();
                    String name = property.attributeValue("name");
                    String valueStr = property.attributeValue("value");
                    String methodName = "set" + name.substring(0, 1).toUpperCase() +  name.substring(1);
                    Field field = clazz.getDeclaredField(name);
                    Method method = clazz.getDeclaredMethod(methodName, field.getType());

                    Object value = null;
                    String fieldTypeName = field.getType().getName();
                    if(fieldTypeName.equals("java.lang.Float")){
                        value = Long.parseLong(valueStr);
                    }else if(fieldTypeName.equals("java.lang.String")){
                        value = valueStr;
                    }else if(fieldTypeName.equals("java.lang.Integer")){
                        value = Integer.parseInt(valueStr);
                    }
                    method.invoke(object, value);
                }
                ioc.put(id, object);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getBean(String id) {
        return ioc.get(id);
    }
}