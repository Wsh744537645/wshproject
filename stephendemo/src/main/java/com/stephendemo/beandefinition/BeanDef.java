package com.stephendemo.beandefinition;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;
import java.util.stream.Stream;

/**
 * @author jmfen
 * date 2020-03-16
 */

@Slf4j
public class BeanDef {
    private ConfigurableApplicationContext cac;

    public BeanDef(ConfigurableApplicationContext cac){
        this.cac = cac;
    }

    public void showBeanDef() {
        showBeanDefNames();
        showBeanDefObject();
        showBeanAnnotation();
    }

    public void showBeanDefNames(){
        int bdCount = cac.getBeanDefinitionCount();
        String[] bdNames = cac.getBeanDefinitionNames();
        log.info("bdCount = {}", bdCount);
        log.info("bdNames =>");
        Stream.of(bdNames).forEach(bdName -> log.info(bdName));
    }

    public void showBeanDefObject(){
        ConfigurableListableBeanFactory clbf = cac.getBeanFactory();
        log.info("bdObjects =>");
        BeanDefinition stephendemoApplicationBD = clbf.getBeanDefinition("stephendemoApplication");
        log.info("stephendemoApplicationBD = {}, -> {}", stephendemoApplicationBD, stephendemoApplicationBD.getClass());
        BeanDefinition bossBD = clbf.getBeanDefinition("boss");
        log.info("bossBD = {}, -> {}", bossBD, bossBD.getClass());

        BeanDefinition companyBD = clbf.getBeanDefinition("company");
        log.info("companyBD = {}, -> {}", companyBD, companyBD.getClass());
        BeanDefinition littleMingBD = clbf.getBeanDefinition("littleMing");
        log.info("littleMingBD = {}, -> {}", littleMingBD, littleMingBD.getClass());
        BeanDefinition littleHuaBD = clbf.getBeanDefinition("littleHua");
        log.info("littleHuaBD = {}, -> {}", littleHuaBD, littleHuaBD.getClass());
    }

    public void showBeanAnnotation() {
        ConfigurableListableBeanFactory clbf = cac.getBeanFactory();
        log.info("bdAnnotation =>");

        AnnotatedBeanDefinition bossBD = (AnnotatedBeanDefinition) clbf.getBeanDefinition("boss");
        Map<String, Object> bossAnno = bossBD.getMetadata().getAnnotationAttributes("org.springframework.stereotype.Component");
        log.info("bossAnno = {}", bossAnno);

        AnnotatedBeanDefinition companyBD = (AnnotatedBeanDefinition)clbf.getBeanDefinition("company");
        Map<String, Object> companyAnno = companyBD.getMetadata().getAnnotationAttributes("org.springframework.context.annotation.Configuration");
        log.info("companyAnno = {}", companyAnno);
        AnnotatedBeanDefinition littleMingBD = (AnnotatedBeanDefinition)clbf.getBeanDefinition("littleMing");
        Map<String, Object> littleMingAnno = littleMingBD.getFactoryMethodMetadata().getAnnotationAttributes("org.springframework.context.annotation.Bean");
        log.info("littleMingAnno = {}", littleMingAnno);
        AnnotatedBeanDefinition littleHuaBD = (AnnotatedBeanDefinition)clbf.getBeanDefinition("littleHua");
        Map<String, Object> littleHuaBDAnno = littleHuaBD.getFactoryMethodMetadata().getAnnotationAttributes("org.springframework.context.annotation.Bean");
        log.info("littleQiangAnno = {}", littleHuaBDAnno);
    }
}