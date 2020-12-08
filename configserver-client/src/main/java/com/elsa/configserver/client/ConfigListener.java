package com.elsa.configserver.client;
//package com.elsa.configserver.client;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.servlet.ServletContext;
//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletContextListener;
//
//import com.elsa.configserver.client.ConfigServerClient;
//
//public class ConfigListener implements ServletContextListener {
//
//	@Override
//	public void contextInitialized(ServletContextEvent sce) {
//		ConfigServerClient client = new ConfigServerClient();
//		ServletContext servletContext = sce.getServletContext();
//		String appName = servletContext.getInitParameter("appName");
//		client.setAppName(appName);
//		String configServerHosts = servletContext.getInitParameter("configServerHosts");
//		List<String> serverHosts = new ArrayList<String>();
//		if (null != configServerHosts && !"".equals(configServerHosts)) {
//			if (configServerHosts.contains(",")) {
//				String[] arr = configServerHosts.split(",");
//				int length = arr.length;
//				for (int i = 0; i < length; i++) {
//					serverHosts.add(arr[i]);
//				}
//			} else {
//				serverHosts.add(configServerHosts);
//			}
//		}
//		client.setServerHosts(serverHosts);
//		client.init();
//	}
//
//	@Override
//	public void contextDestroyed(ServletContextEvent sce) {
//
//	}
//
//}
