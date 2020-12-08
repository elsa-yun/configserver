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
        super("实例化类型" + clazz.getName() + "时失败");
    }

    public ClassInstantiationException(Throwable cause) {
        super(cause);
    }

    public ClassInstantiationException(Class<?> clazz, Throwable cause) {
        super("实例化类型" + clazz.getName() + "时失败", cause);
    }

}
