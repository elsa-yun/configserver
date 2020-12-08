package com.elsa.configserver.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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

import com.elsa.configserver.constant.OpraterLogConstant;
import com.elsa.configserver.constant.PropConfConstant;
import com.elsa.configserver.constant.UsersConstant;
import com.elsa.configserver.domain.AppFileNameDO;
import com.elsa.configserver.domain.AppNameDO;
import com.elsa.configserver.domain.OpraterLogDO;
import com.elsa.configserver.domain.PropConfDO;
import com.elsa.configserver.service.AppFileNameService;
import com.elsa.configserver.service.AppNameService;
import com.elsa.configserver.service.OpraterLogService;
import com.elsa.configserver.service.PropConfService;
import com.elsa.configserver.service.UsersService;
import com.elsa.configserver.util.ConfigUtil;

@Controller
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class OpmController extends BaseController {

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

	@RequestMapping(value = "/opm/opm_list", method = RequestMethod.GET)
	public String list(final ModelMap model, WebRequest request, HttpSession session) {
		String user_id = (String) session.getAttribute(UsersConstant.SESSION_USER_ID);
		String role = (String) session.getAttribute(UsersConstant.SESSION_USER_ROLE);
		if (StringUtils.isBlank(user_id)) {
			return "redirect:/login";
		}
		if (!role.equals(UsersConstant.ROLE_OPM)) {
			return "redirect:/login";
		}

		String app_id = (String) request.getParameter("app_id");
		String environment = (String) request.getParameter("environment");
		String is_pass = (String) request.getParameter("is_pass");
		model.addAttribute("app_id", app_id);
		model.addAttribute("environment", environment);
		model.addAttribute("is_pass", is_pass);

		AppNameDO appNameDO = new AppNameDO();
		appNameDO.setIsDelete(false);
		List<AppNameDO> appNames = appNameService.selectDynamic(appNameDO);

		PropConfDO confDO = new PropConfDO();
		if (StringUtils.isNotBlank(app_id)) {
			confDO.setAppId(Integer.valueOf(app_id.trim()));
		}
		if (StringUtils.isNotBlank(environment)) {
			confDO.setEnvironment(environment.trim());
		}
		if (StringUtils.isNotBlank(is_pass)) {
			if (is_pass.equals("yes")) {
				confDO.setStatus(PropConfConstant.STATUS_PASS);
			}
			if (is_pass.equals("no")) {
				confDO.setStatus(PropConfConstant.STATUS_NOT_PASS);
			}
			if (is_pass.equals("default")) {
				confDO.setStatus(PropConfConstant.STATUS_DEFAULT);
			}
		}
		List<PropConfDO> propConfList = propConfService.selectDynamic(confDO);

		AppFileNameDO appFileNameDO = new AppFileNameDO();
		List<AppFileNameDO> appFileNameList = appFileNameService.selectDynamic(appFileNameDO);

		if (!CollectionUtils.isEmpty(appNames) && !CollectionUtils.isEmpty(propConfList) && !CollectionUtils.isEmpty(appFileNameList)) {
			for (PropConfDO prop : propConfList) {
				for (AppFileNameDO fileName : appFileNameList) {
					if (prop.getAppFileId().intValue() == fileName.getId().intValue()) {
						prop.setAppFileNameDO(fileName);
						break;
					}
				}

				for (AppNameDO app : appNames) {
					if (app.getId().intValue() == prop.getAppId().intValue()) {
						prop.setAppNameDO(app);
						break;
					}
				}
			}
		}

		sortStringList(appNames);
		List<String> environments = new ArrayList<String>();
		environments.add(PropConfConstant.ENVIRONMENT_PRODUCE);
		environments.add(PropConfConstant.ENVIRONMENT_STAGING);
		environments.add(PropConfConstant.ENVIRONMENT_TEST);

		model.addAttribute("environments", environments);
		model.addAttribute("appNames", appNames);
		model.addAttribute("propConfList", propConfList);

		return "opm/opm_list";
	}
	
	private static void sortStringList(List<AppNameDO> stringList) {
		Collections.sort(stringList, new Comparator<AppNameDO>() {
			public int compare(AppNameDO o1, AppNameDO o2) {
				String str1 = (String) o1.getAppName();
				String str2 = (String) o2.getAppName();
				return str1.compareTo(str2);
			}
		});
	}

	@RequestMapping(value = "/opm/opm_edit/{propId}", method = RequestMethod.GET)
	public String edit(final ModelMap model, @PathVariable Integer propId, WebRequest request, HttpSession session) {
		String user_id = (String) session.getAttribute(UsersConstant.SESSION_USER_ID);
		String role = (String) session.getAttribute(UsersConstant.SESSION_USER_ROLE);
		if (StringUtils.isBlank(user_id)) {
			return "redirect:/login";
		}
		if (!role.equals(UsersConstant.ROLE_OPM)) {
			return "redirect:/login";
		}
		PropConfDO propConfDO = propConfService.selectById(propId.longValue());
		AppNameDO appName = appNameService.selectById(propConfDO.getAppId().longValue());
		AppFileNameDO appFileNameDO = appFileNameService.selectById(propConfDO.getAppFileId().longValue());

		OpraterLogDO opraterLog = getLastOpratorLog(propId);

		model.addAttribute("opraterLog", opraterLog);

		model.addAttribute("appNameDO", appName);
		model.addAttribute("appFileNameDO", appFileNameDO);
		model.addAttribute("propConfDO", propConfDO);

		return "opm/opm_edit";
	}

	private OpraterLogDO getLastOpratorLog(Integer propId) {
		OpraterLogDO opraterLogDO = new OpraterLogDO();
		opraterLogDO.setPropConfId(propId);
		opraterLogDO.setType(String.valueOf(OpraterLogConstant.TYPE_ADUIT));
		List<OpraterLogDO> opraterLogs = opraterLogService.selectDynamic(opraterLogDO);
		OpraterLogDO opraterLog = null;
		if (!opraterLogs.isEmpty()) {
			opraterLog = opraterLogs.get(0);
		} else {
			opraterLog = new OpraterLogDO();
		}
		return opraterLog;
	}

	@RequestMapping(value = "/opm/opm_update", method = RequestMethod.POST)
	public String update(final ModelMap model, WebRequest request, HttpSession session) {

		String user_id = (String) session.getAttribute(UsersConstant.SESSION_USER_ID);
		String role = (String) session.getAttribute(UsersConstant.SESSION_USER_ROLE);
		if (StringUtils.isBlank(user_id)) {
			return "redirect:/login";
		}
		if (!role.equals(UsersConstant.ROLE_OPM)) {
			return "redirect:/login";
		}

		OpraterLogDO opraterLogDO = new OpraterLogDO();

		String propId = request.getParameter("prop_id");
		PropConfDO propConfDO = propConfService.selectById(Long.valueOf(propId));
		Integer oldStatus = propConfDO.getStatus();
		if (oldStatus.intValue() == PropConfConstant.STATUS_NOT_PASS) {
			return "redirect:/opm/opm_list";
		}

		String request_content = request.getParameter("content").trim();
		String is_pass = request.getParameter("is_pass");

		Integer appFileId = propConfDO.getAppFileId();
		Integer appId = propConfDO.getAppId();
		String environment = propConfDO.getEnvironment();

		String beforeContent = propConfDO.getContent();
		opraterLogDO.setBeforeContent(beforeContent);
		PropConfDO propConf = getProp(appFileId, appId, environment);
		if (null != propConf) {
			opraterLogDO.setBeforeContent(propConf.getContent());
		}

		if (is_pass.equals("yes")) {
			if (oldStatus.intValue() == PropConfConstant.STATUS_DEFAULT) {
				propConfDO.setStatus(PropConfConstant.STATUS_PASS);
			}

			if (null != propConf) {
				if (oldStatus.intValue() == PropConfConstant.STATUS_DEFAULT) {
					propConfDO.setContent(propConf.getContent().trim() + "\r\n" + request_content);
					opraterLogDO.setAfterContent(propConf.getContent().trim() + "\r\n" + "###########after change#############\r\n"
							+ request_content);
				}
				if (oldStatus.intValue() == PropConfConstant.STATUS_PASS) {
					propConfDO.setContent(request_content);
					opraterLogDO.setAfterContent("###########after change#############\r\n" + request_content);
				}
			} else {
				propConfDO.setContent(request_content);
				opraterLogDO.setAfterContent("###########after change#############\r\n" + request_content);
			}

		}
		if (is_pass.equals("no")) {
			propConfDO.setStatus(PropConfConstant.STATUS_NOT_PASS);

			opraterLogDO.setBeforeContent(beforeContent + "before status=>" + oldStatus);
			opraterLogDO.setAfterContent(request_content + "after status=>" + PropConfConstant.STATUS_NOT_PASS);
		}
		propConfDO.setModifyTime(new Date());
		int update = propConfService.update(propConfDO, false);
		if (update > 0) {
			Object sessionUserName = session.getAttribute(UsersConstant.SESSION_USER_NAME);

			opraterLogDO.setOperator(null == sessionUserName ? "" : sessionUserName.toString());
			opraterLogDO.setAppId(appId);
			opraterLogDO.setAppFileId(appFileId > 0 ? appFileId : 0);
			opraterLogDO.setEnvironment(environment);
			opraterLogDO.setCreateTime(new Date());
			opraterLogDO.setType(String.valueOf(OpraterLogConstant.TYPE_ADUIT));
			opraterLogDO.setPropConfId(Integer.valueOf(propId));

			opraterLogService.insert(opraterLogDO);
		}

		return "redirect:/opm/opm_list";
	}

	private PropConfDO getProp(Integer appFileId, Integer appId, String environment) {
		PropConfDO queryPropConf = new PropConfDO();
		queryPropConf.setAppId(appId);
		queryPropConf.setAppFileId(appFileId);
		queryPropConf.setStatus(PropConfConstant.STATUS_PASS);
		queryPropConf.setEnvironment(environment);
		PropConfDO propConf = propConfService.selectOne(queryPropConf);
		return propConf;
	}

}
