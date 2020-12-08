package com.elsa.configserver.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import com.elsa.configserver.constant.UsersConstant;
import com.elsa.configserver.domain.AppNameDO;
import com.elsa.configserver.domain.UsersDO;
import com.elsa.configserver.service.AppNameService;
import com.elsa.configserver.service.UsersService;

@Controller
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class UsersController extends BaseController {

	@Autowired
	UsersService usersService;

	@Autowired
	AppNameService appNameService;

	@RequestMapping(value = "/users/add", method = RequestMethod.GET)
	public String add(final ModelMap model, HttpSession session) {
		String user_id = (String) session.getAttribute(UsersConstant.SESSION_USER_ID);
		String role = (String) session.getAttribute(UsersConstant.SESSION_USER_ROLE);
		if (StringUtils.isBlank(user_id)) {
			return "redirect:/login";
		}
		if (!role.equals(UsersConstant.ROLE_ADMIN)) {
			return "redirect:/login";
		}
		AppNameDO appNameDO = new AppNameDO();
		appNameDO.setIsDelete(false);
		List<AppNameDO> appNames = appNameService.selectDynamic(appNameDO);
		model.addAttribute("appNames", appNames);
		return "users/add";
	}

	@RequestMapping(value = "/users/edit/{id}", method = RequestMethod.GET)
	public String edit(final ModelMap model, @PathVariable Long id, HttpSession session) {
		String user_id = (String) session.getAttribute(UsersConstant.SESSION_USER_ID);
		String role = (String) session.getAttribute(UsersConstant.SESSION_USER_ROLE);
		if (StringUtils.isBlank(user_id)) {
			return "redirect:/login";
		}
		if (!role.equals(UsersConstant.ROLE_ADMIN)) {
			return "redirect:/login";
		}

		AppNameDO appNameDO = new AppNameDO();
		appNameDO.setIsDelete(false);
		List<AppNameDO> appNames = appNameService.selectDynamic(appNameDO);
		UsersDO selectById = usersService.selectById(id);
		String appIds = selectById.getAppIds();
		String[] split = appIds.split(",");
		for (int i = 0; i < split.length; i++) {
			String s = split[i];
			if (StringUtils.isNotBlank(s)) {
				for (AppNameDO app : appNames) {
					if (StringUtils.isNumeric(s.trim()) && app.getId().intValue() == Integer.valueOf(s.trim())) {
						app.setChecked(1);
					}
				}
			}
		}

		model.addAttribute("appNames", appNames);
		model.addAttribute("m_user_id", id);
		return "users/edit";
	}

	@RequestMapping(value = "/users/edit_pwd", method = RequestMethod.GET)
	public String editPwd(final ModelMap model, HttpSession session) {
		String user_id = (String) session.getAttribute(UsersConstant.SESSION_USER_ID);
		String username = (String) session.getAttribute(UsersConstant.SESSION_USER_NAME);
		if (StringUtils.isBlank(user_id)) {
			return "redirect:/login";
		}
		model.addAttribute("username", username);
		model.addAttribute("success", 0);
		return "users/edit_pwd";
	}

	@RequestMapping(value = "/users/update_pwd", method = RequestMethod.POST)
	public String updatePwd(final ModelMap model, HttpServletRequest request, HttpSession session) {
		String user_id = (String) session.getAttribute(UsersConstant.SESSION_USER_ID);
		String username = (String) session.getAttribute(UsersConstant.SESSION_USER_NAME);
		if (StringUtils.isBlank(user_id)) {
			return "redirect:/login";
		}
		String old_password = request.getParameter("old_password");
		String valid_password = request.getParameter("valid_password");
		String password = request.getParameter("password");
		if (password.equals(valid_password)) {
			UsersDO user = new UsersDO();
			user.setUsername(username);
			user.setPassword(Md5Util.md5pwd(old_password));
			List<UsersDO> userList = usersService.selectDynamic(user);
			if (!userList.isEmpty() && userList.size() > 0) {
				UsersDO updateUser = new UsersDO();
				updateUser.setUsername(username);
				String md5pwd = Md5Util.md5pwd(valid_password);
				updateUser.setPassword(md5pwd);
				updateUser.setId(userList.get(0).getId());
				usersService.update(updateUser, false);
				model.addAttribute("success", 1);
			} else {
				model.addAttribute("success", 2);
			}
		}
		return "users/edit_pwd";
	}

	@RequestMapping(value = "/users/update", method = RequestMethod.POST)
	public String update(final ModelMap model, WebRequest request, HttpSession session) {
		String user_id = (String) session.getAttribute(UsersConstant.SESSION_USER_ID);
		String sessionRole = (String) session.getAttribute(UsersConstant.SESSION_USER_ROLE);
		if (StringUtils.isBlank(user_id)) {
			return "redirect:/login";
		}
		if (!sessionRole.equals(UsersConstant.ROLE_ADMIN)) {
			return "redirect:/login";
		}
		String modify_user_id = request.getParameter("m_user_id");
		Map<String, String[]> appIds = request.getParameterMap();
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String[]> entry : appIds.entrySet()) {
			if (entry.getKey().startsWith("app_ids_")) {
				String app_id = request.getParameter(entry.getKey());
				if (StringUtils.isNotBlank(app_id)) {
					sb.append(app_id.trim());
					sb.append(",");
				}
			}

		}
		String ids = sb.toString();
		if (StringUtils.isNotBlank(ids)) {
			ids = ids.substring(0, ids.length() - 1);
		}
		if (StringUtils.isNotBlank(modify_user_id)) {
			UsersDO users = new UsersDO();
			users.setId(Integer.valueOf(modify_user_id.trim()));
			users.setAppIds(ids);
			users.setModifyTime(new Date());
			usersService.update(users, false);
		}

		return "redirect:/users/list";
	}

	@RequestMapping(value = "/users/save", method = RequestMethod.POST)
	public String save(final ModelMap model, WebRequest request, HttpSession session) {
		String user_id = (String) session.getAttribute(UsersConstant.SESSION_USER_ID);
		String sessionRole = (String) session.getAttribute(UsersConstant.SESSION_USER_ROLE);
		if (StringUtils.isBlank(user_id)) {
			return "redirect:/login";
		}
		if (!sessionRole.equals(UsersConstant.ROLE_ADMIN)) {
			return "redirect:/login";
		}
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String valid_password = request.getParameter("valid_password");
		String role = request.getParameter("role");
		Map<String, String[]> appIds = request.getParameterMap();
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String[]> entry : appIds.entrySet()) {
			if (entry.getKey().startsWith("app_ids_")) {
				String app_id = request.getParameter(entry.getKey());
				if (StringUtils.isNotBlank(app_id)) {
					sb.append(app_id);
					sb.append(",");
				}
			}

		}
		String ids = sb.toString();
		if (StringUtils.isNotBlank(ids)) {
			ids = ids.substring(0, ids.length() - 1);
		}
		if (password.equals(valid_password) && StringUtils.isNotBlank(username.trim())) {
			String md5_pwd = Md5Util.md5pwd(password);
			UsersDO users = new UsersDO();
			users.setPassword(md5_pwd);
			users.setUsername(username);
			users.setAppIds(ids);
			users.setRole(role);
			users.setCreateTime(new Date());
			users.setModifyTime(new Date());
			users.setIsDelete(false);
			usersService.insert(users);
		}

		return "redirect:/users/list";
	}

	@RequestMapping(value = "/users/list")
	public String list(final ModelMap model, WebRequest request, HttpSession session) {
		String user_id = (String) session.getAttribute(UsersConstant.SESSION_USER_ID);
		String role = (String) session.getAttribute(UsersConstant.SESSION_USER_ROLE);
		if (StringUtils.isBlank(user_id)) {
			return "redirect:/login";
		}
		if (!role.equals(UsersConstant.ROLE_ADMIN)) {
			return "redirect:/login";
		}
		UsersDO u = new UsersDO();
		List<UsersDO> userList = usersService.selectDynamic(u);
		if (!CollectionUtils.isEmpty(userList)) {
			AppNameDO appNameDO = new AppNameDO();
			List<AppNameDO> appNames = appNameService.selectDynamic(appNameDO);
			for (UsersDO usersDO : userList) {
				if (!CollectionUtils.isEmpty(appNames)) {
					setUser(usersDO, appNames);
				}
			}
		}
		model.addAttribute("userList", userList);
		return "users/list";
	}

	private void setUser(UsersDO usersDO, List<AppNameDO> appNames) {
		if (StringUtils.isNotBlank(usersDO.getRole())) {
			if (usersDO.getRole().equals(UsersConstant.ROLE_TL)) {
				String appIds = usersDO.getAppIds();
				if (StringUtils.isNotBlank(appIds)) {
					StringBuilder sb = new StringBuilder();
					String ids[] = appIds.split(",");
					for (String id : ids) {
						for (AppNameDO app : appNames) {
							String appName = "";
							if (id.equals(String.valueOf(app.getId()))) {
								appName = app.getAppName();
								sb.append(appName);
								sb.append(",");
								break;
							}
						}
						if (StringUtils.isNotBlank(sb.toString())) {
							usersDO.setOperatorApp(sb.substring(0, sb.toString().length() - 1));
						}
					}
				}
			}
		}
	}

}
