package com.elsa.configserver.domain;

import com.elsa.configserver.domain.BaseDO;

import java.util.Date;

/**
 * 操作日志
 * 
 * @author haisheng.long Tue Feb 03 15:38:41 CST 2015
 */

public class OpraterLogDO extends BaseDO {

	/** 主键 */
	private Integer id;

	/** 应用ID */
	private Integer appId;

	/** 操作内容 */
	private String beforeContent;

	/** 操作人 */
	private String operator;

	/** 系统 运行 环境：test staging produce */
	private String environment;

	/** 1：申请 2：审核 3：拆消 4：恢复 */
	private String type;

	/** 操作时间 */
	private Date createTime;

	/**  */
	private String afterContent;

	private Integer appFileId;

	private Integer propConfId;

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
	 * 设置 操作内容
	 * 
	 * @param beforeContent
	 */
	public void setBeforeContent(String beforeContent) {
		this.beforeContent = beforeContent;
	}

	/**
	 * 设置 操作人
	 * 
	 * @param operator
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * 设置 系统 运行 环境：test staging produce
	 * 
	 * @param environment
	 */
	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	/**
	 * 设置 1：申请， ：申请， 2：审核， ：审核， 3：拆消， ：拆消， ：拆消， 4：恢复
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 设置 操作时间
	 * 
	 * @param createTime
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 设置
	 * 
	 * @param afterContent
	 */
	public void setAfterContent(String afterContent) {
		this.afterContent = afterContent;
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
	 * 获取 操作内容
	 * 
	 * @return beforeContent
	 */
	public String getBeforeContent() {
		return beforeContent;
	}

	/**
	 * 获取 操作人
	 * 
	 * @return operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * 获取 系统 运行 环境：test staging produce
	 * 
	 * @return environment
	 */
	public String getEnvironment() {
		return environment;
	}

	/**
	 * 获取 1：申请， ：申请， 2：审核， ：审核， 3：拆消， ：拆消， ：拆消， 4：恢复
	 * 
	 * @return type
	 */
	public String getType() {
		return type;
	}

	/**
	 * 获取 操作时间
	 * 
	 * @return createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 获取
	 * 
	 * @return afterContent
	 */
	public String getAfterContent() {
		return afterContent;
	}

	public Integer getAppFileId() {
		return appFileId;
	}

	public void setAppFileId(Integer appFileId) {
		this.appFileId = appFileId;
	}

	public Integer getPropConfId() {
		return propConfId;
	}

	public void setPropConfId(Integer propConfId) {
		this.propConfId = propConfId;
	}

}