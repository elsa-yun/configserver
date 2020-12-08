package com.elsa.configserver.controller;

public class T {

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
}
