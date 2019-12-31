package com.springmvc;

/**
 * 自定义视图解析器
 * @author jmfen
 * date 2019-12-30
 */
public class MyViewResolver {
    private String prefix;
    private String suffix;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String jspMapping(String value){
        return prefix + value + suffix;
    }
}