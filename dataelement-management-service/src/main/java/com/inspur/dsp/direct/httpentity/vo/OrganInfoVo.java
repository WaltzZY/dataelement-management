package com.inspur.dsp.direct.httpentity.vo;

import java.util.Date;

/**
 * 部门信息实体
 */
public class OrganInfoVo implements java.io.Serializable{

	private String id;

	private String code;

	private String name;

	private String pinyin;

	private String icon;

	private String organType;

	private String shortName;

	private String regionCode;

	private String regionName;

	private Integer sortOrder;

	private String creator;

	private Date createTime;

	private String lastEditor;

	private Date lastTime;

	private String remark;

	private String status;

	private String appCode;

	private Integer childs;

	private String isBusiness;

	private String organLevel;

	private String type;

	/**
	 * 1200**00000是部委
	 */
	private String shortCode;

	private String organPathCode;

	private Date updateTime;

	/**
	 * 组织机构条线
	 */
	private String organLine;

	/**
	 * 统一社会信用代码
	 */
	private String societyCode;

	/**
	 * 组织机构路径, / 分割
	 */
	private String organPath;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getOrganType() {
		return organType;
	}

	public void setOrganType(String organType) {
		this.organType = organType;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getLastEditor() {
		return lastEditor;
	}

	public void setLastEditor(String lastEditor) {
		this.lastEditor = lastEditor;
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public Integer getChilds() {
		return childs;
	}

	public void setChilds(Integer childs) {
		this.childs = childs;
	}

	public String getIsBusiness() {
		return isBusiness;
	}

	public void setIsBusiness(String isBusiness) {
		this.isBusiness = isBusiness;
	}

	public String getOrganLevel() {
		return organLevel;
	}

	public void setOrganLevel(String organLevel) {
		this.organLevel = organLevel;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

	public String getOrganPathCode() {
		return organPathCode;
	}

	public void setOrganPathCode(String organPathCode) {
		this.organPathCode = organPathCode;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getOrganLine() {
		return organLine;
	}

	public void setOrganLine(String organLine) {
		this.organLine = organLine;
	}

	public String getSocietyCode() {
		return societyCode;
	}

	public void setSocietyCode(String societyCode) {
		this.societyCode = societyCode;
	}

	public String getOrganPath() {
		return organPath;
	}

	public void setOrganPath(String organPath) {
		this.organPath = organPath;
	}

	private static final long serialVersionUID = 1L;
}
