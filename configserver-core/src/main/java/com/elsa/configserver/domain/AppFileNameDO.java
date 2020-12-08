package com.elsa.configserver.domain;

import com.elsa.configserver.domain.BaseDO;

import java.util.Date;

/**
 * app应用里配置文件的名称
 * 
 * @author haisheng.long Thu Jan 29 13:53:32 CST 2015
 */

public class AppFileNameDO extends BaseDO {

	/** 主键 */
	private Integer id;

	/** 应用ID */
	private Integer appId;

	/** 配置文件名 */
	private String propFileName;

	/** 是否删除 */
	private Boolean isDelete;

	/** 创建时间 */
	private Date createTime;

	/** 修改时间 */
	private Date modifyTime;

	private String hasApplyConf = "no";

	/**
	 * 设置 主键
	 * 
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
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
	 * 设置 配置文件名
	 * 
	 * @param propFileName
	 */
	public void setPropFileName(String propFileName) {
		this.propFileName = propFileName;
	}

	/**
	 * 设置 是否删除
	 * 
	 * @param isDelete
	 */
	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
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
	 * 获取 主键
	 * 
	 * @return id
	 */
	public Integer getId() {
		return id;
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
	 * 获取 配置文件名
	 * 
	 * @return propFileName
	 */
	public String getPropFileName() {
		return propFileName;
	}

	/**
	 * 获取 是否删除
	 * 
	 * @return isDelete
	 */
	public Boolean getIsDelete() {
		return isDelete;
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

	public String getHasApplyConf() {
		return hasApplyConf;
	}

	public void setHasApplyConf(String hasApplyConf) {
		this.hasApplyConf = hasApplyConf;
	}

}