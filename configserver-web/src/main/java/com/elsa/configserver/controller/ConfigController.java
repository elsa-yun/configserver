package com.elsa.configserver.controller;

import java.util.ArrayList;
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
import com.elsa.configserver.domain.UsersDO;
import com.elsa.configserver.service.AppFileNameService;
import com.elsa.configserver.service.AppNameService;
import com.elsa.configserver.service.OpraterLogService;
import com.elsa.configserver.service.PropConfService;
import com.elsa.configserver.service.UsersService;
import com.elsa.configserver.util.ConfigUtil;

@Controller
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ConfigController extends BaseController {

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

	@RequestMapping(value = "/config/tl_list", method = RequestMethod.GET)
	public String tlList(final ModelMap model, WebRequest request, HttpSession session) {
		String user_id = (String) session.getAttribute(UsersConstant.SESSION_USER_ID);
		String role = (String) session.getAttribute(UsersConstant.SESSION_USER_ROLE);
		if (StringUtils.isBlank(user_id)) {
			return "redirect:/login";
		}
		if (!role.equals(UsersConstant.ROLE_TL)) {
			return "redirect:/login";
		}
		UsersDO user = usersService.selectById(Long.valueOf(user_id));
		user.getAppIds();

		AppNameDO appNameDO = new AppNameDO();
		List<AppNameDO> appNames = appNameService.selectDynamic(appNameDO);

		setUser(user, appNames);
		List<AppNameDO> appList = user.getAppList();
		List<Integer> appIds = new ArrayList<Integer>();
		if (!CollectionUtils.isEmpty(appList)) {
			PropConfDO confDO = new PropConfDO();
			confDO.setStatus(PropConfConstant.STATUS_DEFAULT);
			List<PropConfDO> propConfList = propConfService.selectDynamic(confDO);
			for (AppNameDO app : appList) {
				appIds.add(app.getId());
			}
			for (AppNameDO app : appList) {
				List<AppFileNameDO> fileNameList = appFileNameService.queryByAppIds(appIds);
				if (!CollectionUtils.isEmpty(fileNameList)) {
					List<AppFileNameDO> fileList = new ArrayList<AppFileNameDO>();
					for (AppFileNameDO f : fileNameList) {
						if (f.getAppId().intValue() == app.getId().intValue()) {
							fileList.add(f);
						}
						if (!CollectionUtils.isEmpty(propConfList)) {
							for (PropConfDO conf : propConfList) {
								Integer appId = conf.getAppId();
								Integer appFileId = conf.getAppFileId();
								Integer status = conf.getStatus();
								if (status.intValue() == PropConfConstant.STATUS_DEFAULT && f.getAppId().intValue() == appId.intValue()
										&& appFileId.intValue() == f.getId().intValue()) {
									f.setHasApplyConf("yes");
								}
							}
						}
					}
					app.setFileNameList(fileList);
				}
			}
		}
		model.addAttribute("userAppList", appList);

		return "config/tl_list";
	}

	private void setUser(UsersDO usersDO, List<AppNameDO> appNames) {
		List<AppNameDO> appList = new ArrayList<AppNameDO>();
		if (StringUtils.isNotBlank(usersDO.getRole())) {
			if (usersDO.getRole().equals(UsersConstant.ROLE_TL)) {
				String appIds = usersDO.getAppIds();
				if (StringUtils.isNotBlank(appIds)) {
					String ids[] = appIds.split(",");
					for (String id : ids) {
						for (AppNameDO app : appNames) {
							if (id.equals(String.valueOf(app.getId()))) {
								appList.add(app);
								break;
							}
						}
					}
				}
			}
		}
		usersDO.setAppList(appList);
	}

	@RequestMapping(value = "/config/tl_apply/{appId}/{appFileId}", method = RequestMethod.GET)
	public String tlApply(final ModelMap model, @PathVariable Integer appId, @PathVariable Integer appFileId, WebRequest request,
			HttpSession session) {
		String user_id = (String) session.getAttribute(UsersConstant.SESSION_USER_ID);
		String role = (String) session.getAttribute(UsersConstant.SESSION_USER_ROLE);
		if (StringUtils.isBlank(user_id)) {
			return "redirect:/login";
		}
		if (!role.equals(UsersConstant.ROLE_TL)) {
			return "redirect:/login";
		}
		AppNameDO appName = appNameService.selectById(appId.longValue());
		AppFileNameDO appFile = appFileNameService.selectById(appFileId.longValue());

		model.addAttribute("app", appName);
		model.addAttribute("appFile", appFile);
		model.addAttribute("environment", configUtil.getEnvironment());
		String errorMsg = request.getParameter("errorMsg");
		model.addAttribute("errorMsg", errorMsg);

		return "config/tl_apply";
	}

	@RequestMapping(value = "/config/tl_edit/{appId}/{appFileId}", method = RequestMethod.GET)
	public String tlEdit(final ModelMap model, @PathVariable Integer appId, @PathVariable Integer appFileId, WebRequest request,
			HttpSession session) {
		String user_id = (String) session.getAttribute(UsersConstant.SESSION_USER_ID);
		String role = (String) session.getAttribute(UsersConstant.SESSION_USER_ROLE);
		if (StringUtils.isBlank(user_id)) {
			return "redirect:/login";
		}
		if (!role.equals(UsersConstant.ROLE_TL)) {
			return "redirect:/login";
		}
		AppNameDO appName = appNameService.selectById(appId.longValue());
		AppFileNameDO appFile = appFileNameService.selectById(appFileId.longValue());

		model.addAttribute("app", appName);
		model.addAttribute("appFile", appFile);
		model.addAttribute("environment", configUtil.getEnvironment());

		PropConfDO queryPropConf = new PropConfDO();
		queryPropConf.setAppFileId(appFileId);
		queryPropConf.setAppId(appId);
		queryPropConf.setStatus(PropConfConstant.STATUS_DEFAULT);
		queryPropConf.setUserId(Integer.valueOf(user_id));
		if (configUtil.isTest()) {
			queryPropConf.setEnvironment(PropConfConstant.ENVIRONMENT_TEST);
			List<PropConfDO> confs = propConfService.selectDynamic(queryPropConf);
			if (!CollectionUtils.isEmpty(confs)) {
				PropConfDO testPropConf = confs.get(0);
				model.addAttribute("testProp", testPropConf);
			}
		} else {
			if (configUtil.isProduct() || configUtil.isStaging()) {
				queryPropConf.setEnvironment(PropConfConstant.ENVIRONMENT_STAGING);
				List<PropConfDO> stagingConfs = propConfService.selectDynamic(queryPropConf);
				if (!CollectionUtils.isEmpty(stagingConfs)) {
					PropConfDO stagingPropConf = stagingConfs.get(0);
					model.addAttribute("stagingProp", stagingPropConf);
				}
				queryPropConf.setEnvironment(PropConfConstant.ENVIRONMENT_PRODUCE);
				List<PropConfDO> produceConfs = propConfService.selectDynamic(queryPropConf);
				if (!CollectionUtils.isEmpty(produceConfs)) {
					PropConfDO producePropConf = produceConfs.get(0);
					model.addAttribute("produceProp", producePropConf);
				}
			}
		}

		return "config/tl_edit";
	}

	@RequestMapping(value = "/config/tl_apply/update", method = RequestMethod.POST)
	public String tlUpdate(final ModelMap model, WebRequest request, HttpSession session) {
		String user_id = (String) session.getAttribute(UsersConstant.SESSION_USER_ID);
		String role = (String) session.getAttribute(UsersConstant.SESSION_USER_ROLE);
		if (StringUtils.isBlank(user_id)) {
			return "redirect:/login";
		}
		if (!role.equals(UsersConstant.ROLE_TL)) {
			return "redirect:/login";
		}

		String test_content = request.getParameter("test_content");
		String staging_content = request.getParameter("staging_content");
		String produce_content = request.getParameter("produce_content");

		String errorMsg = "";
		if (configUtil.isTest() && StringUtils.isBlank(test_content)) {
			errorMsg = "请填写测试环境配置项值";
		}
		if (configUtil.isStaging() && StringUtils.isBlank(staging_content)) {
			errorMsg = "请填写预发环境配置项值";
		}
		if (configUtil.isProduct() && StringUtils.isBlank(produce_content)) {
			errorMsg = "请填写线上环境配置项值";
		}

		if (configUtil.getEnvironment().equals(PropConfConstant.ENVIRONMENT_TEST)) {
			String parameter = request.getParameter("test_config_id");
			if (StringUtils.isNotBlank(parameter)) {
				Integer test_config_id = Integer.valueOf(parameter.trim());
				PropConfDO testPropConf = propConfService.selectById(test_config_id.longValue());
				testPropConf.setContent(test_content);
				testPropConf.setModifyTime(new Date());
				int update = propConfService.update(testPropConf, false);

				OpraterLogDO opraterLogDO = getNewOpratorForAdd(session);

				opraterLogDO.setAppFileId(testPropConf.getAppFileId());
				opraterLogDO.setAppId(testPropConf.getAppId());
				opraterLogDO.setEnvironment(PropConfConstant.ENVIRONMENT_TEST);

				opraterLogDO.setPropConfId(test_config_id);
				opraterLogDO.setBeforeContent(testPropConf.getContent());
				opraterLogDO.setAfterContent(test_content);

				if (update > 0) {
					opraterLogService.insert(opraterLogDO);
				}
			}
		}
		if (configUtil.getEnvironment().equals(PropConfConstant.ENVIRONMENT_STAGING)
				|| configUtil.getEnvironment().equals(PropConfConstant.ENVIRONMENT_PRODUCE)) {
			if (StringUtils.isNotBlank(staging_content)) {
				String stringStagingConfigId = request.getParameter("staging_config_id");
				if (StringUtils.isNotBlank(stringStagingConfigId)) {
					Integer staging_config_id = Integer.valueOf(stringStagingConfigId.trim());
					PropConfDO stagingPropConf = propConfService.selectById(staging_config_id.longValue());
					if (stagingPropConf.getStatus().intValue() == PropConfConstant.STATUS_DEFAULT) {
						stagingPropConf.setContent(staging_content);
						stagingPropConf.setModifyTime(new Date());
						int update = propConfService.update(stagingPropConf, false);

						OpraterLogDO opraterLogDO = getNewOpratorForAdd(session);

						opraterLogDO.setAppFileId(stagingPropConf.getAppFileId());
						opraterLogDO.setAppId(stagingPropConf.getAppId());
						opraterLogDO.setEnvironment(PropConfConstant.ENVIRONMENT_STAGING);

						opraterLogDO.setPropConfId(staging_config_id);
						opraterLogDO.setBeforeContent(stagingPropConf.getContent());
						opraterLogDO.setAfterContent(staging_content);

						if (update > 0) {
							opraterLogService.insert(opraterLogDO);
						}
					}
				}
			}
			if (StringUtils.isNotBlank(produce_content)) {
				String parameter = request.getParameter("produce_config_id");
				if (StringUtils.isNotBlank(parameter)) {
					Integer produce_config_id = Integer.valueOf(parameter.trim());
					PropConfDO producePropConf = propConfService.selectById(produce_config_id.longValue());
					if (producePropConf.getStatus().intValue() == PropConfConstant.STATUS_DEFAULT) {
						producePropConf.setContent(produce_content);
						producePropConf.setModifyTime(new Date());
						int update = propConfService.update(producePropConf, false);

						OpraterLogDO opraterLogDO = getNewOpratorForAdd(session);

						opraterLogDO.setAppFileId(producePropConf.getAppFileId());
						opraterLogDO.setAppId(producePropConf.getAppId());
						opraterLogDO.setPropConfId(produce_config_id);

						opraterLogDO.setBeforeContent(producePropConf.getContent());
						opraterLogDO.setAfterContent(produce_content);
						opraterLogDO.setEnvironment(PropConfConstant.ENVIRONMENT_PRODUCE);
						if (update > 0) {
							opraterLogService.insert(opraterLogDO);
						}
					}
				}
			}
		}

		return "redirect:/config/tl_list";
	}

	private OpraterLogDO getNewOpratorForAdd(HttpSession session) {
		OpraterLogDO opraterLogDO = new OpraterLogDO();
		opraterLogDO.setOperator(session.getAttribute(UsersConstant.SESSION_USER_ROLE).toString());
		opraterLogDO.setType(String.valueOf(OpraterLogConstant.TYPE_APPLY));
		opraterLogDO.setCreateTime(new Date());
		return opraterLogDO;
	}

	@RequestMapping(value = "/config/tl_apply/add", method = RequestMethod.POST)
	public String tlApply(final ModelMap model, WebRequest request, HttpSession session) {
		String user_id = (String) session.getAttribute(UsersConstant.SESSION_USER_ID);
		String role = (String) session.getAttribute(UsersConstant.SESSION_USER_ROLE);
		if (StringUtils.isBlank(user_id)) {
			return "redirect:/login";
		}
		if (!role.equals(UsersConstant.ROLE_TL)) {
			return "redirect:/login";
		}
		String app_id = request.getParameter("app_id");
		String app_file_id = request.getParameter("app_file_id");
		String test_content = request.getParameter("test_content");
		String staging_content = request.getParameter("staging_content");
		String produce_content = request.getParameter("produce_content");

		String errorMsg = "";
		if (configUtil.isTest() && StringUtils.isBlank(test_content)) {
			errorMsg = "请填写测试环境配置项值";
		}
		if (configUtil.isStaging() && StringUtils.isBlank(staging_content)) {
			errorMsg = "请填写预发环境配置项值";
		}
		if (configUtil.isProduct() && StringUtils.isBlank(produce_content)) {
			errorMsg = "请填写线上环境配置项值";
		}

		
		if (configUtil.getEnvironment().equals(PropConfConstant.ENVIRONMENT_TEST)) {
			PropConfDO insertPropConf = new PropConfDO();
			insertPropConf.setAppFileId(Integer.valueOf(app_file_id));
			insertPropConf.setAppId(Integer.valueOf(app_id));
			insertPropConf.setContent(test_content);
			insertPropConf.setStatus(PropConfConstant.STATUS_DEFAULT);
			insertPropConf.setEnvironment(PropConfConstant.ENVIRONMENT_TEST);
			insertPropConf.setUserId(Integer.valueOf(user_id));
			insertPropConf.setCreateTime(new Date());
			insertPropConf.setModifyTime(new Date());
			Long id = propConfService.insert(insertPropConf);

			OpraterLogDO opraterLogDO = getNewOprator(session, app_id, app_file_id);

			opraterLogDO.setEnvironment(PropConfConstant.ENVIRONMENT_TEST);
			opraterLogDO.setPropConfId(id.intValue());
			opraterLogDO.setAfterContent(test_content);
			if (id > 0) {
				opraterLogService.insert(opraterLogDO);
			}
		}
		if (configUtil.getEnvironment().equals(PropConfConstant.ENVIRONMENT_STAGING)
				|| configUtil.getEnvironment().equals(PropConfConstant.ENVIRONMENT_PRODUCE)) {
			if (StringUtils.isNotBlank(staging_content)) {
				PropConfDO insertPropConf = new PropConfDO();
				insertPropConf.setAppFileId(Integer.valueOf(app_file_id));
				insertPropConf.setAppId(Integer.valueOf(app_id));
				insertPropConf.setContent(staging_content);
				insertPropConf.setEnvironment(PropConfConstant.ENVIRONMENT_STAGING);
				insertPropConf.setStatus(PropConfConstant.STATUS_DEFAULT);
				insertPropConf.setUserId(Integer.valueOf(user_id));
				insertPropConf.setCreateTime(new Date());
				insertPropConf.setModifyTime(new Date());
				Long id = propConfService.insert(insertPropConf);

				OpraterLogDO opraterLogDO = getNewOprator(session, app_id, app_file_id);

				opraterLogDO.setEnvironment(PropConfConstant.ENVIRONMENT_STAGING);
				opraterLogDO.setPropConfId(id.intValue());
				opraterLogDO.setAfterContent(staging_content);
				if (id > 0) {
					opraterLogService.insert(opraterLogDO);
				}
			}
			if (StringUtils.isNotBlank(produce_content)) {
				PropConfDO insertPropConf = new PropConfDO();
				insertPropConf.setAppFileId(Integer.valueOf(app_file_id));
				insertPropConf.setAppId(Integer.valueOf(app_id));
				insertPropConf.setContent(produce_content);
				insertPropConf.setEnvironment(PropConfConstant.ENVIRONMENT_PRODUCE);
				insertPropConf.setStatus(PropConfConstant.STATUS_DEFAULT);
				insertPropConf.setUserId(Integer.valueOf(user_id));
				insertPropConf.setCreateTime(new Date());
				insertPropConf.setModifyTime(new Date());
				Long id = propConfService.insert(insertPropConf);

				OpraterLogDO opraterLogDO = getNewOprator(session, app_id, app_file_id);

				opraterLogDO.setEnvironment(PropConfConstant.ENVIRONMENT_PRODUCE);
				opraterLogDO.setPropConfId(id.intValue());
				opraterLogDO.setAfterContent(produce_content);
				if (id > 0) {
					opraterLogService.insert(opraterLogDO);
				}
			}
		}
		return "redirect:/config/tl_list";
	}

	private OpraterLogDO getNewOprator(HttpSession session, String app_id, String app_file_id) {
		OpraterLogDO opraterLogDO = new OpraterLogDO();
		opraterLogDO.setAppFileId(Integer.valueOf(app_file_id));
		opraterLogDO.setAppId(Integer.valueOf(app_id));
		opraterLogDO.setOperator(session.getAttribute(UsersConstant.SESSION_USER_ROLE).toString());
		opraterLogDO.setType(String.valueOf(OpraterLogConstant.TYPE_APPLY));
		opraterLogDO.setCreateTime(new Date());
		return opraterLogDO;
	}

}
