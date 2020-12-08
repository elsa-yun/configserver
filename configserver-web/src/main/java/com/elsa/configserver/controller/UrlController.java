package com.elsa.configserver.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.elsa.configserver.client.HttpClientConstants;
import com.elsa.configserver.constant.PropConfConstant;
import com.elsa.configserver.domain.AppFileNameDO;
import com.elsa.configserver.domain.AppNameDO;
import com.elsa.configserver.domain.PropConfDO;
import com.elsa.configserver.service.AppFileNameService;
import com.elsa.configserver.service.AppNameService;
import com.elsa.configserver.service.PropConfService;
import com.elsa.configserver.util.ConfigUtil;

@Controller
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class UrlController {

	@Autowired
	PropConfService propConfService;

	@Autowired
	AppNameService appNameService;

	@Autowired
	AppFileNameService appFileNameService;

	@Autowired
	ConfigUtil configUtil;

	@RequestMapping(value = "/stream_old", method = RequestMethod.GET)
	public byte[] Test(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String appName = request.getParameter(HttpClientConstants.APP_NAME);
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(appName)) {
			AppNameDO query = new AppNameDO();
			query.setAppName(appName);
			query.setIsDelete(false);
			List<AppNameDO> appList = appNameService.selectDynamic(query);
			if (!CollectionUtils.isEmpty(appList)) {
				AppNameDO app = appList.get(0);
				Integer appId = app.getId();
				AppFileNameDO q = new AppFileNameDO();
				q.setIsDelete(false);
				q.setAppId(appId);
				List<AppFileNameDO> fileNameList = appFileNameService.selectDynamic(q);
				if (!CollectionUtils.isEmpty(fileNameList)) {
					for (AppFileNameDO appFileNameDO : fileNameList) {
						Integer appFileId = appFileNameDO.getId();

						String propFileName = appFileNameDO.getPropFileName();

						PropConfDO queryPropConfDO = new PropConfDO();
						queryPropConfDO.setAppFileId(appFileId);
						queryPropConfDO.setAppId(appId);
						queryPropConfDO.setStatus(PropConfConstant.STATUS_PASS);
						queryPropConfDO.setEnvironment(configUtil.getEnvironment());
						PropConfDO result = propConfService.selectOne(queryPropConfDO);

						if (null != result) {
							String content = result.getContent().trim();
							// String file_path =
							// configUtil.getLocalFolderPath()+File.separator +
							// propFileName;
							// boolean isWrite = FileUtil.writeFile(file_path,
							// content);
							sb.append(propFileName);
							sb.append(HttpClientConstants.FILE_NAME_SPLIT_CHARS);
							sb.append(content);
							sb.append(HttpClientConstants.FILE_VALUE_SPLIT_CHARS);

						}
					}
				}

			}
		}

		String str = sb.toString();

		response.setContentType("application/octet-stream");
		OutputStream out = response.getOutputStream();
		out.write(str.getBytes("utf-8"));
		out.flush();
		out.close();
		return null;
	}
	
	@RequestMapping(value = "/stream", method = RequestMethod.GET)
	public String streamNew(final ModelMap model,HttpServletRequest request, HttpServletResponse response) throws IOException {
		String appName = request.getParameter(HttpClientConstants.APP_NAME);
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(appName)) {
			AppNameDO query = new AppNameDO();
			query.setAppName(appName);
			query.setIsDelete(false);
			List<AppNameDO> appList = appNameService.selectDynamic(query);
			if (!CollectionUtils.isEmpty(appList)) {
				AppNameDO app = appList.get(0);
				Integer appId = app.getId();
				AppFileNameDO q = new AppFileNameDO();
				q.setIsDelete(false);
				q.setAppId(appId);
				List<AppFileNameDO> fileNameList = appFileNameService.selectDynamic(q);
				if (!CollectionUtils.isEmpty(fileNameList)) {
					for (AppFileNameDO appFileNameDO : fileNameList) {
						Integer appFileId = appFileNameDO.getId();

						String propFileName = appFileNameDO.getPropFileName();

						PropConfDO queryPropConfDO = new PropConfDO();
						queryPropConfDO.setAppFileId(appFileId);
						queryPropConfDO.setAppId(appId);
						queryPropConfDO.setStatus(PropConfConstant.STATUS_PASS);
						queryPropConfDO.setEnvironment(configUtil.getEnvironment());
						PropConfDO result = propConfService.selectOne(queryPropConfDO);

						if (null != result) {
							String content = result.getContent().trim();
							sb.append(propFileName);
							sb.append(HttpClientConstants.FILE_NAME_SPLIT_CHARS);
							sb.append(content);
							sb.append(HttpClientConstants.FILE_VALUE_SPLIT_CHARS);

						}
					}
				}

			}
		}
        // 禁用缓存
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-cache,no-store");
		String str = sb.toString();
		model.put("content", str);
		return "stream";
	}
}
