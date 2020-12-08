package com.elsa.configserver.controller;
//package com.elsa.configserver.controller;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.DisposableBean;
//import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
//import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
//import org.springframework.core.PriorityOrdered;
//
//public class LogHessian implements DisposableBean, BeanFactoryPostProcessor, PriorityOrdered {
//
//	private ExecutorService executorService = Executors.newFixedThreadPool(4);
//
//	private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
//
////	public static void main(String argsp[]) {
////		ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
////		for (int j = 0; j < 10; j++) {
////			map.put("key" + j, "value" + j);
////		}
////		for (Map.Entry<String, String> entry : map.entrySet()) {
////			MeitunHessianServiceExporter.logger.info(entry.getKey());
////			map.remove(entry.getKey());
////		}
////		MeitunHessianServiceExporter.logger.info("*******************************************************");
////		for (Map.Entry<String, String> entry : map.entrySet()) {
////			MeitunHessianServiceExporter.logger.info(entry.getKey());
////		}
////	}
//
//	public void destroy() throws Exception {
//		executorService.shutdown();
//	}
//
//	@Override
//	public int getOrder() {
//		return LOWEST_PRECEDENCE - 11;
//	}
//
//	@Override
//	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//		scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
//			public void run() {
//				// MeitunHessianServiceExporter.logger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//			}
//		}, 10, 10, TimeUnit.MILLISECONDS);
//		// executorService.execute(new Runnable() {
//		// public void run() {
//		// MeitunHessianServiceExporter.logger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//		// }
//		// });
//	}
//
//}
