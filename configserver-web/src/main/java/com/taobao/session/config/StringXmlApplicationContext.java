package com.taobao.session.config;

import java.io.IOException;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.core.io.ByteArrayResource;

public class StringXmlApplicationContext extends AbstractXmlApplicationContext {

    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * 必须是UTF-8编码的XML，即XML的第一行必须是<?xml version="1.0" encoding="UTF-8"?>
     */
    private String xml;

    public StringXmlApplicationContext(String xml) {
        this.xml = xml;
        refresh();
    }

    @Override
    protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) throws BeansException, IOException {
        byte[] bytes = xml.getBytes(DEFAULT_CHARSET);
        ByteArrayResource resource = new ByteArrayResource(bytes);
        reader.loadBeanDefinitions(resource);
    }

}
