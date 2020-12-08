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
import com.elsa.configserver.service.AppFileNameService;
import com.elsa.configserver.service.AppNameService;

@Controller
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class AppFileNameController extends BaseController {

	@Autowired
	AppFileNameService appFileNameService;

	@Autowired
	AppNameService appNameService;

	@RequestMapping(value = "/appFileName/add", method = RequestMethod.POST)
	public String add(final ModelMap model, WebRequest request, HttpSession session) {
		String user_id = (String) session.getAttribute(UsersConstant.SESSION_USER_ID);
		String role = (String) session.getAttribute(UsersConstant.SESSION_USER_ROLE);
		if (StringUtils.isBlank(user_id)) {
			return "redirect:/login";
		}
		if (!role.equals(UsersConstant.ROLE_ADMIN)) {
			return "redirect:/login";
		}
		String propFileName = request.getParameter("app_file_name");
		String appId = request.getParameter("app_id");
		if (StringUtils.isBlank(propFileName)) {
			model.addAttribute("error", "文件名不能为空");
			return "redirect:/appFileName/list/" + appId;
		}
		if (propFileName.startsWith("/")) {
			model.addAttribute("error", "文件名不能为以/开头");
			return "redirect:/appFileName/list/" + appId;
		}
		if (StringUtils.isBlank(appId)) {
			model.addAttribute("error", "app_id不能为空");
			return "redirect:/appFileName/list/" + appId;
		}
		AppFileNameDO query = new AppFileNameDO();
		query.setPropFileName(propFileName.trim());
		query.setAppId(Integer.valueOf(appId));
		Long count = appFileNameService.selectCountDynamic(query);

		if (null != count && count.intValue() == 0) {
			AppFileNameDO appFileNameDO = new AppFileNameDO();
			appFileNameDO.setPropFileName(propFileName);
			appFileNameDO.setAppId(Integer.valueOf(appId));
			appFileNameDO.setIsDelete(false);
			appFileNameDO.setCreateTime(new Date());
			appFileNameDO.setModifyTime(new Date());
			appFileNameService.insert(appFileNameDO);
		}
		return "redirect:/appFileName/list/" + appId;
	}

	@RequestMapping(value = "/appFileName/update/{fileId}", method = RequestMethod.POST)
	public String update(final ModelMap model, WebRequest request, @PathVariable Integer fileId, HttpSession session) {
		String user_id = (String) session.getAttribute(UsersConstant.SESSION_USER_ID);
		String role = (String) session.getAttribute(UsersConstant.SESSION_USER_ROLE);
		if (StringUtils.isBlank(user_id)) {
			return "redirect:/login";
		}
		if (!role.equals(UsersConstant.ROLE_ADMIN)) {
			return "redirect:/login";
		}
		String appId = request.getParameter("app_id");
		String propFileName = request.getParameter("propFileName");

		if (StringUtils.isBlank(propFileName)) {
			model.addAttribute("error", "文件名不能为空");
			return "redirect:/appFileName/list/" + appId;
		}
		if (StringUtils.isBlank(appId)) {
			model.addAttribute("error", "app_id不能为空");
			return "redirect:/appFileName/list/" + appId;
		}
		AppFileNameDO query = new AppFileNameDO();
		query.setPropFileName(propFileName.trim());
		query.setAppId(Integer.valueOf(appId));
		Long count = appFileNameService.selectCountDynamic(query);
		if (null != count && count.intValue() == 0) {
			AppFileNameDO appFileNameDO = appFileNameService.selectById(fileId.longValue());
			appFileNameDO.setPropFileName(propFileName);
			appFileNameDO.setModifyTime(new Date());
			appFileNameService.update(appFileNameDO, false);
		}
		return "redirect:/appFileName/list/" + appId;
	}

	@RequestMapping(value = "/appFileName/list/{appId}")
	public String list(final ModelMap model, @PathVariable Integer appId, WebRequest request, HttpSession session) {
		String user_id = (String) session.getAttribute(UsersConstant.SESSION_USER_ID);
		String role = (String) session.getAttribute(UsersConstant.SESSION_USER_ROLE);
		if (StringUtils.isBlank(user_id)) {
			return "redirect:/login";
		}
		if (!role.equals(UsersConstant.ROLE_ADMIN)) {
			return "redirect:/login";
		}
		AppFileNameDO appFileNameDO = new AppFileNameDO();
		appFileNameDO.setAppId(appId);
		List<AppFileNameDO> list = appFileNameService.selectDynamic(appFileNameDO);

		AppNameDO appNameDO = appNameService.selectById(appId.longValue());
		model.addAttribute("appFileNames", list);
		model.addAttribute("appId", appId);
		model.addAttribute("appNameDO", appNameDO);
		return "appFileName/list";
	}

}
