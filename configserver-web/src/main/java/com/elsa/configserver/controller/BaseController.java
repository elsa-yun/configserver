package com.elsa.configserver.controller;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.WebRequest;

import com.elsa.configserver.constant.UsersConstant;
import com.elsa.configserver.util.ConfigUtil;

public class BaseController {

	@Autowired
	ConfigUtil configUtil;

	@ModelAttribute
	public void ajaxAttribute(WebRequest request, Model model, HttpSession session) {
		String role = (String) session.getAttribute(UsersConstant.SESSION_USER_ROLE);
		String user_name = (String) session.getAttribute(UsersConstant.SESSION_USER_NAME);

		if (StringUtils.isBlank(role)) {
			model.addAttribute("user_role", "");
		}
		model.addAttribute("userName", user_name);
		model.addAttribute("user_role", role);
		String contextPath = request.getContextPath();
		if (!contextPath.endsWith("/")) {
			contextPath = contextPath + "/";
		}
		model.addAttribute("localDomain", configUtil.getLocalDomain());
		model.addAttribute("contextPath", contextPath);
	}

}
