package com.ssm.base.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

@MappedSuperclass 
public class BaseDataModel implements Serializable{

	private String updateBy;
	private Date updateDt;
	private Date createDt;
	private String createBy;
	private Long signInSessionId;
	
//	@PrePersist
//    protected void onCreate() {
//		updateDt = createDt = new Date();
//    }
//
//    @PreUpdate
//    protected void onUpdate() {
//    	updateDt = new Date();
//    }
	
	
	@Audited
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_dt", nullable = false, length = 3594)
	public Date getCreateDt() {
		return this.createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	@Audited
	@Column(name = "create_by", nullable = false, length = 50)
	public String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@Audited
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_dt", nullable = false, length = 3594)
	public Date getUpdateDt() {
		return this.updateDt;
	}

	public void setUpdateDt(Date updateDt) {
		this.updateDt = updateDt;
	}

	@Audited
	@Column(name = "update_by", nullable = false, length = 50)
	public String getUpdateBy() {
		return this.updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	@Audited
	@Column(name = "sign_in_session_id") 
	public Long getSignInSessionId() {
		return signInSessionId;
	}

	public void setSignInSessionId(Long signInSessionId) {
		this.signInSessionId = signInSessionId;
	}
}
