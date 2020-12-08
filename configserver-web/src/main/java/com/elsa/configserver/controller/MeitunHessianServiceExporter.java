package com.elsa.configserver.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.remoting.caucho.HessianServiceExporter;

public class MeitunHessianServiceExporter extends HessianServiceExporter {

	public static final Log logger = LogFactory.getLog(MeitunHessianServiceExporter.class);

	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.handleRequest(request, response);
		//		String sid = null;
//		String src = null;
//		StringBuilder s = new StringBuilder();
//		try {
//			sid = request.getHeader("sid");
//			String caller = request.getHeader("caller");
//			String call = request.getHeader("call");
//			src = request.getRemoteHost();
//			logger.info("sid======================================================>" + sid);
//			logger.info("caller======================================================>" + caller);
//			logger.info("call======================================================>" + call);
//			logger.info("src======================================================>" + src);
//
//			s.append("sid======================================================>" + sid);
//			s.append("caller======================================================>" + caller);
//			s.append("call======================================================>" + call);
//			s.append("src======================================================>" + src);
//
//			Enumeration headerNames = request.getHeaderNames();
//			while (headerNames.hasMoreElements()) {
//				Object nextElement = headerNames.nextElement();
//				logger.info(nextElement.toString() + "======================================================>" + request.getHeader(nextElement.toString()));
//				s.append(nextElement.toString() + "======================================================>" + request.getHeader(nextElement.toString()));
//			}
//			Enumeration parameterNames = request.getParameterNames();
//
//			logger.info("getParameterNames======================================================>");
//			s.append("getParameterNames======================================================>");
//			while (parameterNames.hasMoreElements()) {
//				Object element = parameterNames.nextElement();
//				logger.info(element.toString() + "======================================================>" + request.getParameter(element.toString()));
//				s.append(element.toString() + "======================================================>" + request.getParameter(element.toString()));
//			}
//
//			logger.info("getAttributeNames======================================================>");
//			s.append("getAttributeNames======================================================>");
//			Enumeration attributeNames = request.getAttributeNames();
//			while (attributeNames.hasMoreElements()) {
//				Object nextElement = attributeNames.nextElement();
//				logger.info(nextElement.toString() + "======================================================>" + request.getAttribute(nextElement.toString()));
//				s.append(nextElement.toString() + "======================================================>" + request.getAttribute(nextElement.toString()));
//			}
//
//			Map map = request.getParameterMap();
//			logger.info("@@@@@@@@@@@@@@@@@@@" + map.size());
//
//			StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
//			for (StackTraceElement stackTraceElement : stackTrace) {
//				String methodName = stackTraceElement.getMethodName();
//				Class<?> class1 = stackTraceElement.getClass();
//				int lineNumber = stackTraceElement.getLineNumber();
//				logger.info("methodName@@@@@@@@@@@@@@@@@@@" + stackTraceElement.toString());
//				logger.info("methodName@@@@@@@@@@@@@@@@@@@" + methodName);
//				logger.info("class1@@@@@@@@@@@@@@@@@@@" + class1);
//				logger.info("lineNumber@@@@@@@@@@@@@@@@@@@" + lineNumber);
//			}
//
//			Set keSet = map.entrySet();
//			for (Iterator itr = keSet.iterator(); itr.hasNext();) {
//				Map.Entry me = (Map.Entry) itr.next();
//				Object ok = me.getKey();
//				Object ov = me.getValue();
//				String[] value = new String[1];
//				if (ov instanceof String[]) {
//					value = (String[]) ov;
//				} else {
//					value[0] = ov.toString();
//				}
//
//				for (int k = 0; k < value.length; k++) {
//					logger.info(ok + "@@@@@@@@@@@@@@@@@@@" + value[k]);
//				}
//			}
//
//		} catch (Exception e) {
//
//		}
//
//		InputStream is = request.getInputStream();
//
//		DataInputStream input = new DataInputStream(is);
//		String str = input.readUTF();
//
//		this.splitString(str, "id=");
//
//		// 获取post参数
//		StringBuffer sb = new StringBuffer();
//		InputStreamReader isr = new InputStreamReader(is);
//		BufferedReader br = new BufferedReader(isr);
//		String ss = "";
//		while ((ss = br.readLine()) != null) {
//			sb.append(ss);
//		}
//		String string = sb.toString();
//		logger.info("@*********************************************************************"+string);
//		// 防止用get传递参数
//		if (str.equals("")) {
//			if (request.getQueryString() != null) {
//				str = request.getRequestURL() + "?" + request.getQueryString();
//			} else {
//				str = request.getRequestURL().toString();
//			}
//		}
//
//		super.handleRequest(request, response);
//		
//		Map map = request.getParameterMap();
//		Set keSet = map.entrySet();
//		for (Iterator itr = keSet.iterator(); itr.hasNext();) {
//			Map.Entry me = (Map.Entry) itr.next();
//			Object ok = me.getKey();
//			Object ov = me.getValue();
//			String[] value = new String[1];
//			if (ov instanceof String[]) {
//				value = (String[]) ov;
//			} else {
//				value[0] = ov.toString();
//			}
//
//			for (int k = 0; k < value.length; k++) {
//				logger.info(ok + "@@@@@@@@@@@@@@@@@@@" + value[k]);
//			}
//		}
//
	}

	public String splitString(String str, String temp) {
		String result = null;
		if (str.indexOf(temp) != -1) {
			if (str.substring(str.indexOf(temp)).indexOf("&") != -1) {
				result = str.substring(str.indexOf(temp)).substring(str.substring(str.indexOf(temp)).indexOf("=") + 1, str.substring(str.indexOf(temp)).indexOf("&"));

			} else {
				result = str.substring(str.indexOf(temp)).substring(str.substring(str.indexOf(temp)).indexOf("=") + 1);

			}
		}
		return result;

	}

	public static boolean writeFile(String filePath, String content) {
		FileOutputStream outputStream = null;
		FileLock fileLock = null;
		FileChannel channel = null;
		try {

			String folderPath = filePath.substring(0, filePath.lastIndexOf("/"));
			File file = new File(folderPath);
			mkDir(file);
			File configFile = new File(filePath);
			outputStream = new FileOutputStream(configFile);
			channel = outputStream.getChannel();
			fileLock = channel.lock();
			outputStream.write(content.getBytes());
			outputStream.flush();
		} catch (Exception e) {
			// log.error(e.getMessage());
		} finally {
			if (fileLock != null) {
				try {
					fileLock.release();
					fileLock = null;
				} catch (IOException e) {
					// log.error(e.getMessage(), e);
				}
			}
			if (channel != null) {
				try {
					channel.close();
					channel = null;
				} catch (IOException e) {
					// log.error(e.getMessage(), e);
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					// log.error(e.getMessage(), e);
				}
			}
		}
		// writeFileTrimLine(filePath, filePath);
		return true;
	}

	public static void mkDir(File file) {
		if (file.getParentFile().exists()) {
			file.mkdir();
		} else {
			mkDir(file.getParentFile());
			file.mkdir();
		}
	}

}
