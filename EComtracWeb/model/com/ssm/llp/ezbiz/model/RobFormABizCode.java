package com.ssm.llp.ezbiz.model;

// Generated Jan 14, 2016 11:44:02 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

/**
 * RobFormABizCode generated by hbm2java
 */
@Entity
@Table(name = "rob_form_a_biz_code")
@Audited
public class RobFormABizCode implements java.io.Serializable {

	private long robFormABizCodeId;
	private String robFormACode;
	private String bizCode;
	private String bizCodeDesc;
	private Date createDt;
	private String createBy;
	private Date updateDt;
	private String updateBy;
	private Boolean selected = Boolean.FALSE;

	public RobFormABizCode() {
	}

	public RobFormABizCode(long robFormABizCodeId, Date createDt, String createBy, Date updateDt, String updateBy) {
		this.robFormABizCodeId = robFormABizCodeId;
		this.createDt = createDt;
		this.createBy = createBy;
		this.updateDt = updateDt;
		this.updateBy = updateBy;
	}

	public RobFormABizCode(long robFormABizCodeId, String robFormACode, String bizCode, Date createDt, String createBy, Date updateDt,
			String updateBy) {
		this.robFormABizCodeId = robFormABizCodeId;
		this.robFormACode = robFormACode;
		this.bizCode = bizCode;
		this.createDt = createDt;
		this.createBy = createBy;
		this.updateDt = updateDt;
		this.updateBy = updateBy;
	}

	public RobFormABizCode(String code, String codeDesc) {
		bizCode= code;
		bizCodeDesc = codeDesc;
	}

	@Id
	@Column(name = "rob_form_a_biz_code_id", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getRobFormABizCodeId() {
		return this.robFormABizCodeId;
	}

	public void setRobFormABizCodeId(long robFormABizCodeId) {
		this.robFormABizCodeId = robFormABizCodeId;
	}

	@Column(name = "rob_form_a_code", length = 25)
	public String getRobFormACode() {
		return this.robFormACode;
	}

	public void setRobFormACode(String robFormACode) {
		this.robFormACode = robFormACode;
	}

	@Column(name = "biz_code", length = 70)
	public String getBizCode() {
		return this.bizCode;
	}

	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_dt", nullable = false, length = 3594)
	public Date getCreateDt() {
		return this.createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	@Column(name = "create_by", nullable = false, length = 50)
	public String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_dt", nullable = false, length = 3594)
	public Date getUpdateDt() {
		return this.updateDt;
	}

	public void setUpdateDt(Date updateDt) {
		this.updateDt = updateDt;
	}

	@Column(name = "update_by", nullable = false, length = 50)
	public String getUpdateBy() {
		return this.updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	
	@Column(name = "biz_code_desc", length = 200)
	public String getBizCodeDesc() {
		return bizCodeDesc;
	}

	public void setBizCodeDesc(String bizCodeDesc) {
		this.bizCodeDesc = bizCodeDesc;
	}

	@Transient
	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

}
