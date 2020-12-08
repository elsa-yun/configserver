package com.elsa.configserver.util;

import org.apache.commons.lang.StringUtils;

import com.elsa.configserver.constant.PropConfConstant;

public class ConfigUtil {

	/** test | staging | produce */
	private String environment;

	private String localFolderPath;

	private String localDomain;

	public void init() {

	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public boolean isTest() {
		if (StringUtils.isBlank(this.environment)) {
			return false;
		}
		if (this.environment.equals(PropConfConstant.ENVIRONMENT_TEST)) {
			return true;
		}
		return false;
	}

	public boolean isStaging() {
		if (StringUtils.isBlank(this.environment)) {
			return false;
		}
		if (this.environment.equals(PropConfConstant.ENVIRONMENT_STAGING)) {
			return true;
		}
		return false;

	}

	public boolean isProduct() {
		if (StringUtils.isBlank(this.environment)) {
			return false;
		}
		if (this.environment.equals(PropConfConstant.ENVIRONMENT_PRODUCE)) {
			return true;
		}
		return false;
	}

	public String getLocalFolderPath() {
		return localFolderPath;
	}

	public void setLocalFolderPath(String localFolderPath) {
		this.localFolderPath = localFolderPath;
	}

	public String getLocalDomain() {
		return localDomain;
	}

	public void setLocalDomain(String localDomain) {
		this.localDomain = localDomain;
	}

}
