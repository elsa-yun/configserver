package com.elsa.configserver.client;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConfigServerListener implements ServletContextListener {

	private static final Log logger = LogFactory.getLog(ConfigServerListener.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext servletContext = sce.getServletContext();
		String app_name = servletContext.getInitParameter("appName");
		if (StringUtils.isNotBlank(app_name)) {
			ConfigServerNewClient client = new ConfigServerNewClient();
			client.setAppName(app_name.trim());
			client.init();
			logger.info("***************************** app_name:" + app_name + " *****************************");
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		//MultiThreadedHttpConnectionManager.shutdownAll();
		logger.info("***************************** ConfigServer Listener contextDestroyed *****************************");
	}

}
