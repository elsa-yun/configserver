package com.taobao.session;

/**
 * 
 * @author fenghao 
 * 
 *
 */
public class TairReadFailureException extends RuntimeException {

	
	 private static final long serialVersionUID = 1L;

	    public TairReadFailureException() {
	        super();
	    }

	    public TairReadFailureException(String message, Throwable cause) {
	        super(message, cause);
	    }

	    public TairReadFailureException(String message) {
	        super(message);
	    }

	    public TairReadFailureException(Throwable cause) {
	        super(cause);
	    }
}
