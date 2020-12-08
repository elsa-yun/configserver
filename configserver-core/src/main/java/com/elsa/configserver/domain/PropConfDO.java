package com.elsa.configserver.domain;

import com.elsa.configserver.domain.BaseDO;

import java.util.Date;

/**
 * 应用配置项
 * 
 * @author haisheng.long Mon Feb 02 10:58:52 CST 2015
 */

public class PropConfDO extends BaseDO {

	/** 主键 */
	private Integer id;

	/** app中的文件名 */
	private Integer appFileId;

	/** 应用ID */
	private Integer appId;

	/** 实际使用，1：可使用;2:使用中 */
	private Integer actualUse;

	/** 应用配置项内容 */
	private String content;

	/** 申请状态：0：申请中;1: 审核通过;2:审核不通过 */
	private Integer status;

	/** 系统 运行 环境 Test | staging | produce */
	private String environment;

	/** 创建人ID */
	private Integer userId;

	/** 创建时间 */
	private Date createTime;

	/** 修改时间 */
	private Date modifyTime;

	/** 有配置项更新的时间 */
	private Date itemModifyTime;

	private AppNameDO appNameDO;

	private AppFileNameDO appFileNameDO;

	/**
	 * 设置 主键
	 * 
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 设置 app中的文件名
	 * 
	 * @param appFileId
	 */
	public void setAppFileId(Integer appFileId) {
		this.appFileId = appFileId;
	}

	/**
	 * 设置 应用ID
	 * 
	 * @param appId
	 */
	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	/**
	 * 设置 实际使用，1：可使用;2:使用中
	 * 
	 * @param actualUse
	 */
	public void setActualUse(Integer actualUse) {
		this.actualUse = actualUse;
	}

	/**
	 * 设置 应用配置项内容
	 * 
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 设置 申请状态：1：申请中;2: 审核通过;3:审核不通过
	 * 
	 * @param status
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 设置 系统 运行 环境 Test | staging | produce
	 * 
	 * @param environment
	 */
	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	/**
	 * 设置 创建人ID
	 * 
	 * @param userId
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
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
	 * 设置 修改时间
	 * 
	 * @param modifyTime
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	/**
	 * 设置 有配置项更新的时间
	 * 
	 * @param itemModifyTime
	 */
	public void setItemModifyTime(Date itemModifyTime) {
		this.itemModifyTime = itemModifyTime;
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
	 * 获取 app中的文件名
	 * 
	 * @return appFileId
	 */
	public Integer getAppFileId() {
		return appFileId;
	}

	/**
	 * 获取 应用ID
	 * 
	 * @return appId
	 */
	public Integer getAppId() {
		return appId;
	}

	/**
	 * 获取 实际使用，1：可使用;2:使用中
	 * 
	 * @return actualUse
	 */
	public Integer getActualUse() {
		return actualUse;
	}

	/**
	 * 获取 应用配置项内容
	 * 
	 * @return content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 获取 申请状态：1：申请中;2: 审核通过;3:审核不通过
	 * 
	 * @return status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * 获取 系统 运行 环境 Test | staging | produce
	 * 
	 * @return environment
	 */
	public String getEnvironment() {
		return environment;
	}

	/**
	 * 获取 创建人ID
	 * 
	 * @return userId
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * 获取 创建时间
	 * 
	 * @return createTime
	 */
	public Date getCreateTime() {
		return createTime;
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
	 * 获取 有配置项更新的时间
	 * 
	 * @return itemModifyTime
	 */
	public Date getItemModifyTime() {
		return itemModifyTime;
	}

	public AppNameDO getAppNameDO() {
		return appNameDO;
	}

	public void setAppNameDO(AppNameDO appNameDO) {
		this.appNameDO = appNameDO;
	}

	public AppFileNameDO getAppFileNameDO() {
		return appFileNameDO;
	}

	public void setAppFileNameDO(AppFileNameDO appFileNameDO) {
		this.appFileNameDO = appFileNameDO;
	}

	@Override
	public String toString() {
		return "PropConfDO [id=" + id + ", appFileId=" + appFileId + ", appId=" + appId + ", actualUse=" + actualUse + ", content="
				+ content + ", status=" + status + ", environment=" + environment + ", userId=" + userId + ", createTime=" + createTime
				+ ", modifyTime=" + modifyTime + ", itemModifyTime=" + itemModifyTime + "]";
	}

}