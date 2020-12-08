package com.elsa.configserver.domain;

import com.elsa.configserver.domain.BaseDO;

import java.util.Date;

/**
 * 
 * @author haisheng.long Wed Jan 28 14:58:14 CST 2015
 */

public class CityDO extends BaseDO {

	/** 主键 */
	private Integer id;

	/**  */
	private String name;

	/**  */
	private String spelling;

	/**  */
	private Integer parentId;

	/**  */
	private Boolean isDelete;

	/**  */
	private Date addDate;

	/**  */
	private Date lastDate;

	/**  */
	private Integer agencyId;

	/**  */
	private String fristWord;

	/**  */
	private String simpleSpelling;

	/**  */
	private Integer type;

	/**  */
	private String citycol;

	/**  */
	private Float p1;

	/**  */
	private Double p2;

	/**  */
	private Date modifyTime;

	/**
	 * 设置 主键
	 * 
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 设置
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 设置
	 * 
	 * @param spelling
	 */
	public void setSpelling(String spelling) {
		this.spelling = spelling;
	}

	/**
	 * 设置
	 * 
	 * @param parentId
	 */
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
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
	 * 设置
	 * 
	 * @param addDate
	 */
	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	/**
	 * 设置
	 * 
	 * @param lastDate
	 */
	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}

	/**
	 * 设置
	 * 
	 * @param agencyId
	 */
	public void setAgencyId(Integer agencyId) {
		this.agencyId = agencyId;
	}

	/**
	 * 设置
	 * 
	 * @param fristWord
	 */
	public void setFristWord(String fristWord) {
		this.fristWord = fristWord;
	}

	/**
	 * 设置
	 * 
	 * @param simpleSpelling
	 */
	public void setSimpleSpelling(String simpleSpelling) {
		this.simpleSpelling = simpleSpelling;
	}

	/**
	 * 设置
	 * 
	 * @param type
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * 设置
	 * 
	 * @param citycol
	 */
	public void setCitycol(String citycol) {
		this.citycol = citycol;
	}

	/**
	 * 设置
	 * 
	 * @param p1
	 */
	public void setP1(Float p1) {
		this.p1 = p1;
	}

	/**
	 * 设置
	 * 
	 * @param p2
	 */
	public void setP2(Double p2) {
		this.p2 = p2;
	}

	/**
	 * 设置
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
	 * 获取
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 获取
	 * 
	 * @return spelling
	 */
	public String getSpelling() {
		return spelling;
	}

	/**
	 * 获取
	 * 
	 * @return parentId
	 */
	public Integer getParentId() {
		return parentId;
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
	 * 获取
	 * 
	 * @return addDate
	 */
	public Date getAddDate() {
		return addDate;
	}

	/**
	 * 获取
	 * 
	 * @return lastDate
	 */
	public Date getLastDate() {
		return lastDate;
	}

	/**
	 * 获取
	 * 
	 * @return agencyId
	 */
	public Integer getAgencyId() {
		return agencyId;
	}

	/**
	 * 获取
	 * 
	 * @return fristWord
	 */
	public String getFristWord() {
		return fristWord;
	}

	/**
	 * 获取
	 * 
	 * @return simpleSpelling
	 */
	public String getSimpleSpelling() {
		return simpleSpelling;
	}

	/**
	 * 获取
	 * 
	 * @return type
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * 获取
	 * 
	 * @return citycol
	 */
	public String getCitycol() {
		return citycol;
	}

	/**
	 * 获取
	 * 
	 * @return p1
	 */
	public Float getP1() {
		return p1;
	}

	/**
	 * 获取
	 * 
	 * @return p2
	 */
	public Double getP2() {
		return p2;
	}

	/**
	 * 获取
	 * 
	 * @return modifyTime
	 */
	public Date getModifyTime() {
		return modifyTime;
	}

	@Override
	public String toString() {
		return "CityDO [id=" + id + ", name=" + name + ", spelling=" + spelling + "]";
	}

}