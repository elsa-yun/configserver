package com.alibaba.common.lang;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

/**
 * @author yxq1871
 *
 */
public class Stater {
	private static Logger log = Logger.getLogger(Stater.class);
	private static Logger statLog = Logger.getLogger("System-Stat");

	private static Map<String, AtomicLong> datas = new HashMap<String, AtomicLong>(100);

	private static ReentrantLock lock = new ReentrantLock();

	private static ReentrantLock timerLock = new ReentrantLock();
	private static Condition condition = timerLock.newCondition();

	static long waitTime = 60000L;

	private static Thread writeThread = null;

	static {
		runWriteThread();
	}

	private static void runWriteThread() {
		if (null != writeThread) {
			try {
				writeThread.interrupt();
			} catch (Exception e) {
				log.error("interrupt write thread error", e);
			}
		}
		writeThread = new Thread(new Runnable() {
			public void run() {
				while (true) {
					timerLock.lock();
					try {
						condition.await(waitTime, TimeUnit.MILLISECONDS);
					} catch (Exception e) {
						log.error("wait error", e);
					} finally {
						timerLock.unlock();
					}
					Map<String, Long> tmp = new HashMap<String, Long>(datas.size());

					StringBuilder sb = new StringBuilder();
					sb.append(System.currentTimeMillis()).append(":");
					for (String key : datas.keySet()) {
						long data = datas.get(key).longValue();
						if (data == 0)
							continue;
						sb.append(key).append("-").append(data).append(";");
						tmp.put(key, new Long(data));
					}

					for (String key : tmp.keySet()) {
						datas.get(key).addAndGet(-tmp.get(key).longValue());
					}

					if (tmp.size() > 0)
						statLog.error(sb);
				}
			}
		});
		writeThread.start();
	}

	public static long add(String type, long count) {
		if (!datas.containsKey(type)) {
			lock.lock();
			try {
				if (!datas.containsKey(type)) {
					datas.put(type, new AtomicLong(0));
				}
			} finally {
				lock.unlock();
			}
		}
		long data = datas.get(type).addAndGet(count);
		if ((data % 100000 == 0) && (null == writeThread || !writeThread.isAlive())) {
			lock.lock();
			try {
				runWriteThread();
			} finally {
				lock.unlock();
			}
		}
		return data;
	}

	public static long addOne(String type) {
		return add(type, 1);
	}
}
