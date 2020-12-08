package com.taobao.session;

/**
 * @author hengyi
 */
public class ModifyReadOnlyAttributeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ModifyReadOnlyAttributeException() {
        super();
    }

    public ModifyReadOnlyAttributeException(String message) {
        super(message);
    }

    public ModifyReadOnlyAttributeException(Throwable cause) {
        super(cause);
    }

    public ModifyReadOnlyAttributeException(String message, Throwable cause) {
        super(message, cause);
    }

}
