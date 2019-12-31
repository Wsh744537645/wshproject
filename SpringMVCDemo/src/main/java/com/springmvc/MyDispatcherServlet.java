package com.springmvc;

import com.springmvc.base.MyController;
import com.springmvc.base.MyRequestMapping;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * @author jmfen
 * date 2019-12-30
 */
public class MyDispatcherServlet extends HttpServlet {
    //模拟IOC容器，保存Controller实例对象
    private Map<String, Object> iocContainer = new HashMap<>();
    //保存handler映射
    private Map<String, Method> handlerMapping = new HashMap<>();
    //自定义视图解析器
    private MyViewResolver myViewResolver;

    @Override
    public void init(ServletConfig config) throws ServletException {
        //扫描 Controller，创建实例对象，并存入 iocContainer
        scanController(config);
        //初始化 handler 映射
        initHandlerMapping();
        //加载视图解析器
        loadViewResolver(config);
    }

    /**
     * 获取包下的所有类名
     * @param packageName
     * @return
     */
    public List<String> getClassNames(String packageName){
        List<String> classNameList = new ArrayList<String>();
        String packagePath = packageName.replace(".", "/");
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(packagePath);
        if(url != null){
            File file = new File(url.getPath());
            File[] childFiles = file.listFiles();
            for(File childFile : childFiles){
                String className = packageName+"."+childFile.getName().replace(".class", "");
                classNameList.add(className);
            }
        }
        return classNameList;
    }

    /**
     * 扫描Controller
     * @param config
     */
    public void scanController(ServletConfig config){
        SAXReader reader = new SAXReader();
        try {
            //解析springmvc.xml
            String path = config.getServletContext().getRealPath("") + "\\WEB-INF\\classes\\" + config.getInitParameter("contextConfigLocation");
            Document document = reader.read(path);
            Element root = document.getRootElement();
            Iterator iterator = root.elementIterator();
            while (iterator.hasNext()){
                Element ele = (Element)iterator.next();
                if(ele.getName().equals("component-scan")){
                    String packageName = ele.attributeValue("base-package");
                    //获取base-package 包下的所有类名
                    List<String> list = getClassNames(packageName);
                    for(String str : list){
                        Class clazz = Class.forName(str);
                        //判断是否有 MyController 注解
                        if(clazz.isAnnotationPresent(MyController.class)){
                            //获取 Controller 中 MyRequestMapping 注解的 value
                            MyRequestMapping anno = (MyRequestMapping)clazz.getAnnotation(MyRequestMapping.class);
                            String value = anno.value().substring(1);
                            iocContainer.put(value, clazz.newInstance());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化handler映射
     */
    public void initHandlerMapping(){
        iocContainer.keySet().forEach(key -> {
            Class clazz = iocContainer.get(key).getClass();
            Method[] methods = clazz.getMethods();
            for(Method method : methods){
                if(method.isAnnotationPresent(MyRequestMapping.class)){
                    MyRequestMapping anno = method.getAnnotation(MyRequestMapping.class);
                    String value = anno.value().substring(1);
                    handlerMapping.put(value, method);
                }
            }
        });
    }

    /**
     * 加在自定义视图解析器
     * @param config
     */
    public void loadViewResolver(ServletConfig config){
        SAXReader reader = new SAXReader();
        try {
            //解析springmvc.xml
            String path = config.getServletContext().getRealPath("") + "\\WEB-INF\\classes\\"+config.getInitParameter("contextConfigLocation");
            Document document = reader.read(path);
            Element root = document.getRootElement();
            Iterator iterator = root.elementIterator();
            while (iterator.hasNext()){
                Element ele = (Element)iterator.next();
                if(ele.getName().equals("bean")){
                    String className = ele.attributeValue("class");
                    Class clazz = Class.forName(className);
                    Object obj = clazz.newInstance();
                    //获取 setter 方法
                    Method prefixMethod = clazz.getMethod("setPrefix", String.class);
                    Method suffixMethod = clazz.getMethod("setSuffix", String.class);
                    Iterator beanIter = ele.elementIterator();
                    Map<String, String> propertyMap = new HashMap<>();
                    while (beanIter.hasNext()){
                        Element beanEle = (Element) beanIter.next();
                        String name = beanEle.attributeValue("name");
                        String value = beanEle.attributeValue("value");
                        propertyMap.put(name, value);
                    }
                    for(String name : propertyMap.keySet()) {
                        if(name.equals("prefix")){
                            prefixMethod.invoke(obj, propertyMap.get(name));
                        }
                        if(name.equals("auffix")){
                            suffixMethod.invoke(obj, propertyMap.get(name));
                        }
                    }
                    myViewResolver = (MyViewResolver) obj;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求
        String handlerUri = req.getRequestURI().split("/")[2];
        //获取Controller实例
        Object obj = iocContainer.get(handlerUri);
        String methodUri = req.getRequestURI().split("/")[3];
        Method method = handlerMapping.get(methodUri);
        try{
            String value = (String) method.invoke(obj);
            String result = myViewResolver.jspMapping(value);
            req.getRequestDispatcher(result).forward(req, resp);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}