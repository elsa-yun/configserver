package com.elsa.configserver.controller;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.remoting.caucho.HessianClientInterceptor;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;
import org.springframework.util.ReflectionUtils;

import redis.clients.jedis.Jedis;

import com.caucho.hessian.client.HessianProxy;

public class MeitunHessianProxyFactoryBean extends HessianProxyFactoryBean {

	public static final Log logger = LogFactory.getLog(MeitunHessianProxyFactoryBean.class);

	private final static Map<String, String> INTERFACE_METHOD_MAP = new HashMap<String, String>(512);

	private static int LOG_METHOD_MAX_MILLISECONDS = 200;

	private static int lOG_METHOD_MIN_MILLISECONDS = 2;

	private int maxMethodTime = LOG_METHOD_MAX_MILLISECONDS;

	public Object invoke(MethodInvocation invocation) throws Throwable {
		long start = System.currentTimeMillis();
		Method method = invocation.getMethod();
		String methodName = method.getName();
		Object[] arguments = invocation.getArguments();
		Object o = super.invoke(invocation);
		long end = System.currentTimeMillis();
		String interface_name = null;
		URL url = null;
		try {
			Field proxy = ReflectionUtils.findField(HessianClientInterceptor.class, "hessianProxy");
			if (null != proxy) {
				proxy.setAccessible(true);
				Object field = ReflectionUtils.getField(proxy, this);

				HessianProxy jdkDynamicProxyTargetObject = getJdkDynamicProxyTargetObjectNew(field);

				Field type_field = ReflectionUtils.findField(jdkDynamicProxyTargetObject.getClass(), "_type");
				type_field.setAccessible(true);
				Object targetInterface = ReflectionUtils.getField(type_field, jdkDynamicProxyTargetObject);
				url = jdkDynamicProxyTargetObject.getURL();

				if (null != targetInterface) {
					interface_name = targetInterface.toString().replace("interface", "");
					interface_name = interface_name.trim();
				}
			}
		} catch (Exception e) {
			throw e;// 此处后续要去掉
		}
		if (null != interface_name) {
			long total_time = end - start;
			String value = interface_name + "^^^" + methodName + "^^^" + total_time + "^^^" + arguments.length;
			interface_name = interface_name.trim();
			if (maxMethodTime < lOG_METHOD_MIN_MILLISECONDS) {
				maxMethodTime = lOG_METHOD_MIN_MILLISECONDS;
			}
			if (total_time > LOG_METHOD_MAX_MILLISECONDS) {
				if (INTERFACE_METHOD_MAP.size() < 15000) {
					INTERFACE_METHOD_MAP.put(interface_name + "^^^" + methodName, value);
				}
			}
			if (total_time > maxMethodTime) {
				StringBuilder sb = new StringBuilder();
				sb.append("method>>>");
				sb.append(methodName);
				sb.append("==args>>>");
				sb.append(arguments.toString());
				sb.append("==stime>>>");
				sb.append(start);
				sb.append("==etime>>>");
				sb.append(end);
				sb.append("==tTime>>>");
				sb.append(total_time);
				sb.append("==interface>>>");
				sb.append(interface_name);
				if (null != url) {
					sb.append("==url>>>");
					sb.append(url);
				}
				logger.info(sb.toString());
			}
		}

		return o;
	}

	public static Map<String, String> get_interface_method_map() {
		return Collections.unmodifiableMap(INTERFACE_METHOD_MAP);
	}

	private static HessianProxy getJdkDynamicProxyTargetObjectNew(Object proxy) throws Exception {
		Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
		h.setAccessible(true);
		return (HessianProxy) h.get(proxy);
	}

	public int getMaxMethodTime() {
		return maxMethodTime;
	}

	public void setMaxMethodTime(int maxMethodTime) {
		this.maxMethodTime = maxMethodTime;
	}

	public static void main(String args[]) throws InterruptedException {
		Jedis j = new Jedis("172.16.1.65", 6379, 2000);
		j.auth("redis");

		for (int i = 0; i < 0; i++) {
			String set = j.set("lhs_cache_key_" + i, UUID.randomUUID().toString());
			String get = j.get("lhs_cache_key_" + i);
			System.out.println(get);
		}

		j.quit();
		j.disconnect();
		j.close();
	}
	// final CountDownLatch begin = new CountDownLatch(1);
	// final ConcurrentHashMap<String, Object> concurrent_hash_map = new
	// ConcurrentHashMap<String, Object>(1024);
	// AtomicInteger ai = new AtomicInteger();
	// final AtomicInteger count = new AtomicInteger();
	// for (int i = 0; i < 10000; i++) {
	// concurrent_hash_map.put("key" + i,
	// "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
	// + i);
	// int incrementAndGet = ai.incrementAndGet();
	// if (incrementAndGet >= 10000) {
	// begin.countDown();
	// }
	// }
	//
	// final ScheduledExecutorService ses =
	// Executors.newScheduledThreadPool(5);
	// ses.scheduleWithFixedDelay(new Runnable() {
	// @Override
	// public void run() {
	// try {
	// begin.await();
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// synchronized (this) {
	// for (Map.Entry<String, Object> entry :
	// concurrent_hash_map.entrySet()) {
	// MeitunHessianServiceExporter.logger.info(entry.getKey());
	// concurrent_hash_map.remove(entry.getKey());
	// count.incrementAndGet();
	// }
	// System.out.println("++++++" + count.get());
	// }
	// if (count.get() == 10000) {
	// ses.shutdown();
	// }
	// }
	// }, 10, 10, TimeUnit.MILLISECONDS);
	// ses.scheduleWithFixedDelay(new Runnable() {
	// @Override
	// public void run() {
	// try {
	// begin.await();
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// synchronized (this) {
	// for (Map.Entry<String, Object> entry :
	// concurrent_hash_map.entrySet()) {
	// MeitunHessianServiceExporter.logger.info(entry.getKey());
	// concurrent_hash_map.remove(entry.getKey());
	// count.incrementAndGet();
	// }
	// System.out.println("**********" + count.get());
	// }
	// if (count.get() == 10000) {
	// ses.shutdown();
	// }
	// }
	// }, 10, 10, TimeUnit.MILLISECONDS);
	// ses.scheduleWithFixedDelay(new Runnable() {
	// @Override
	// public void run() {
	// try {
	// begin.await();
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// synchronized (this) {
	// for (Map.Entry<String, Object> entry :
	// concurrent_hash_map.entrySet()) {
	// MeitunHessianServiceExporter.logger.info(entry.getKey());
	// concurrent_hash_map.remove(entry.getKey());
	// count.incrementAndGet();
	// }
	// System.out.println("############" + count.get());
	// }
	// if (count.get() == 10000) {
	// ses.shutdown();
	// }
	// }
	// }, 10, 10, TimeUnit.MILLISECONDS);
	// ses.scheduleWithFixedDelay(new Runnable() {
	// @Override
	// public void run() {
	// try {
	// begin.await();
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// synchronized (this) {
	// for (Map.Entry<String, Object> entry :
	// concurrent_hash_map.entrySet()) {
	// MeitunHessianServiceExporter.logger.info(entry.getKey());
	// concurrent_hash_map.remove(entry.getKey());
	// count.incrementAndGet();
	// }
	// System.out.println("@@@@@@@@@@@@" + count.get());
	// }
	// if (count.get() == 10000) {
	// ses.shutdown();
	// }
	// }
	// }, 10, 10, TimeUnit.MILLISECONDS);

	// end.await();
	// System.out.println("Game Over" + count.get());
	// ses.shutdown();
	// #############################################################################################################################
	// final ScheduledExecutorService ses =
	// Executors.newScheduledThreadPool(5);
	// final BlockingQueue<String> queue = new
	// LinkedBlockingQueue<String>(10000);
	// final AtomicInteger i = new AtomicInteger(0);
	// final AtomicInteger count = new AtomicInteger(0);
	//
	// ses.scheduleWithFixedDelay(new Runnable() {
	// @Override
	// public void run() {
	// if (i.get() <= 100000) {
	// try {
	// queue.put("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + i);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// i.addAndGet(1);
	// }
	// }
	// }, 10, 10, TimeUnit.MILLISECONDS);
	//
	// for (int j = 0; j < 4; j++) {
	// ses.scheduleWithFixedDelay(new Runnable() {
	// @Override
	// public void run() {
	// try {
	// String take = queue.take();
	//
	// if (null != take) {
	// count.incrementAndGet();
	// MeitunHessianServiceExporter.logger.info(take);
	// }
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// }
	// }, 10, 10, TimeUnit.MILLISECONDS);
	// }
	//
	// }

	// final ScheduledExecutorService ses = Executors.newScheduledThreadPool(2);
	// final AtomicInteger i = new AtomicInteger(0);
	// final AtomicInteger count = new AtomicInteger(0);
	//
	// final ConcurrentHashMap<String, Object> concurrent_hash_map = new
	// ConcurrentHashMap<String, Object>(1024);
	//
	// ses.scheduleWithFixedDelay(new Runnable() {
	// @Override
	// public void run() {
	// if (i.get() <= 100000) {
	// concurrent_hash_map.put("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
	// + i, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + i);
	// i.addAndGet(1);
	// }
	// }
	// }, 10, 10, TimeUnit.MILLISECONDS);
	//
	// ses.scheduleWithFixedDelay(new Runnable() {
	// @Override
	// public void run() {
	// for (Map.Entry<String, Object> entry : concurrent_hash_map.entrySet()) {
	// MeitunHessianServiceExporter.logger.info(entry.getKey());
	// concurrent_hash_map.remove(entry.getKey());
	// int incrementAndGet = count.incrementAndGet();
	// MeitunHessianServiceExporter.logger.info("&&&&&&" + incrementAndGet);
	// }
	// }
	// }, 5, 5, TimeUnit.MILLISECONDS);
	// }

	// private static Object getCglibProxyTargetObjectnNew(Object proxy) throws
	// Exception {
	// Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
	// h.setAccessible(true);
	// return h.get(proxy);
	// }
	// private static Object getCglibProxyTargetObject(Object proxy) throws
	// Exception {
	// Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
	// h.setAccessible(true);
	// Object dynamicAdvisedInterceptor = h.get(proxy);
	// Field advised =
	// dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
	// advised.setAccessible(true);
	//
	// return
	// ((AdvisedSupport)advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
	// }
	//
	//
	// private static Object getJdkDynamicProxyTargetObject(Object proxy) throws
	// Exception {
	// Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
	// h.setAccessible(true);
	// AopProxy aopProxy = (AopProxy) h.get(proxy);
	// Field advised = aopProxy.getClass().getDeclaredField("advised");
	// advised.setAccessible(true);
	//
	// return
	// ((AdvisedSupport)advised.get(aopProxy)).getTargetSource().getTarget();
	// }
	//
	// private static Object getTarget(Object proxy) throws Exception {
	//
	// if (!AopUtils.isAopProxy(proxy)) {
	// return proxy;
	// }
	//
	// if (AopUtils.isJdkDynamicProxy(proxy)) {
	// return getJdkDynamicProxyTargetObject(proxy);
	// } else {
	// return getCglibProxyTargetObject(proxy);
	// }
	// }
}
