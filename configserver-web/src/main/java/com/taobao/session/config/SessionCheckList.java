package com.taobao.session.config;

import java.util.HashSet;
import java.util.Set;

/**   
 * @author xiaoxie   
 * @create time：2010-1-21 下午02:23:29   
 * @description  
 */
public class SessionCheckList {
	/**
	 * 禁止写入的session列表
	 */
	private static Set<String> forbidenWrittenSessionKeys = new HashSet<String> ();
	static {
		forbidenWrittenSessionKeys.add("userID");
		forbidenWrittenSessionKeys.add("userIDNum");
		forbidenWrittenSessionKeys.add("_nk_");
		forbidenWrittenSessionKeys.add("login");
	}
	public static boolean isForbidenWritten(String key) {
		return forbidenWrittenSessionKeys.contains(key);
	}
	public static void main(String[] args) {
		boolean wr = SessionCheckList.isForbidenWritten("_nk_");
		System.out.println(wr);
	}
}
