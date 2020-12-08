package com.elsa.configserver.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class MAPIHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private final byte[] body; // 报文

	public static final byte[] input2byte(InputStream inStream) throws IOException {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		byte[] buff = new byte[100];
		int rc = 0;
		while ((rc = inStream.read(buff, 0, 100)) > 0) {
			swapStream.write(buff, 0, rc);
		}
		byte[] in2b = swapStream.toByteArray();
		return in2b;
	}

	public MAPIHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		body = input2byte(request.getInputStream());
		String str = new String(body);
		MeitunHessianServiceExporter.logger.info("^^^^^^^^^^^^^^^^^^^^^^^^^" + str);
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		final ByteArrayInputStream bais = new ByteArrayInputStream(body);
		return new ServletInputStream() {
			public int read() throws IOException {
				return bais.read();
			}
		};
	}

}