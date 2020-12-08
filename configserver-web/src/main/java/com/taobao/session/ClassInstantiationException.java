package com.taobao.session;

/**
 * @author hengyi
 */
public class ClassInstantiationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ClassInstantiationException() {
        super();
    }

    public ClassInstantiationException(Class<?> clazz) {
        super("ʵ��������" + clazz.getName() + "ʱʧ��");
    }

    public ClassInstantiationException(Throwable cause) {
        super(cause);
    }

    public ClassInstantiationException(Class<?> clazz, Throwable cause) {
        super("ʵ��������" + clazz.getName() + "ʱʧ��", cause);
    }

}
