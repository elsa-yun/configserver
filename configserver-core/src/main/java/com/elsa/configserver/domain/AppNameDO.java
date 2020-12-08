package com.elsa.configserver.domain;

import com.elsa.configserver.domain.BaseDO;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author haisheng.long Wed Jan 28 18:51:19 CST 2015
 */

public class AppNameDO extends BaseDO {

	/** 主键 */
	private Integer id;

	/** 应用名 */
	private String appName;

	/** 是否删除 */
	private Boolean isDelete;

	/** 创建时间 */
	private Date createTime;

	/** 是否有效 */
	private String validName;

	/** 修改时间 */
	private Date modifyTime;
	
	private int checked = 0;


	private List<AppFileNameDO> fileNameList;

	/**
	 * 设置 主键
	 * 
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 设置 应用名
	 * 
	 * @param appName
	 */
	public void setAppName(String appName) {
		this.appName = appName;
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
	 * 获取 主键
	 * 
	 * @return id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 获取 应用名
	 * 
	 * @return appName
	 */
	public String getAppName() {
		return appName;
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

	public String getValidName() {
		if (!getIsDelete()) {
			return "有效";
		}
		return "已禁用";
	}

	public void setValidName(String validName) {
		this.validName = validName;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public List<AppFileNameDO> getFileNameList() {
		return fileNameList;
	}

	public void setFileNameList(List<AppFileNameDO> fileNameList) {
		this.fileNameList = fileNameList;
	}

	public int getChecked() {
		return checked;
	}
	
	public void setChecked(int checked) {
		this.checked = checked;
	}
}