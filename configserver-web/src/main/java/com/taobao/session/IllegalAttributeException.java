package com.taobao.session;
/**   
 * @author xiaoxie   
 * @create time��2010-1-21 ����02:00:51   
 * @description  
 */
public class IllegalAttributeException extends RuntimeException {


	private static final long serialVersionUID = 1L;

	public IllegalAttributeException() {
        super();
    }

    public IllegalAttributeException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalAttributeException(String message) {
        super(message);
    }

    public IllegalAttributeException(Throwable cause) {
        super(cause);
    }
}
