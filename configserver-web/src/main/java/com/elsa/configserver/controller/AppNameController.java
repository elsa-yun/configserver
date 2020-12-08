package com.elsa.configserver.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import com.elsa.configserver.constant.UsersConstant;
import com.elsa.configserver.domain.AppFileNameDO;
import com.elsa.configserver.domain.AppNameDO;
import com.elsa.configserver.domain.PropConfDO;
import com.elsa.configserver.service.AppFileNameService;
import com.elsa.configserver.service.AppNameService;
import com.elsa.configserver.service.PropConfService;

@Controller
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class AppNameController extends BaseController {

	@Autowired
	AppNameService appNameService;

	@Autowired
	AppFileNameService appFileNameService;

	@Autowired
	PropConfService propConfService;

	@RequestMapping(value = "/appName/add", method = RequestMethod.POST)
	public String add(final ModelMap model, WebRequest request, HttpSession session) {
		String user_id = (String) session.getAttribute(UsersConstant.SESSION_USER_ID);
		String role = (String) session.getAttribute(UsersConstant.SESSION_USER_ROLE);
		if (StringUtils.isBlank(user_id)) {
			return "redirect:/login";
		}
		if (!role.equals(UsersConstant.ROLE_ADMIN)) {
			return "redirect:/login";
		}
		String appName = request.getParameter("app_name");
		if (StringUtils.isBlank(appName)) {
			model.addAttribute("error", "应用名不能为空");
			return "redirect:/appName/list";
		}

		AppNameDO queryDO = new AppNameDO();
		queryDO.setAppName(appName.trim());
		Long count = appNameService.selectCountDynamic(queryDO);
		if (null != count && count.intValue() == 0) {
			AppNameDO appNameDO = new AppNameDO();
			appNameDO.setAppName(appName.trim());
			appNameDO.setIsDelete(false);
			appNameDO.setCreateTime(new Date());
			appNameDO.setModifyTime(new Date());
			appNameService.insert(appNameDO);
		}
		return "redirect:/appName/list";
	}

	@RequestMapping(value = "/appName/update/{id}", method = RequestMethod.POST)
	public String update(final ModelMap model, @PathVariable Integer id, WebRequest request, HttpSession session) {
		String user_id = (String) session.getAttribute(UsersConstant.SESSION_USER_ID);
		String role = (String) session.getAttribute(UsersConstant.SESSION_USER_ROLE);
		if (StringUtils.isBlank(user_id)) {
			return "redirect:/login";
		}
		if (!role.equals(UsersConstant.ROLE_ADMIN)) {
			return "redirect:/login";
		}
		AppNameDO appNameDO = appNameService.selectById(id.longValue());
		String appName = request.getParameter("app_name");
		if (StringUtils.isBlank(appName)) {
			model.addAttribute("error", "应用名不能为空");
			return "redirect:/appName/list";
		}
		AppNameDO queryDO = new AppNameDO();
		queryDO.setAppName(appName.trim());
		Long count = appNameService.selectCountDynamic(queryDO);
		if (null != count && count.intValue() == 0) {
			appNameDO.setAppName(appName.trim());
			appNameDO.setModifyTime(new Date());
			appNameService.update(appNameDO, false);
		}
		return "redirect:/appName/list";
	}

	@RequestMapping(value = "/appName/list")
	public String list(final ModelMap model, WebRequest request, HttpSession session) {
		String user_id = (String) session.getAttribute(UsersConstant.SESSION_USER_ID);
		String role = (String) session.getAttribute(UsersConstant.SESSION_USER_ROLE);
		if (StringUtils.isBlank(user_id)) {
			return "redirect:/login";
		}
		if (!role.equals(UsersConstant.ROLE_ADMIN)) {
			return "redirect:/login";
		}
		AppNameDO appNameDO = new AppNameDO();
		List<AppNameDO> appList = appNameService.selectDynamic(appNameDO);
		String error = request.getParameter("error");
		if (StringUtils.isNotBlank(error)) {
			model.addAttribute("error", error);
		}
		model.addAttribute("appNames", appList);
		return "appName/list";
	}

	@RequestMapping(value = "/appName/del/{id}")
	public String del(final ModelMap model, @PathVariable Integer id, WebRequest request, HttpSession session) {
		String user_id = (String) session.getAttribute(UsersConstant.SESSION_USER_ID);
		String role = (String) session.getAttribute(UsersConstant.SESSION_USER_ROLE);
		if (StringUtils.isBlank(user_id)) {
			return "redirect:/login";
		}
		if (!role.equals(UsersConstant.ROLE_ADMIN)) {
			return "redirect:/login";
		}
		AppNameDO appNameDO = new AppNameDO();
		appNameDO.setId(id);
		appNameDO.setIsDelete(true);
		appNameDO.setModifyTime(new Date());
		appNameService.update(appNameDO, false);

		AppFileNameDO appFileNameDO = new AppFileNameDO();
		appFileNameDO.setAppId(id);
		List<AppFileNameDO> selectDynamic = appFileNameService.selectDynamic(appFileNameDO);
		for (AppFileNameDO af : selectDynamic) {
			af.setIsDelete(true);
			af.setModifyTime(new Date());
			appFileNameService.update(af, false);
		}

		PropConfDO propConfDO = new PropConfDO();
		propConfDO.setAppId(id);
		List<PropConfDO> selectDynamic2 = propConfService.selectDynamic(propConfDO);

		for (PropConfDO pcd : selectDynamic2) {
			pcd.setStatus(2);
			pcd.setModifyTime(new Date());
			propConfService.update(pcd, false);

		}

		String error = request.getParameter("error");
		if (StringUtils.isNotBlank(error)) {
			model.addAttribute("error", error);
		}
		return "redirect:/appName/list";
	}
}
