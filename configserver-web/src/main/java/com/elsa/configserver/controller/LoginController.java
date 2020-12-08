package com.elsa.configserver.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import com.elsa.configserver.client.Base;
import com.elsa.configserver.client.TestHessian;
import com.elsa.configserver.constant.UsersConstant;
import com.elsa.configserver.domain.UsersDO;
import com.elsa.configserver.service.AppFileNameService;
import com.elsa.configserver.service.AppNameService;
import com.elsa.configserver.service.OpraterLogService;
import com.elsa.configserver.service.PropConfService;
import com.elsa.configserver.service.UsersService;
import com.elsa.configserver.util.ConfigUtil;

@Controller
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class LoginController extends BaseController {

	@Autowired
	AppNameService appNameService;

	@Autowired
	UsersService usersService;

	@Autowired
	AppFileNameService appFileNameService;

	@Autowired
	ConfigUtil configUtil;

	@Autowired
	PropConfService propConfService;

	@Autowired
	OpraterLogService opraterLogService;

	/*@Autowired
	Base baseBean;*/

/*	@Autowired
	TestHessian testHessianBean;*/

	@RequestMapping(value = "/dfs/list", method = RequestMethod.GET)
	public String dfs_list(final ModelMap model, WebRequest request) {
		model.put("dfsList", FileUploadController.CURRENT_MAP.values());
		return "dfsList";
	}

	@RequestMapping(value = "/dfs/rand", method = RequestMethod.GET)
	public String dfs_rand(final ModelMap model, WebRequest request) {
		int size = FileUploadController.CURRENT_MAP.size();
		Random rand = new Random();
		int index = rand.nextInt(size);

		List<String> uploadList = new ArrayList<String>();
		uploadList.addAll(FileUploadController.CURRENT_MAP.values());
		List<String> list = new ArrayList<String>();
		if (size > 0) {
			list.add(uploadList.get(index));
		}
		model.put("dfsList", list);
		return "dfsList";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(final ModelMap model, WebRequest request) {
		// testHessianBean.get_test_hessian_str();
		// baseBean.get_test_hessian_str_new();
		// String password = "pwd";
		// String username = "admin";
		// UsersDO usersDO = new UsersDO();
		// String passwordToMD5OrSHA = Md5Util.md5pwd(password);
		// usersDO.setUsername(username);
		// usersDO.setPassword(passwordToMD5OrSHA);
		//
		// List<UsersDO> userList = usersService.selectDynamic(usersDO);

		return "login";
	}

	@RequestMapping(value = "/login_in", method = RequestMethod.POST)
	public String login_in(final ModelMap model, WebRequest request, HttpSession session) {

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		if (StringUtils.isBlank(username)) {
			return "redirect:/config/tl_list";
		}
		if (StringUtils.isBlank(password)) {
			return "redirect:/config/tl_list";
		}
		String passwordToMD5OrSHA = Md5Util.md5pwd(password);
		UsersDO usersDO = new UsersDO();
		usersDO.setUsername(username);
		usersDO.setPassword(passwordToMD5OrSHA);

		List<UsersDO> userList = usersService.selectDynamic(usersDO);

		if (!CollectionUtils.isEmpty(userList)) {
			UsersDO user = userList.get(0);
			session.setAttribute(UsersConstant.SESSION_USER_ID, String.valueOf(user.getId()));
			session.setAttribute(UsersConstant.SESSION_USER_NAME, user.getUsername());
			session.setAttribute(UsersConstant.SESSION_USER_ROLE, user.getRole());
			return "redirect:/main";
		} else {
			return "redirect:/login";
		}
	}

	@RequestMapping(value = "/login_out", method = RequestMethod.GET)
	public String login_out(final ModelMap model, WebRequest request, HttpSession session) {

		session.removeAttribute(UsersConstant.SESSION_USER_ID);
		session.removeAttribute(UsersConstant.SESSION_USER_NAME);
		session.removeAttribute(UsersConstant.SESSION_USER_ROLE);

		return "redirect:/login";
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(final ModelMap model, WebRequest request, HttpSession session) {
		return "index";
	}

	@RequestMapping(value = "/left", method = RequestMethod.GET)
	public String left(final ModelMap model, WebRequest request, HttpSession session) {
		return "left";
	}

	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String main(final ModelMap model, WebRequest request, HttpSession session) {
		String role = (String) session.getAttribute(UsersConstant.SESSION_USER_ROLE);
		if("tl".equals(role)){
			return "redirect:/config/tl_list";
		}
		if("opm".equals(role)){
			return "redirect:/opm/opm_list";
		}
		if("admin".equals(role)){
			return "redirect:/appName/list";
		}
		
		return "redirect:/config/tl_list";
	}

	@RequestMapping(value = "/applylist", method = RequestMethod.GET)
	public String applylist(final ModelMap model, WebRequest request, HttpSession session) {
		return "applylist";
	}

	@RequestMapping(value = "/top", method = RequestMethod.GET)
	public String top(final ModelMap model, WebRequest request, HttpSession session) {
		return "top";
	}

}
