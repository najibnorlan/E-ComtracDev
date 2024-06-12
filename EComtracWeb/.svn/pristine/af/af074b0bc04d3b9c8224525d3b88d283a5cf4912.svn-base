package com.ssm.llp.mod1.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.ssm.llp.base.common.model.LlpFileData;
import com.ssm.llp.ezbiz.model.RobFormNotes;

/**
 * Represents contacts within the example application.
 *
 * <p>User: Nick Heudecker</p>
 */

@Entity
@Audited //utk table audit rob_incentive_aud
@Table(name = "rob_incentive")
public class RobIncentive implements java.io.Serializable {

	private long srlIncentive;
    private String userRefNo; //LlpUserProfile userProfile;
    private String idNo;
    private String okuRefNo; //RobUserOku userOku
    private String incentiveType;
    private String incentiveForm;
    private String robFormCode;
    private String brNo;
    private String checkDigit;
    private Date incentiveApplicationDt;
    private Date incentiveApprovalDt;
    private String applicationStatus;
    private String processingBranch;
	private Date createDt;
	private String createBy;
	private Date updateDt;
	private String updateBy;
	
    public RobIncentive() {
	}

    @Id
    @Column(name = "srl_incentive", columnDefinition="serial", unique = true, nullable = false, updatable=false,insertable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getSrlIncentive() {
		return srlIncentive;
	}

	public void setSrlIncentive(long srlIncentive) {
		this.srlIncentive = srlIncentive;
	}
    
	@Column(name = "user_ref_no", length = 25)
	public String getUserRefNo() {
		return userRefNo;
	}

	public void setUserRefNo(String userRefNo) {
		this.userRefNo = userRefNo;
	}

	@Column(name = "id_no", length = 20)
	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	@Column(name = "oku_ref_no", length = 25)
	public String getOkuRefNo() {
		return okuRefNo;
	}

	public void setOkuRefNo(String okuRefNo) {
		this.okuRefNo = okuRefNo;
	}

	@Column(name = "incentive_type", length = 5)
	public String getIncentiveType() {
		return incentiveType;
	}

	public void setIncentiveType(String incentiveType) {
		this.incentiveType = incentiveType;
	}

	@Column(name = "incentive_form", length = 5)
	public String getIncentiveForm() {
		return incentiveForm;
	}

	public void setIncentiveForm(String incentiveForm) {
		this.incentiveForm = incentiveForm;
	}

	@Column(name = "rob_form_code", length = 50)
	public String getRobFormCode() {
		return robFormCode;
	}

	public void setRobFormCode(String robFormCode) {
		this.robFormCode = robFormCode;
	}

	@Column(name = "br_no", length = 20)
	public String getBrNo() {
		return brNo;
	}

	public void setBrNo(String brNo) {
		this.brNo = brNo;
	}

	@Column(name = "check_digit", length = 1)
	public String getCheckDigit() {
		return checkDigit;
	}

	public void setCheckDigit(String checkDigit) {
		this.checkDigit = checkDigit;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "incentive_application_dt", length = 3594)
	public Date getIncentiveApplicationDt() {
		return incentiveApplicationDt;
	}

	public void setIncentiveApplicationDt(Date incentiveApplicationDt) {
		this.incentiveApplicationDt = incentiveApplicationDt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "incentive_approval_dt", length = 3594)
	public Date getIncentiveApprovalDt() {
		return incentiveApprovalDt;
	}

	public void setIncentiveApprovalDt(Date incentiveApprovalDt) {
		this.incentiveApprovalDt = incentiveApprovalDt;
	}

	@Column(name = "application_status", length = 5)
	public String getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus(String applicationStatus) {
		this.applicationStatus = applicationStatus;
	}

	@Column(name = "processing_branch", length = 5)
	public String getProcessingBranch() {
		return processingBranch;
	}

	public void setProcessingBranch(String processingBranch) {
		this.processingBranch = processingBranch;
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
	
}
