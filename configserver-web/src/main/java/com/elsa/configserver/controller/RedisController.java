package com.elsa.configserver.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import redis.clients.jedis.Jedis;

import com.elsa.redis.util.JedisCacheUtil;
import com.elsa.redis.util.JedisDBUtil;

@Controller
public class RedisController {

	private static final String SET_GET_KEY = "execute_key_";

	private static final String JEDIS_KEY = "jedis_key_";

	private static final String VALUE3 = "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd"
			+ "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd"
			+ "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd";

	private static final String VALUE1 = "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd";
	private static final String VALUE2 = "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd";

	private static final String LHS_KEY = "lhs_key_";

	private static final Log logger = LogFactory.getLog(RedisController.class);

	@Autowired
	JedisCacheUtil jedisCacheUtil;

	// @Autowired
	JedisDBUtil jedisDBUtil;

//	@Autowired
//	RedisDO redisDO;

	private ConcurrentMap<String, Jedis> jedisMap = new ConcurrentHashMap<String, Jedis>();

	private AtomicInteger ai = new AtomicInteger(0);

	private AtomicInteger fail = new AtomicInteger(0);

	private AtomicInteger getai = new AtomicInteger(0);

	private AtomicInteger getfail = new AtomicInteger(0);

	final ConcurrentMap<Integer, String> setMap = new ConcurrentHashMap<Integer, String>();

	final ConcurrentMap<Integer, String> delMap = new ConcurrentHashMap<Integer, String>();
	
	@RequestMapping(value = "/set/key", method = RequestMethod.GET)
	public String dbLinkOpen(final ModelMap model, HttpServletRequest request) {
		for (int i = 0; i < 100; i++) {
			jedisCacheUtil.setCacheString("cache_key" + i, "中国国加另中dfdf", 600);
		}
		List<String> list=new ArrayList<String>();
		for (int i = 0; i < 100; i++) {
			String cacheString = jedisCacheUtil.getCacheString("cache_key" + i);
			list.add(cacheString);
		}
		model.put("cacheList", list);
		return "view_cache";
	}

//	@RequestMapping(value = "/jedis/open/{connectSize}", method = RequestMethod.GET)
//	public String dbLinkOpen(final ModelMap model, @PathVariable int connectSize, HttpServletRequest request) {
//
//		Long startTime = System.currentTimeMillis();
//		logger.info("openStartTime========>" + startTime);
//		for (int i = 0; i < connectSize; i++) {
//			try {
//				Jedis jedis = new Jedis(redisDO.getHost(), redisDO.getPort(), redisDO.getTimeout());
//				jedisMap.put(JEDIS_KEY + i, jedis);
//				logger.info(JEDIS_KEY + i + "========>");
//			} catch (Exception e) {
//				logger.error(e.getMessage(), e);
//			}
//		}
//		Long endTime = System.currentTimeMillis();
//		logger.info("openEndTime========>" + endTime);
//		model.addAttribute("jedisMap", jedisMap);
//		model.addAttribute("jedisMap", jedisMap);
//		model.addAttribute("consumeMilliTime", startTime / 1000 - endTime / 1000);
//		model.addAttribute("consumeTime", "start=>" + startTime + "end=>" + endTime);
//		return "open";
//	}
//
//	@RequestMapping(value = "/jedis/set/{num}/{j}", method = RequestMethod.GET)
//	public String dbLinkSet(final ModelMap model, @PathVariable int num, @PathVariable final int j, HttpServletRequest request) {
//		Long startTime = System.currentTimeMillis();
//
//		ExecutorService es = Executors.newFixedThreadPool(200);
//		ai.set(0);
//		final int size = jedisMap.size();
//		for (int i = 0; i < num; i++) {
//			es.execute(new Runnable() {
//				@Override
//				public void run() {
//					try {
//						Thread.sleep((long) (Math.random() * 50));
//					} catch (InterruptedException e) {
//						logger.error(e.getMessage(), e);
//					}
//					int curr = RandomUtils.nextInt(0, size - 1);
//					try {
//						Jedis jedis = jedisMap.get(JEDIS_KEY + curr);
//						String a = "";
//						if (j == 1) {
//							a = VALUE1;
//						} else if (j == 2) {
//							a = VALUE2;
//						} else {
//							a = VALUE3;
//						}
//
//						Integer integer = RandomUtils.nextInt(0, 10000000);
//						String key = SET_GET_KEY + integer;
//						setMap.put(integer, key);
//						delMap.put(integer, key);
//
//						String set = jedis.set(key, a);
//						if (set.equals("OK")) {
//							jedis.expire(key, 10 * 60);
//							ai.incrementAndGet();
//						} else {
//							fail.incrementAndGet();
//						}
//						logger.info("jedis_key_set jedisMapSize========>" + size + "  numMap.size()" + setMap.size() + " jedis_key_set_" + "========>" + set + "set_currentTimeMillis" + "==>"
//								+ System.currentTimeMillis());
//					} catch (Exception e) {
//						fail.incrementAndGet();
//						logger.error(e.getMessage(), e);
//					}
//					logger.info("ai.set()=>" + ai.get());
//					logger.info("fail.set()=>" + fail.get());
//				}
//			});
//		}
//
//		es.shutdown();
//
//		Long endTime = System.currentTimeMillis();
//		model.addAttribute("jedisMap", jedisMap);
//		model.addAttribute("ai", ai.get());
//		model.addAttribute("fail", fail.get());
//		model.addAttribute("consumeMilliTime", startTime / 1000 - endTime / 1000);
//		return "execute";
//	}
//
//	@RequestMapping(value = "/jedis/get/{num}", method = RequestMethod.GET)
//	public String dbLinkGet(final ModelMap model, @PathVariable int num, HttpServletRequest request) {
//		Long startTime = System.currentTimeMillis();
//
//		ExecutorService es = Executors.newFixedThreadPool(200);
//		getai.set(0);
//
//		final int size = jedisMap.size();
//		for (int i = 0; i < num; i++) {
//			es.execute(new Runnable() {
//				@Override
//				public void run() {
//					try {
//						Thread.sleep((long) (Math.random() * 50));
//					} catch (InterruptedException e) {
//						logger.error(e.getMessage(), e);
//					}
//					int curr = RandomUtils.nextInt(0, size - 1);
//					try {
//						Jedis jedis = jedisMap.get(JEDIS_KEY + curr);
//
//						String jedisKey = "";
//						for (ConcurrentMap.Entry<Integer, String> entry : setMap.entrySet()) {
//							jedisKey = entry.getValue();
//							setMap.remove(entry.getKey());
//							break;
//						}
//
//						if (!"".equals(jedisKey)) {
//							String get = jedis.get(jedisKey);
//							if (null != get && !"".equals(get)) {
//								getai.incrementAndGet();
//							}
//							logger.info("jedis_key_get jedisMapSize========>" + size + "  numMap.size()" + setMap.size() + " jedis_key_get_" + "========>" + get + "set_currentTimeMillis" + "==>"
//									+ System.currentTimeMillis());
//						}
//					} catch (Exception e) {
//						getfail.incrementAndGet();
//						logger.error(e.getMessage(), e);
//					}
//					logger.info("getai.get()=>" + getai.get());
//					logger.info("getfail.get()=>" + getfail.get());
//				}
//			});
//		}
//
//		es.shutdown();
//
//		Long endTime = System.currentTimeMillis();
//		model.addAttribute("jedisMap", jedisMap);
//		model.addAttribute("ai", getai.get());
//		model.addAttribute("fail", getfail.get());
//		model.addAttribute("consumeMilliTime", startTime / 1000 - endTime / 1000);
//		return "execute";
//	}
//
//	@RequestMapping(value = "/jedis/delete/{num}", method = RequestMethod.GET)
//	public String dbLinkDelete(final ModelMap model, @PathVariable int num, HttpServletRequest request) {
//		Long startTime = System.currentTimeMillis();
//		final int size = jedisMap.size();
//		int j = 0;
//		for (int i = 0; i < num; i++) {
//			int curr = RandomUtils.nextInt(0, size - 1);
//			try {
//				Jedis jedis = jedisMap.get(JEDIS_KEY + curr);
//
//				String jedisKey = "";
//				for (ConcurrentMap.Entry<Integer, String> entry : delMap.entrySet()) {
//					jedisKey = entry.getValue();
//					delMap.remove(entry.getKey());
//					break;
//				}
//				if (!"".equals(jedisKey)) {
//					Long delete = jedis.del(jedisKey);
//					if (null != delete && delete > 0) {
//						logger.info("delete key name=>" + jedisKey);
//						j++;
//					}
//				}
//			} catch (Exception e) {
//				logger.error(e.getMessage(), e);
//			}
//		}
//		logger.info("delete keys num=>" + j);
//		Long endTime = System.currentTimeMillis();
//		model.addAttribute("jedisMap", jedisMap);
//		model.addAttribute("consumeTime", "start=>" + startTime + "end=>" + endTime + " delete count nums=>" + j);
//		model.addAttribute("consumeMilliTime", startTime / 1000 - endTime / 1000);
//		return "close";
//	}
//
//	@RequestMapping(value = "/jedis/close", method = RequestMethod.GET)
//	public String dbLinkClose(final ModelMap model, HttpServletRequest request) {
//		Long startTime = System.currentTimeMillis();
//		for (ConcurrentMap.Entry<String, Jedis> entry : jedisMap.entrySet()) {
//			Jedis j = entry.getValue();
//			if (null != j) {
//				try {
//					j.quit();
//					j.disconnect();
//					j.close();
//					logger.info("jedis_key========>" + entry.getKey());
//				} catch (Exception e) {
//					logger.error(e.getMessage(), e);
//				}
//			}
//		}
//
//		Long endTime = System.currentTimeMillis();
//		model.addAttribute("jedisMap", jedisMap);
//		model.addAttribute("consumeTime", "start=>" + startTime + "end=>" + endTime);
//		model.addAttribute("consumeMilliTime", startTime / 1000 - endTime / 1000);
//		return "close";
//	}
//
//	@RequestMapping(value = "/cache", method = RequestMethod.GET)
//	public String cache(final ModelMap model, HttpServletRequest request) {
//
//		String clear = request.getParameter("clear");
//		List<PersonDO> strList = new ArrayList<PersonDO>();
//		Map<String, PersonDO> cacheMap = new HashMap<String, PersonDO>();
//
//		Long startTime = System.currentTimeMillis();
//		PersonDO personDO = null;
//		for (int i = 0; i < 10; i++) {
//			if (null != clear && clear.equals("yes")) {
//				jedisCacheUtil.deleteKey(LHS_KEY + i);
//			}
//			PersonDO str = (PersonDO) jedisCacheUtil.getCache(LHS_KEY + i);
//			if (null == str) {
//				personDO = new PersonDO();
//				personDO.setId(LHS_KEY + i);
//				personDO.setName("abcdefghijklmnopqrstuvwxyz0123456789_" + i);
//				jedisCacheUtil.setCache(LHS_KEY + i, personDO);
//			}
//		}
//		for (int i = 0; i < 10; i++) {
//			PersonDO person = (PersonDO) jedisCacheUtil.getCache(LHS_KEY + i);
//			cacheMap.put(LHS_KEY + i, person);
//			strList.add(person);
//		}
//		Long endTime = System.currentTimeMillis();
//
//		model.addAttribute("cacheMap", cacheMap);
//		model.addAttribute("cacheList", strList);
//		model.addAttribute("consumeTime", "start=>" + startTime + "end=>" + endTime);
//		return "cache";
//	}
//
//	@RequestMapping(value = "/db", method = RequestMethod.GET)
//	public String db(final ModelMap model, HttpServletRequest request) {
//
//		// String clear = request.getParameter("clear");
//		List<PersonDO> strList = new ArrayList<PersonDO>();
//		Map<String, PersonDO> cacheMap = new HashMap<String, PersonDO>();
//
//		Long startTime = System.currentTimeMillis();
//		PersonDO personDO = null;
//		for (int i = 0; i < 10; i++) {
//			// if (null != clear && clear.equals("yes")) {
//			// jedisDBUtil.deleteKey(LHS_KEY + i);
//			// }
//			PersonDO str = (PersonDO) jedisDBUtil.getDB(LHS_KEY + i);
//			if (null == str) {
//				personDO = new PersonDO();
//				personDO.setId(LHS_KEY + i);
//				personDO.setName("abcdefghijklmnopqrstuvwxyz0123456789_" + i);
//				jedisDBUtil.setDB(LHS_KEY + i, personDO);
//			}
//		}
//		for (int i = 0; i < 10; i++) {
//			PersonDO person = (PersonDO) jedisDBUtil.getDB(LHS_KEY + i);
//			cacheMap.put(LHS_KEY + i, person);
//			strList.add(person);
//		}
//		Long endTime = System.currentTimeMillis();
//
//		model.addAttribute("cacheMap", cacheMap);
//		model.addAttribute("cacheList", strList);
//		model.addAttribute("consumeTime", "start=>" + startTime + "end=>" + endTime);
//		return "db";
//	}

//	public static void main(String args[]) {
//		System.out.println(VALUE3.length() * 100000 / 1024 / 1024 + "M");// 976M
//		System.out.println(VALUE2.length() * 100000 / 1024 / 1024 + "M");// 97M
//		System.out.println(VALUE1.length() * 100000 / 1024 / 1024 + "M");// 9M
//
//		System.out.println(VALUE3.length());// 10240
//		System.out.println(VALUE2.length());// 10240
//		System.out.println(VALUE1.length());// 100
//
//		final ConcurrentMap<Integer, Integer> numMap = new ConcurrentHashMap<Integer, Integer>();
//		for (int i = 0; i < 100000; i++) {
//			numMap.put(i, i);
//		}
//
//		System.out.println(numMap.size());
//		int i = 0;
//		for (ConcurrentMap.Entry<Integer, Integer> entry : numMap.entrySet()) {
//			numMap.remove(entry.getKey());
//			i++;
//		}
//		System.out.println(numMap.size());

		// System.out.println(numMap.size());
		// System.out.println(numMap.size());
		// int ran = RandomUtils.nextInt(0, numMap.size() - 1);
		// Integer integer = numMap.get(ran);
		// numMap.remove(ran);
		// ran = RandomUtils.nextInt(0, numMap.size() - 1);
		// integer = numMap.get(ran);
		// numMap.remove(ran);
		// System.out.println(numMap.size());
//	}

}
