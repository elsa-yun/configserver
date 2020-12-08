package com.elsa.configserver.controller;
//package com.elsa.configserver.controller;
//
//import java.math.BigDecimal;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//import java.util.UUID;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import redis.clients.jedis.Jedis;
//
//import com.elsa.mq.RabbitMqProducer;
//import com.elsa.mq.exception.MqClientException;
//import com.elsa.redis.util.JedisCacheUtil;
//
//@Controller
//public class HessianRedisController {
//
//	private static final String AAAA = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//			+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//			+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//			+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//			+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//			+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//			+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//			+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
//
//	private static final Log logger = LogFactory.getLog(HessianRedisController.class);
//
//	@Autowired
//	JedisCacheUtil jedisCacheUtil;
//
//	// hessian.
//
//	@Autowired
//	RedisDO redisDO;
//
//	@Autowired
//	RabbitMqProducer rabbitMqProducer;
//
//	final ConcurrentMap<Integer, String> setMap = new ConcurrentHashMap<Integer, String>();
//
//	final ConcurrentMap<Integer, String> delMap = new ConcurrentHashMap<Integer, String>();
//
//	@RequestMapping(value = "/hit/rate", method = RequestMethod.GET)
//	public String dbLinkOpen(final ModelMap model, HttpServletRequest request) {
//		String redisCacheAllHosts = redisDO.getRedisCacheAllHosts();
//		int timeout = redisDO.getTimeout();
//		String[] split = redisCacheAllHosts.split(",");
//		Map<String, String> map = new HashMap<String, String>();
//		Map<String, String> hitMap = new HashMap<String, String>();
//		for (String string : split) {
//			String[] split2 = string.split(":");
//			String host = split2[0];
//			String port = split2[1];
//			Jedis j = new Jedis(host, Integer.parseInt(port), timeout);
//			String info = j.info();
//			j.quit();
//			j.disconnect();
//			j.close();
//			map.put(string, info);
//			String[] split3 = info.split("\\n");
//			double keyspace_hits = 0;
//			double keyspace_misses = 0;
//			for (String string2 : split3) {
//				String[] split4 = string2.split(":");
//				if (split4[0].equals("keyspace_hits")) {
//					keyspace_hits = Double.parseDouble(split4[1].trim());
//				}
//				if (split4[0].equals("keyspace_misses")) {
//					keyspace_misses = Double.parseDouble(split4[1].trim());
//				}
//			}
//			double i = divdouble(keyspace_hits, keyspace_hits + keyspace_misses, 4) * 100;
//			hitMap.put(string, i + "");
//		}
//
//		model.put("cacheList", map);
//		model.put("hitMap", hitMap);
//		return "view_hit";
//	}
//
//	// @RequestMapping(value = "/redis/db/{num}", method = RequestMethod.GET)
//	// public String redisDB(final ModelMap model, @PathVariable int num,
//	// HttpServletRequest request) {
//	// for (int i = 0; i < 1; i++) {
//	// jedisDBUtil.setDBString("cache_key" + i, "中国国加另中人呀" + num);
//	// }
//	// List<String> list = new ArrayList<String>();
//	// for (int i = 0; i < 1; i++) {
//	// String cacheString = jedisDBUtil.getDBString("cache_key" + i);
//	// list.add(cacheString);
//	// }
//	// model.put("cacheList", list);
//	// return "view_cache";
//	// }
//
//	private static double divdouble(double d1, double d2, int len) {
//		if (d1 == 0) {
//			return 0;
//		}
//		if (d2 == 0) {
//			return 0;
//		}
//
//		BigDecimal b1 = new BigDecimal(d1);
//		BigDecimal b2 = new BigDecimal(d2);
//		return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
//	}
//
//	@RequestMapping(value = "/mq/send/{num}", method = RequestMethod.GET)
//	public String mq(final ModelMap model, @PathVariable int num, HttpServletRequest request) {
//		for (int i = 0; i < num; i++) {
//			try {
//				rabbitMqProducer.sendP2PMessage("confirm_msg_test", AAAA);
//			} catch (MqClientException e) {
//				logger.error(e.getMessage(), e);
//			}
//		}
//		model.put("cacheList", "" + num);
//		return "view_cache";
//	}
//
//	@RequestMapping(value = "/test/redis/keys", method = RequestMethod.GET)
//	public String testInsertRedisView(final ModelMap model, HttpServletRequest request) {
//		String testRedisHosts = redisDO.getOldRedisHosts();
//		String[] split6 = testRedisHosts.split(":");
//		String testHost = split6[0];
//		String testPort = split6[1];
//		Jedis jedis = null;
//		try {
//			jedis = new Jedis(testHost, Integer.parseInt(testPort), 2000);
//			Set<String> keys = jedis.keys("*");
//			// for (String key : keys) {
//			// Long ttl = jedis.ttl(key);
//			// }
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			jedis.quit();
//			jedis.disconnect();
//			jedis.close();
//		}
//
//		String redisCacheAllHosts = redisDO.getRedisCacheAllHosts();
//		int timeout = redisDO.getTimeout();
//		String[] split = redisCacheAllHosts.split(",");
//		Map<String, String> map = new HashMap<String, String>();
//		Map<String, String> hitMap = new HashMap<String, String>();
//		for (String string : split) {
//			String[] split2 = string.split(":");
//			String host = split2[0];
//			String port = split2[1];
//			Jedis j = new Jedis(host, Integer.parseInt(port), timeout);
//			String info = j.info();
//			j.quit();
//			j.disconnect();
//			j.close();
//			map.put(string, info);
//			String[] split3 = info.split("\\n");
//			double keyspace_hits = 0;
//			double keyspace_misses = 0;
//			for (String string2 : split3) {
//				logger.debug("+++++" + string2);
//				String[] split4 = string2.split(":");
//				if (split4[0].equals("keyspace_hits")) {
//					keyspace_hits = Double.parseDouble(split4[1].trim());
//				}
//				if (split4[0].equals("keyspace_misses")) {
//					keyspace_misses = Double.parseDouble(split4[1].trim());
//				}
//			}
//			logger.debug("+++++keyspace_hits:" + keyspace_hits);
//			logger.debug("+++++keyspace_misses:" + keyspace_misses);
//			double i = divdouble(keyspace_hits, keyspace_hits + keyspace_misses, 4) * 100;
//			hitMap.put(string, i + "");
//		}
//
//		model.put("cacheList", map);
//		model.put("hitMap", hitMap);
//		return "view_hit";
//	}
//
//	@RequestMapping(value = "/test/redis/insert", method = RequestMethod.GET)
//	public String testInsertRedis(final ModelMap model, HttpServletRequest request) {
//
//		String testRedisHosts = redisDO.getOldRedisHosts();
//		String[] split6 = testRedisHosts.split(":");
//		String testHost = split6[0];
//		String testPort = split6[1];
//		for (int i = 0; i < 15; i++) {
//			MyThread t = new MyThread(testHost, testPort);
//			t.start();
//		}
//
//		String redisCacheAllHosts = redisDO.getRedisCacheAllHosts();
//		int timeout = redisDO.getTimeout();
//		String[] split = redisCacheAllHosts.split(",");
//		Map<String, String> map = new HashMap<String, String>();
//		Map<String, String> hitMap = new HashMap<String, String>();
//		for (String string : split) {
//			String[] split2 = string.split(":");
//			String host = split2[0];
//			String port = split2[1];
//			Jedis j = new Jedis(host, Integer.parseInt(port), timeout);
//			String info = j.info();
//			j.quit();
//			j.disconnect();
//			j.close();
//			map.put(string, info);
//			String[] split3 = info.split("\\n");
//			double keyspace_hits = 0;
//			double keyspace_misses = 0;
//			for (String string2 : split3) {
//				logger.debug("+++++" + string2);
//				String[] split4 = string2.split(":");
//				if (split4[0].equals("keyspace_hits")) {
//					keyspace_hits = Double.parseDouble(split4[1].trim());
//				}
//				if (split4[0].equals("keyspace_misses")) {
//					keyspace_misses = Double.parseDouble(split4[1].trim());
//				}
//			}
//			logger.debug("+++++keyspace_hits:" + keyspace_hits);
//			logger.debug("+++++keyspace_misses:" + keyspace_misses);
//			double i = divdouble(keyspace_hits, keyspace_hits + keyspace_misses, 4) * 100;
//			hitMap.put(string, i + "");
//		}
//
//		model.put("cacheList", map);
//		model.put("hitMap", hitMap);
//		return "view_hit";
//	}
//
//}
//
//class MyThread extends Thread {
//
//	private String host;
//
//	private String port;
//
//	private Jedis jedis;
//
//	public MyThread(String host, String port) {
//		super();
//		this.host = host;
//		this.port = port;
//		jedis = new Jedis(host, Integer.parseInt(port), 100 * 1000);
//	}
//
//	public void run() {
//		for (int i = 0; i < 100 * 10000; i++) {
//			jedis.set(UUID.randomUUID().toString(), UUID.randomUUID().toString());
//		}
//		jedis.quit();
//		jedis.disconnect();
//		jedis.close();
//	}
//}
