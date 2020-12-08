package com.elsa.configserver.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import redis.clients.jedis.Jedis;

import com.elsa.mq.RabbitMqProducer;
import com.elsa.mq.exception.MqClientException;
import com.elsa.redis.util.JedisCacheUtil;

@Controller
public class HitRateRedisController {

	private static final String AAAA = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
			+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
			+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
			+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
			+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
			+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
			+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
			+ "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

	private static final Log logger = LogFactory.getLog(HitRateRedisController.class);

	@Autowired
	JedisCacheUtil jedisCacheUtil;

	// @Autowired
	// JedisDBUtil jedisDBUtil;//hessian.

	@Autowired
	RedisDO redisDO;

/*	@Autowired
	RabbitMqProducer rabbitMqProducer;*/

	final ConcurrentMap<Integer, String> setMap = new ConcurrentHashMap<Integer, String>();

	final ConcurrentMap<Integer, String> delMap = new ConcurrentHashMap<Integer, String>();

	@RequestMapping(value = "/hit/rate", method = RequestMethod.GET)
	public String dbLinkOpen(final ModelMap model, HttpServletRequest request) {
		String redisCacheAllHosts = redisDO.getRedisCacheAllHosts();
		int timeout = redisDO.getTimeout();
		String[] split = redisCacheAllHosts.split(",");
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> hitMap = new HashMap<String, String>();
		List<HessianDO> hessian_logs = new ArrayList<HessianDO>();
		List<String> hessians = new ArrayList<String>();
		for (String string : split) {
			String[] split2 = string.split(":");
			String host = split2[0];
			String port = split2[1];
			Jedis j = new Jedis(host, Integer.parseInt(port), timeout);
			String info = j.info();

			Set<String> keys = j.keys("hessian.*");
			List<String> list = new ArrayList<String>();
			list.addAll(keys);
			Collections.sort(list);
			for (String k : list) {
				String value = j.get(k);
				if (null != value) {
					value = value.replaceAll("\\^\\^\\^", "@@@LHS@@@");
					String[] split3 = value.split("@@@LHS@@@");
					if (split3.length > 6) {
						String interfaceName = split3[0];
						String methodName = split3[1];
						String methodIp = split3[2];
						int methodTime = Integer.valueOf(split3[3]);
						String methodCount = split3[4];
						String remotingHost = split3[5];
						String callTimes = split3[6];
						HessianDO h = new HessianDO();
						h.setInterfaceName(interfaceName);
						h.setMethodName(methodName);
						h.setMethodIp(methodIp);
						h.setMethodTime(methodTime);
						h.setRemotingHost(remotingHost);
						h.setCallTimes(callTimes);
						h.setMethodCount(methodCount);
						hessian_logs.add(h);
					} else {
						hessians.add(value);
					}
				}
			}

			j.quit();
			j.disconnect();
			j.close();
			map.put(string, info);
			String[] split3 = info.split("\\n");
			double keyspace_hits = 0;
			double keyspace_misses = 0;
			for (String string2 : split3) {
				logger.debug("+++++" + string2);
				String[] split4 = string2.split(":");
				if (split4[0].equals("keyspace_hits")) {
					keyspace_hits = Double.parseDouble(split4[1].trim());
				}
				if (split4[0].equals("keyspace_misses")) {
					keyspace_misses = Double.parseDouble(split4[1].trim());
				}
			}
			logger.debug("+++++keyspace_hits:" + keyspace_hits);
			logger.debug("+++++keyspace_misses:" + keyspace_misses);
			double i = divdouble(keyspace_hits, keyspace_hits + keyspace_misses, 4) * 100;
			hitMap.put(string, i + "");
		}

		Collections.sort(hessian_logs, new Comparator<HessianDO>() {
			@Override
			public int compare(HessianDO o1, HessianDO o2) {
				if (o1.getMethodTime() > o2.getMethodTime()) {
					return -1;
				}
				if (o1.getMethodTime() < o2.getMethodTime()) {
					return 1;
				}
				return 0;
			}
		});
		model.put("hessian_logs", hessian_logs);
		model.put("cacheList", map);
		model.put("hitMap", hitMap);
		model.put("hessians", hessians);
		return "view_hit";
	}

	// @RequestMapping(value = "/redis/db/{num}", method = RequestMethod.GET)
	// public String redisDB(final ModelMap model, @PathVariable int num,
	// HttpServletRequest request) {
	// for (int i = 0; i < 1; i++) {
	// jedisDBUtil.setDBString("cache_key" + i, "中国国加另中人呀" + num);
	// }
	// List<String> list = new ArrayList<String>();
	// for (int i = 0; i < 1; i++) {
	// String cacheString = jedisDBUtil.getDBString("cache_key" + i);
	// list.add(cacheString);
	// }
	// model.put("cacheList", list);
	// return "view_cache";
	// }
	
	/*
	 select C.id as couponId,
		C.coupon_name as couponName,
		C.coupon_type as couponType,
		C.face_value as faceValue,
		C.need_over_mon as needOverMon,
		C.coupon_use_stime as couponUseStime,
		C.coupon_use_etime as couponUseEtime,
		C.use_plantform as usePlantform,
		C.use_range as useRange,
		C.coupon_use_type as couponUseType,
		C.use_receive_day as useReceiveDay,
		C.hitao_sign as hitaoSign,
		C.coupon_number as couponNumber,
		C.offer_type as offerType,
		C.just_scan as justScan,
		CU.id as couponUserId,
		CU.number as number,
		CU.source as source,
		CU.source_name as sourceName,
		CU.ref_code as refCode,
		CU.status as couponUserStatus,
		CU.to_user_id as toUserId,
		CU.to_user_login as toUserLogin,
		CU.create_user_id as sendEmpId,
		CU.create_user_name as sendEmp,
		CU.create_time as sendTime
		from coupon C , coupon_user CU
		where 
		C.id = CU.batch_id
		and CU.status != 4
		 
		 
		 
		 
		 
		 
		order by CU.id desc 
	 	Limit 0,?
	 */

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

	@RequestMapping(value = "/mq/send/{num}", method = RequestMethod.GET)
	public String mq(final ModelMap model, @PathVariable int num, HttpServletRequest request) {
		for (int i = 0; i < num; i++) {
/*			try {
				rabbitMqProducer.sendP2PMessage("confirm_msg_test", AAAA);
			} catch (MqClientException e) {
				logger.error(e.getMessage(), e);
			}*/
		}
		model.put("cacheList", "" + num);
		return "view_cache";
	}

	@RequestMapping(value = "/test/redis/keys", method = RequestMethod.GET)
	public String testInsertRedisView(final ModelMap model, HttpServletRequest request) {
		String testRedisHosts = redisDO.getOldRedisHosts();
		String[] split6 = testRedisHosts.split(":");
		String testHost = split6[0];
		String testPort = split6[1];
		Jedis jedis = null;
		try {
			jedis = new Jedis(testHost, Integer.parseInt(testPort), 2000);
			Set<String> keys = jedis.keys("*");
			// for (String key : keys) {
			// Long ttl = jedis.ttl(key);
			// }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.quit();
			jedis.disconnect();
			jedis.close();
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
				logger.debug("+++++" + string2);
				String[] split4 = string2.split(":");
				if (split4[0].equals("keyspace_hits")) {
					keyspace_hits = Double.parseDouble(split4[1].trim());
				}
				if (split4[0].equals("keyspace_misses")) {
					keyspace_misses = Double.parseDouble(split4[1].trim());
				}
			}
			logger.debug("+++++keyspace_hits:" + keyspace_hits);
			logger.debug("+++++keyspace_misses:" + keyspace_misses);
			double i = divdouble(keyspace_hits, keyspace_hits + keyspace_misses, 4) * 100;
			hitMap.put(string, i + "");
		}

		model.put("cacheList", map);
		model.put("hitMap", hitMap);
		return "view_hit";
	}

	@RequestMapping(value = "/test/redis/insert", method = RequestMethod.GET)
	public String testInsertRedis(final ModelMap model, HttpServletRequest request) {

		String testRedisHosts = redisDO.getOldRedisHosts();
		String[] split6 = testRedisHosts.split(":");
		String testHost = split6[0];
		String testPort = split6[1];
		for (int i = 0; i < 15; i++) {
			MyThread t = new MyThread(testHost, testPort);
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
				logger.debug("+++++" + string2);
				String[] split4 = string2.split(":");
				if (split4[0].equals("keyspace_hits")) {
					keyspace_hits = Double.parseDouble(split4[1].trim());
				}
				if (split4[0].equals("keyspace_misses")) {
					keyspace_misses = Double.parseDouble(split4[1].trim());
				}
			}
			logger.debug("+++++keyspace_hits:" + keyspace_hits);
			logger.debug("+++++keyspace_misses:" + keyspace_misses);
			double i = divdouble(keyspace_hits, keyspace_hits + keyspace_misses, 4) * 100;
			hitMap.put(string, i + "");
		}

		model.put("cacheList", map);
		model.put("hitMap", hitMap);
		return "view_hit";
	}

	public static void main(String args[]) {
		String s = "com.elsa.configserver.client.Base^^^get_test_hessian_str_new^^^192.168.10.25^^^501^^^0^^^http://127.0.0.1:8080/configserver-web/base.hessian^^^1";
		String[] split = s.split("\\^\\^\\^");
		System.out.println(split.length);
		System.out.println(Integer.MAX_VALUE);
	}
}

class MyThread extends Thread {

	private String host;

	private String port;

	private Jedis jedis;

	public MyThread(String host, String port) {
		super();
		this.host = host;
		this.port = port;
		jedis = new Jedis(host, Integer.parseInt(port), 100 * 1000);
	}

	public void run() {
		for (int i = 0; i < 100 * 10000; i++) {
			jedis.set(UUID.randomUUID().toString(), UUID.randomUUID().toString());
		}
		jedis.quit();
		jedis.disconnect();
		jedis.close();
	}
}
