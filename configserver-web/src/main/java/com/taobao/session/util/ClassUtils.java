package com.taobao.session.util;

import org.apache.commons.lang.IllegalClassException;

import com.taobao.session.ClassInstantiationException;
import com.taobao.session.ClassNotFoundException;

/**
 * @author hengyi
 */
public class ClassUtils {

    @SuppressWarnings("unchecked")
    public static <T> Class<? extends T> findClass(String className, Class<T> superClass) {
        Class<?> result = null;
        try {
            result = Class.forName(className);
        } catch (Exception e) {
            throw new ClassNotFoundException(className, e);
        }
        if (superClass.isAssignableFrom(result)) {
            return (Class<? extends T>) result;
        } else {
            throw new IllegalClassException(superClass, result);
        }
    }

    public static <T> T newInstance(Class<? extends T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new ClassInstantiationException(clazz, e);
        }
    }

}
