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

import org.hibernate.envers.Audited;

@Entity
@Table(name = "rob_training_final_listing_remark")
@Audited
public class RobTrainingFinalListingRemark implements Serializable {

	private long robTrainingFinalListingRemarkId;
	private String finalListingRefNo;
	private String remark;
	private Date createDt;
	private String createBy;
	private Date updateDt;
	private String updateBy;
	private String queryBy;

	public RobTrainingFinalListingRemark() {
		super();
	}

	public RobTrainingFinalListingRemark(long robTrainingFinalListingRemarkId, String finalListingRefNo, String remark, Date createDt,
			String createBy, Date updateDt, String updateBy, String queryBy) {
		super();
		this.robTrainingFinalListingRemarkId = robTrainingFinalListingRemarkId;
		this.finalListingRefNo = finalListingRefNo;
		this.remark = remark;
		this.createBy = createBy;
		this.updateDt = updateDt;
		this.updateBy = updateBy;
		this.queryBy = queryBy;
	}
	
	
	@Id
	@Column(name = "rob_training_final_listing_remark_id", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getRobTrainingFinalListingRemarkId() {
		return robTrainingFinalListingRemarkId;
	}

	public void setRobTrainingFinalListingRemarkId(long robTrainingFinalListingRemarkId) {
		this.robTrainingFinalListingRemarkId = robTrainingFinalListingRemarkId;
	}

	@Column(name = "final_listing_ref_no", length = 50)
	public String getFinalListingRefNo() {
		return finalListingRefNo;
	}

	public void setFinalListingRefNo(String finalListingRefNo) {
		this.finalListingRefNo = finalListingRefNo;
	}

	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_dt", nullable = true, length = 3594)
	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	@Column(name = "create_by", nullable = true, length = 50)
	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_dt", nullable = true, length = 3594)
	public Date getUpdateDt() {
		return updateDt;
	}

	public void setUpdateDt(Date updateDt) {
		this.updateDt = updateDt;
	}

	@Column(name = "update_by", nullable = true, length = 50)
	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	@Column(name = "query_by")
	public String getQueryBy() {
		return queryBy;
	}

	public void setQueryBy(String queryBy) {
		this.queryBy = queryBy;
	}
}