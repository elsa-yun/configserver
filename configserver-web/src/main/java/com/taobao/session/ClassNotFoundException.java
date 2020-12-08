package com.taobao.session;

/**
 * @author hengyi
 */
public class ClassNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ClassNotFoundException() {
        super();
    }

    public ClassNotFoundException(String className, Throwable cause) {
        super("δ�ҵ�����" + className, cause);
    }

    public ClassNotFoundException(String className) {
        super("δ�ҵ�����" + className);
    }

    public ClassNotFoundException(Throwable cause) {
        super(cause);
    }

}
