package com.elsa.configserver.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.elsa.configserver.domain.CityDO;
import com.elsa.configserver.service.CityService;

@Controller
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class CityController {

	private static final String CART_ITEMS = "cart_items";

	private static final Log logger = LogFactory.getLog(CityController.class);

	private static final AtomicLong count = new AtomicLong();

	@Autowired
	private CityService cityService;

	//
	// @RequestMapping(value = "/city/add", method = RequestMethod.GET)
	// public String add(final ModelMap model) {
	// CityDO cityDO = new CityDO();
	// model.addAttribute("city", cityDO);
	// return "city/add";
	// }
	//
	// @RequestMapping(value = "/city/edit/{id}", method = RequestMethod.GET)
	// public String edit(final ModelMap model, @PathVariable Long id) {
	// CityDO cityDO = cityService.selectById(id);
	// model.addAttribute("city", cityDO);
	// return "city/edit";
	// }
	//
	// @RequestMapping(value = "/city/view/{id}", method = RequestMethod.GET)
	// public String view(final ModelMap model, @PathVariable Long id) {
	// CityDO cityDO = cityService.selectById(id);
	// model.addAttribute("city", cityDO);
	// return "city/view";
	// }
	//
	// @RequestMapping(value = "/city/save", method = RequestMethod.POST)
	// public String save(@ModelAttribute("city") @Valid CityDO city,
	// BindingResult result) {
	// if (result.hasErrors()) {
	// return null;
	// }
	// cityService.insert(city);
	// return "redirect:/city/list";
	// }
	//
	// @RequestMapping(value = "/city/update/{id}", method = RequestMethod.PUT)
	// public String update(@ModelAttribute("city") @Valid CityDO city,
	// BindingResult result, @PathVariable Integer id) {
	// if (result.hasErrors()) {
	// return null;
	// }
	// city.setId(id);
	// cityService.update(city, true);
	// return "redirect:/city/list";
	// }
	//
	// @RequestMapping(value = "/city/delete/{id}", method =
	// RequestMethod.DELETE)
	// public String delete(@PathVariable Long id) {
	// cityService.deleteById(id);
	// return "redirect:/city/list";
	// }

	@RequestMapping(value = "/city/list")
	public String list(final ModelMap model, HttpServletRequest request) {
		// ExecutorService es = Executors.newFixedThreadPool(10);
		// List<Thread> list = new ArrayList<Thread>();
		// for (int i = 0; i < 1; i++) {
		// Thread t = new Thread(new Runnable() {
		// public void run() {
		//
		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
		// });
		// list.add(t);
		//
		// }
		//
		// for (int i = 0; i < list.size(); i++) {
		// list.get(i).start();
		// }
		// es.shutdown();

		CityDO select = cityService.select(1);
		logger.debug("city=========>" + select);
		Long count1 = count.addAndGet(1);
		// CityDO page1 = cityService.select(1);
		// CityDO page = cityService.transactional(1, count1.intValue());
		// CityDO page2 = cityService.select(1);
		// logger.debug("name=>" + page.getName());
//		HttpSession session = request.getSession();
//		session.setAttribute(CART_ITEMS, "longhaisheng_session_test");
//		session.setAttribute("cart_price", "1000000");
//		model.addAttribute("count1", count1);
//		model.addAttribute("attribute", session.getAttribute(CART_ITEMS));
//		
//		
//		Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
//		Cookie[] cookies = request.getCookies();
//		if (null != cookies) {
//			for (Cookie cookie : cookies) {
//				cookieMap.put(cookie.getName(), cookie);
//			}
//		}
//
//		Cookie cookie = getCookieByName(request, "cart_price");
//		String value = cookie.getValue();
//		logger.error("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::>>>>>>>>>>>>>" + cookie.getValue());
		
		return "city/list";
	}

	@RequestMapping(value = "/city/view")
	public String view(final ModelMap model, HttpServletRequest request) {
		CityDO select = cityService.select(1);
		logger.debug("city=========>" + select);
		Long count1 = count.addAndGet(1);

		HttpSession session = request.getSession();
		Object attribute = session.getAttribute(CART_ITEMS);
		model.addAttribute("attribute", attribute);
		model.addAttribute("count1", count1);

		Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie);
			}
		}

		Cookie cookie = getCookieByName(request, "cart_price");
		String value = cookie.getValue();
		logger.error("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::>>>>>>>>>>>>>" + cookie.getValue());

		return "city/list";
	}

	public static Cookie getCookieByName(HttpServletRequest request, String name) {
		Map<String, Cookie> cookieMap = ReadCookieMap(request);
		if (cookieMap.containsKey(name)) {
			Cookie cookie = (Cookie) cookieMap.get(name);
			return cookie;
		} else {
			return null;
		}
	}

	private static Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {
		Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie);
			}
		}
		return cookieMap;
	}

}
