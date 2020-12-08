package com.taobao.session;

import javax.servlet.ServletException;

/**
 * @author hengyi
 */
public class MissingFilterInitParameterException extends ServletException {

    private static final long serialVersionUID = 1L;

    public MissingFilterInitParameterException() {
        super();
    }

    public MissingFilterInitParameterException(String message) {
        super(message);
    }

    public MissingFilterInitParameterException(Throwable rootCause) {
        super(rootCause);
    }

    public MissingFilterInitParameterException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

}
