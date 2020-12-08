package com.elsa.configserver.controller;

import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;

public class Md5Util {

	public final static String md5pwd(String password) {
		MessageDigestPasswordEncoder md5 = new MessageDigestPasswordEncoder("MD5");
		md5.setEncodeHashAsBase64(false);
		String passwordToMD5OrSHA = md5.encodePassword(password, "lhs");
		return passwordToMD5OrSHA;
	}
}
