package com.taobao.session.config;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

/**
 * 
 * @author fenghao
 * 
 */
public class ServerSessionList {

	private static AtomicInteger lasttime = new AtomicInteger(0);

	private static final Logger tairErrorlogger = Logger.getLogger("tairlog");

	private static Set<String> nickKeySet = new HashSet<String>();

	public static Set<String> getNickKeySet() {
		return nickKeySet;
	}

	public static void setNickKeySet(Set<String> nickKeySet) {
		ServerSessionList.nickKeySet = nickKeySet;
	}

	public static void addError(String errorMsg) {
		tairErrorlogger.error(errorMsg);
	}
	
	public static void addInfo(String warnMsg) {
		tairErrorlogger.info(warnMsg);
	}
	
	public static void addWarn(String message) {
		if (200 == lasttime.get()) {
			tairErrorlogger.warn(message);
			lasttime.set(0);
		} else {
			if (lasttime.get() < 200) {
				lasttime.getAndAdd(1);
			}
		}
	}

}
