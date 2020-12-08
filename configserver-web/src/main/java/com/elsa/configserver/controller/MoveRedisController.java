package com.elsa.configserver.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import redis.clients.jedis.Jedis;

@Controller
public class MoveRedisController {

	public static final Log logger = LogFactory.getLog(MoveRedisController.class);

	@Autowired
	RedisDO redisDO;

	@RequestMapping(value = "/move/redis", method = RequestMethod.GET)
	public String dbLinkOpen(final ModelMap model, HttpServletRequest request) {

		String oldRedisHosts = redisDO.getOldRedisHosts();
		String newRedisHosts = redisDO.getNewRedisHosts();

		List<String> get_redis_all_key_patten = get_redis_all_key_patten();

		for (int i = 0; i < 20; i++) {
			int start = i * 500;
			int end = (i + 1) * 500;
			MoveRedisThread t = new MoveRedisThread(oldRedisHosts, newRedisHosts, get_redis_all_key_patten.subList(start, end));
			t.start();
		}

		String redisCacheAllHosts = redisDO.getRedisCacheAllHosts();
		int timeout = redisDO.getTimeout();
		String[] split = redisCacheAllHosts.split(",");
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> hitMap = new HashMap<String, String>();
		for (String string : split) {
			String[] split2 = string.split(":");
			String host = split2[0];
			String port = split2[1];
			Jedis j = new Jedis(host, Integer.parseInt(port), timeout);
			String info = j.info();
			j.quit();
			j.disconnect();
			j.close();
			map.put(string, info);
			String[] split3 = info.split("\\n");
			double keyspace_hits = 0;
			double keyspace_misses = 0;
			for (String string2 : split3) {
				String[] split4 = string2.split(":");
				if (split4[0].equals("keyspace_hits")) {
					keyspace_hits = Double.parseDouble(split4[1].trim());
				}
				if (split4[0].equals("keyspace_misses")) {
					keyspace_misses = Double.parseDouble(split4[1].trim());
				}
			}
			double i = divdouble(keyspace_hits, keyspace_hits + keyspace_misses, 4) * 100;
			hitMap.put(string, i + "");
		}

		model.put("cacheList", map);
		model.put("hitMap", hitMap);
		return "view_hit";
	}

	private static double divdouble(double d1, double d2, int len) {
		if (d1 == 0) {
			return 0;
		}
		if (d2 == 0) {
			return 0;
		}

		BigDecimal b1 = new BigDecimal(d1);
		BigDecimal b2 = new BigDecimal(d2);
		return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static List<String> get_redis_all_key_patten() {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				for (int k = 0; k < 10; k++) {
					for (int l = 0; l < 10; l++) {
						String s = i + "" + j + "" + k + "" + l + "*user";
						// if (s.startsWith("000")) {
						list.add(s);
						// }
					}
				}
			}
		}
		for (int i = 65; i < 26 + 65; i++) {
			char c = (char) i;
			Character s = Character.valueOf(c);
			list.add(s.toString().toLowerCase() + "*user");
			list.add(s.toString() + "*user");
		}
		Collections.sort(list);
		return list;
	}

	public static void main(String args[]) {
		List<String> get_redis_all_key_patten = get_redis_all_key_patten();
		for (String str : get_redis_all_key_patten) {
		}
		for (int i = 0; i < 20; i++) {
			System.out.println("thread-" + i);
			int start = i * 500;
			int end = (i + 1) * 500;
			List<String> subList = get_redis_all_key_patten.subList(start, end);
			for (String str : subList) {
				System.out.println(str);
			}
		}

	}
}

class MoveRedisThread extends Thread {

	public final static Integer TOKEN_LIVE_TIME = 24 * 3600 * 365;

	private Jedis old_jedis;
	private Jedis new_jedis;
	private List<String> keyPattens;
	private volatile boolean needRun = true;

	public MoveRedisThread(String oldHosts, String newHosts, List<String> keyPattens) {
		String[] oldSplit = oldHosts.split(",");
		String[] newSplit = newHosts.split(",");
		old_jedis = new Jedis(oldSplit[0], Integer.parseInt(oldSplit[1]), 1000 * 1000);
		old_jedis.auth(oldSplit[2]);

		new_jedis = new Jedis(newSplit[0], Integer.parseInt(newSplit[1]), 1000 * 1000);
		new_jedis.auth(newSplit[2]);
		this.keyPattens = keyPattens;
	}

	public void run() {
		if (needRun) {
			for (String keyPatten : keyPattens) {
				MoveRedisController.logger.info("run patten is=>" + keyPatten);
				Set<String> keys = old_jedis.keys(keyPatten);
				for (String key : keys) {
					if (null != key) {
						byte[] bs = old_jedis.get(key.getBytes());
						if (null != bs) {
							String set = new_jedis.set(key.getBytes(), bs);
							if (null != set) {
								boolean equalsIgnoreCase = "ok".equalsIgnoreCase(set);
								if (equalsIgnoreCase) {
									new_jedis.expire(key.getBytes(), TOKEN_LIVE_TIME);
								} else {
									MoveRedisController.logger.info("fail key is:" + key);
								}
							}
						}
					}
				}
			}
			needRun = false;
			try {
				old_jedis.quit();
				old_jedis.disconnect();
				old_jedis.close();
			} catch (Exception e) {

			}
			try {
				new_jedis.quit();
				new_jedis.disconnect();
				new_jedis.close();
			} catch (Exception e) {

			}
		}
	}
}
