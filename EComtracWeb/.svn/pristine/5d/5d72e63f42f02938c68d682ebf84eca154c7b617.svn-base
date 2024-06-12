package com.ssm.llp.ezbiz.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "rob_training_final_listing")
@Audited
public class RobTrainingFinalListing implements Serializable {

	private String finalListingRefNo;
	private String status;
	private String trainingCode;
	private Date createDt;
	private String createBy;
	private Date updateDt;
	private String updateBy;
	private String queryBy;
	private String latestRemark;
//	private String remark;
//	private String query;
//	private Date submitDt;
//	private Date reSubmitDt;
//	private Date approveDt;
//	private String approveBy;
//	private Date queryDt;
//	private String remarkAnswer;

	// transient
	private List<RobTrainingFinalListingRemark> listRobTrainingFinalListingRemark = new ArrayList<RobTrainingFinalListingRemark>(
			0);

	public RobTrainingFinalListing() {
		super();
	}

	public RobTrainingFinalListing(String finalListingRefNo, String status, String trainingCode, Date createDt,
			String createBy, Date updateDt, String updateBy, String queryBy) {
		super();
		this.finalListingRefNo = finalListingRefNo;
		this.status = status;
		this.trainingCode = trainingCode;
		this.createDt = createDt;
		this.createBy = createBy;
		this.updateDt = updateDt;
		this.updateBy = updateBy;
		this.queryBy = queryBy;
	}

	@Id
	@GenericGenerator(name = "trans_code", strategy = "com.ssm.llp.base.hibernate.LlpIdGenerator", parameters = {
			@Parameter(name = "genCode", value = "COMTRAC_RUNNING_NO"), @Parameter(name = "fieldCode", value = "FL"),
			@Parameter(name = "moduleCode", value = ""), @Parameter(name = "yearMonthDay", value = "yyyyMMdd"),
			@Parameter(name = "lastNoPattern", value = "00000") })
	@GeneratedValue(generator = "trans_code")
	@Column(name = "final_listing_ref_no", nullable = false, length = 50)
	public String getFinalListingRefNo() {
		return finalListingRefNo;
	}

	public void setFinalListingRefNo(String finalListingRefNo) {
		this.finalListingRefNo = finalListingRefNo;
	}

	@Column(name = "status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "training_code")
	public String getTrainingCode() {
		return trainingCode;
	}

	public void setTrainingCode(String trainingCode) {
		this.trainingCode = trainingCode;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_dt", nullable = false, length = 3594)
	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	@Column(name = "create_by", nullable = false, length = 50)
	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_dt", nullable = false, length = 3594)
	public Date getUpdateDt() {
		return updateDt;
	}

	public void setUpdateDt(Date updateDt) {
		this.updateDt = updateDt;
	}

	@Column(name = "update_by", nullable = false, length = 50)
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

	@Column(name = "latest_remark")
	public String getLatestRemark() {
		return latestRemark;
	}

	public void setLatestRemark(String latestRemark) {
		this.latestRemark = latestRemark;
	}

	// @Transient ??
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "finalListingRefNo")
	@OrderBy("finalListingRefNo ASC")
	public List<RobTrainingFinalListingRemark> getListRobTrainingFinalListingRemark() {
		return listRobTrainingFinalListingRemark;
	}

	public void setListRobTrainingFinalListingRemark(
			List<RobTrainingFinalListingRemark> listRobTrainingFinalListingRemark) {
		this.listRobTrainingFinalListingRemark = listRobTrainingFinalListingRemark;
	}

//	@Transient
//	public String getRemarkAnswer() {
//		return remarkAnswer;
//	}
//
//	public void setRemarkAnswer(String remarkAnswer) {
//		this.remarkAnswer = remarkAnswer;
//	}
}