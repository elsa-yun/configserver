package com.elsa.configserver.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.elsa.configserver.client.HttpClientConstants;

public class HttpClientUtil {

	private static final Log log = LogFactory.getLog(HttpClientUtil.class);

	private static final String SERVER_HOST = "http://config.elsa.com";

	private static final int PORT = 0;

	private static final int PollingIntervalTime = 5;

	private static final int SC_OK = 200;

	private static final int SC_NOT_MODIFIED = 304;

	private static final int SC_NOT_FOUND = 404;

	private static final int SC_SERVICE_UNAVAILABLE = 503;

	private final AtomicInteger domainNamePos = new AtomicInteger(0);

	private List<String> serverHosts;

	private HttpClient httpClient = null;

	public HttpClientUtil() {
		initHttpClient();
	}

	String getUriString(String appName) {
		return getUriString(appName, "");
	}

	private String getHost() {
		if (null != this.serverHosts && !this.serverHosts.isEmpty()) {
			if (this.serverHosts.size() == 1) {
				return this.serverHosts.get(0);
			} else {
				randomDomainNamePos();
				return this.serverHosts.get(this.domainNamePos.get());
			}
		}
		return SERVER_HOST;
	}

	private void randomDomainNamePos() {
		Random rand = new Random();
		List<String> domainList = this.serverHosts;
		if (null != this.serverHosts && !domainList.isEmpty()) {
			this.domainNamePos.set(rand.nextInt(domainList.size()));
		}
	}

	String getUriString(String appName, String configName) {
		StringBuilder uriBuilder = new StringBuilder();
		uriBuilder.append(getHost());
		uriBuilder.append(HttpClientConstants.HTTP_URI_FILE);
		uriBuilder.append("?");
		uriBuilder.append(HttpClientConstants.APP_NAME).append("=").append(appName);
		if (null != configName && !"".equals(configName)) {
			uriBuilder.append("&");
			uriBuilder.append(HttpClientConstants.FILE_NAME).append("=").append(configName);
		}
		uriBuilder.append("&");
		uriBuilder.append("current_time").append("=").append(System.currentTimeMillis());
		return uriBuilder.toString();
	}

	protected void initHttpClient() {
		HostConfiguration hostConfiguration = new HostConfiguration();
		// hostConfiguration.setHost(SERVER_HOST, PORT);

		SimpleHttpConnectionManager connectionManager = new SimpleHttpConnectionManager();// TODO old is MultiThreadedHttpConnectionManager
		connectionManager.closeIdleConnections(PollingIntervalTime * 4000);
		HttpConnectionManagerParams params = new HttpConnectionManagerParams();
		params.setStaleCheckingEnabled(true);
		params.setMaxConnectionsPerHost(hostConfiguration, 1);
		params.setMaxTotalConnections(20);
		params.setConnectionTimeout(2000);
		params.setSoTimeout(60 * 1000);

		connectionManager.setParams(params);
		httpClient = new HttpClient(connectionManager);
		httpClient.setHostConfiguration(hostConfiguration);
	}

	private void configureHttpMethod(long onceTimeOut, HttpMethod httpMethod) {
		HttpMethodParams params = new HttpMethodParams();
		params.setSoTimeout((int) onceTimeOut);
		httpMethod.setParams(params);
	}

	private final static boolean isWindows() {
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().startsWith("windows")) {
			return true;
		}
		return false;
	}

	private String getConfigureInfomation(String appName, long timeout) {
		long waitTime = 0;
		String uri = getUriString(appName);

		if (log.isInfoEnabled()) {
			log.info(uri);
		}

		int retryTimes = 5;
		log.info("设定的获取配置数据的重试次数为：" + retryTimes);
		int tryCount = 0;

		while (0 == timeout || timeout > waitTime) {
			tryCount++;
			if (tryCount > retryTimes + 1) {
				log.warn("已经到达了设定的重试次数");
				break;
			}
			log.info("获取配置数据，第" + tryCount + "次尝试, waitTime:" + waitTime);

			long onceTimeOut = getOnceTimeOut(waitTime, timeout);
			waitTime += onceTimeOut;

			HttpMethod httpMethod = new GetMethod(uri);
			configureHttpMethod(onceTimeOut, httpMethod);

			try {
				int httpStatus = httpClient.executeMethod(httpMethod);
				switch (httpStatus) {
				case SC_OK: {
					String result = getSuccess(httpMethod);
					return result;
				}

				case SC_NOT_MODIFIED: {
					return null;
				}

				case SC_NOT_FOUND: {
					log.warn("没有找到DataID为:" + appName + "对应的配置信息");
					return null;
				}

				case SC_SERVICE_UNAVAILABLE: {
				}

					break;

				default: {
					log.warn("HTTP State: " + httpStatus + ":" + httpClient.getState());
				}
				}
			} catch (HttpException e) {
				if (!isWindows()) {
					log.error("获取配置信息Http异常", e);
				} else {
					log.info("======================configserver 获取配置信息Http异常=========================================");
				}
			} catch (IOException e) {
				if (!isWindows()) {
					log.error("获取配置信息IO异常", e);
				} else {
					log.info("==================================configserver 获取配置信息IO异常===============================");
				}
			} catch (Exception e) {
				if (!isWindows()) {
					log.error("未知异常", e);
				} else {
					log.info("==================================configserver 未知异常=======================================");
				}
			} finally {
				httpMethod.releaseConnection();
			}
		}
		// throw new RuntimeException("获取ConfigureInfomation超时, AppName" +
		// appName + ",超时时间为：" + timeout);
		return "";
	}

	long getOnceTimeOut(long waitTime, long timeout) {
		long onceTimeOut = 2000;
		long remainTime = timeout - waitTime;
		if (onceTimeOut > remainTime) {
			onceTimeOut = remainTime;
		}
		return onceTimeOut;
	}

	public String getSuccess(HttpMethod httpMethod) {
		StringBuilder contentBuilder = new StringBuilder();
		String content = null;
		try {
			content = httpMethod.getResponseBodyAsString();
		} catch (Exception e) {
			log.error("获取配置信息失败", e);
		}
		if (null == content) {
			return null;
		}
		contentBuilder.append(content);
		return contentBuilder.toString();
	}

	public String getSuccess(HttpMethod httpMethod, boolean isStream) {
		StringBuilder contentBuilder = new StringBuilder();
		String content = null;
		try {
			InputStream is = null;
			InputStreamReader isr = null;
			BufferedReader br = null;
			try {
				is = httpMethod.getResponseBodyAsStream();
				isr = new InputStreamReader(is, ((HttpMethodBase) httpMethod).getResponseCharSet());
				br = new BufferedReader(isr);
				char[] buffer = new char[2048];
				int readlen = -1;
				while ((readlen = br.read(buffer, 0, 4096)) != -1) {
					contentBuilder.append(buffer, 0, readlen);
				}
			} catch (Exception e) {
				log.error("处理异常", e);
			} finally {
				try {
					br.close();
				} catch (Exception e1) {
					// ignore
				}
				try {
					isr.close();
				} catch (Exception e1) {
					// ignore
				}
				try {
					is.close();
				} catch (Exception e1) {
					// ignore
				}
			}

		} catch (Exception e) {
			log.error("获取配置信息失败", e);
		}
		if (null == content) {
			return null;
		}
		return contentBuilder.toString();
	}

	public Map<String, String> getConfigContent(String appName) {
		Map<String, String> map = new HashMap<String, String>();
		String str = this.getConfigureInfomation(appName, 5000);
		if (null != str && !"".equals(str.trim())) {
			String[] contents = str.split(HttpClientConstants.FILE_VALUE_SPLIT_CHARS);
			int length = contents.length;
			for (int i = 0; i < length; i++) {
				String content = contents[i];
				String[] oneFileContent = content.split(HttpClientConstants.FILE_NAME_SPLIT_CHARS);
				if (oneFileContent.length >= 2) {
					String fileName = oneFileContent[0];
					String fileContent = oneFileContent[1];
					map.put(fileName, fileContent);
				}

			}
		}
		return map;
	}

	public List<String> getServerHosts() {
		return serverHosts;
	}

	public void setServerHosts(List<String> serverHosts) {
		this.serverHosts = serverHosts;
	}
	
/*	public static void main(String args[]){
		HttpClientUtil client=new HttpClientUtil();
		System.out.println(client.getUriString("order-web"));
	}*/

}
