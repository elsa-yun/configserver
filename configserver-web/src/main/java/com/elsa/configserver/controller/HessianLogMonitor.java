package com.elsa.configserver.controller;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.PriorityOrdered;

import com.elsa.redis.util.JedisCacheUtil;

public class HessianLogMonitor implements DisposableBean, BeanFactoryPostProcessor, PriorityOrdered {

	private static final Log logger = LogFactory.getLog(HessianLogMonitor.class);

	private JedisCacheUtil jedisCacheUtil;

	private ScheduledExecutorService scheduledExecutorService;

	private final static int SCHEDULE_DELAY = 1;

	public void watcher() {
		if (logger.isInfoEnabled()) {
			logger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ write hessian log to redis scheduler start @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		}
		scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
			public void run() {
				Map<String, String> get_interface_method_map = MeitunHessianProxyFactoryBean.get_interface_method_map();
				for (Map.Entry<String, String> ent : get_interface_method_map.entrySet()) {
					logger.info(ent.getValue());
					jedisCacheUtil.setCacheString("hessian." + ent.getKey(), ent.getValue());
				}
			}
		}, 1, SCHEDULE_DELAY, TimeUnit.SECONDS);
	}

	@Override
	public int getOrder() {
		return LOWEST_PRECEDENCE - 11;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		try {
			jedisCacheUtil = beanFactory.getBean(com.elsa.redis.util.JedisCacheUtil.class);
			if (null != jedisCacheUtil) {
				scheduledExecutorService = Executors.newScheduledThreadPool(1);
				this.watcher();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void destroy() throws Exception {
		if (null != scheduledExecutorService) {
			scheduledExecutorService.shutdown();
			if (logger.isInfoEnabled()) {
				logger.info("========================================= write hessian log to redis scheduler shutdown=========================================");
			}
		}
	}

}
