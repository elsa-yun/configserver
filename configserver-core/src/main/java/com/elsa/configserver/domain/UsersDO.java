package com.elsa.configserver.domain;

import com.elsa.configserver.domain.BaseDO;

import java.util.Date;
import java.util.List;

/**
 * 用户
 * 
 * @author haisheng.long Thu Jan 29 17:19:43 CST 2015
 */

public class UsersDO extends BaseDO {

	/** 主键 */
	private Integer id;

	/** 用户名 */
	private String username;

	/** 密码 */
	private String password;

	/** 角色： tl ：业务线 tl; opm:运维 ; admin：管理 员 */
	private String role;

	/**  */
	private Boolean isDelete;

	/** 可操作的应用IDS */
	private String appIds;

	/** 修改时间 */
	private Date modifyTime;

	/** 创建时间 */
	private Date createTime;

	private String operatorApp;

	private List<AppNameDO> appList;

	public String getOperatorApp() {
		return operatorApp;
	}

	public void setOperatorApp(String operatorApp) {
		this.operatorApp = operatorApp;
	}

	/**
	 * 设置 主键
	 * 
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 设置 用户名
	 * 
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 设置 密码
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 设置 角色： tl ：业务线 tl; opm:运维 ; admin：管理 员
	 * 
	 * @param role
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * 设置
	 * 
	 * @param isDelete
	 */
	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}

	/**
	 * 设置 可操作的应用IDS
	 * 
	 * @param appIds
	 */
	public void setAppIds(String appIds) {
		this.appIds = appIds;
	}

	/**
	 * 设置 修改时间
	 * 
	 * @param modifyTime
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	/**
	 * 设置 创建时间
	 * 
	 * @param createTime
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取 主键
	 * 
	 * @return id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 获取 用户名
	 * 
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 获取 密码
	 * 
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 获取 角色： tl ：业务线 tl; opm:运维 ; admin：管理 员
	 * 
	 * @return role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * 获取
	 * 
	 * @return isDelete
	 */
	public Boolean getIsDelete() {
		return isDelete;
	}

	/**
	 * 获取 可操作的应用IDS
	 * 
	 * @return appIds
	 */
	public String getAppIds() {
		return appIds;
	}

	/**
	 * 获取 修改时间
	 * 
	 * @return modifyTime
	 */
	public Date getModifyTime() {
		return modifyTime;
	}

	/**
	 * 获取 创建时间
	 * 
	 * @return createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	public List<AppNameDO> getAppList() {
		return appList;
	}

	public void setAppList(List<AppNameDO> appList) {
		this.appList = appList;
	}

}