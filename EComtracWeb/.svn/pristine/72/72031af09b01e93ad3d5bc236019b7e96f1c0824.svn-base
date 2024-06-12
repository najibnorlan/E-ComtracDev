package com.ssm.llp.ezbiz.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "rob_sync_profile_audit")
public class RobSyncProfileAudit implements Serializable{

	private Integer id;
	private String originalName;
	private String updatedName;
	private String icNo;
	private String remarks;
	private Date createDt;
	private String createBy;
	private String approveBy;
	
	public RobSyncProfileAudit() {
	}
	
	public RobSyncProfileAudit(Integer id, String originalName,
			String updatedName, String icNo, String remarks, Date createDt,
			String createBy, String approveBy) {
		super();
		this.id = id;
		this.originalName = originalName;
		this.updatedName = updatedName;
		this.icNo = icNo;
		this.remarks = remarks;
		this.createDt = createDt;
		this.createBy = createBy;
		this.approveBy = approveBy;
	}
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "original_name")
	public String getOriginalName() {
		return originalName;
	}
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}
	
	@Column(name = "updated_name")
	public String getUpdatedName() {
		return updatedName;
	}
	public void setUpdatedName(String updatedName) {
		this.updatedName = updatedName;
	}
	
	@Column(name = "ic_no")
	public String getIcNo() {
		return icNo;
	}
	public void setIcNo(String icNo) {
		this.icNo = icNo;
	}
	
	@Column(name = "remarks")
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	@Column(name = "approve_by", nullable = false, length = 50)
	public String getApproveBy() {
		return approveBy;
	}

	public void setApproveBy(String approveBy) {
		this.approveBy = approveBy;
	}

}
