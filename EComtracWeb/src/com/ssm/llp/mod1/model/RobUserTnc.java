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
import org.hibernate.annotations.Where;
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
@Audited 
@Table(name = "rob_user_tnc")
public class RobUserTnc implements java.io.Serializable {

    private long srlTnc;
    private String userRefNo;
    private long idRegUser;
    private String loginId;
    private String idNo;
    private String tncType;
    private String isAgree;
    private Date agreementDt;
    private String agreementStatus;
    private String remarks;
    private Date createDt;
	private String createBy;
	private Date updateDt;
	private String updateBy;
	
	// private LlpFileData agreementDataId;
	
	//transient
	private Boolean declarationChkBox = Boolean.FALSE;
	private String myKadNo;
	
    public RobUserTnc() {
	}

    @Id
    @Column(name = "srl_tnc", columnDefinition="serial", unique = true, nullable = false, updatable=false,insertable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
	public long getSrlTnc() {
		return srlTnc;
	}

	public void setSrlTnc(long srlTnc) {
		this.srlTnc = srlTnc;
	}

	@Column(name = "user_ref_no", length = 25, nullable = false)
	public String getUserRefNo() {
		return userRefNo;
	}

	public void setUserRefNo(String userRefNo) {
		this.userRefNo = userRefNo;
	}

	@Column(name = "id_reg_user")
	public long getIdRegUser() {
		return idRegUser;
	}

	public void setIdRegUser(long idRegUser) {
		this.idRegUser = idRegUser;
	}

	@Column(name = "login_id")
	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	@Column(name = "id_no", length = 20)
	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	@Column(name = "tnc_type")
	public String getTncType() {
		return tncType;
	}

	public void setTncType(String tncType) {
		this.tncType = tncType;
	}

	@Column(name = "is_agree", length = 1)
	public String getIsAgree() {
		return isAgree;
	}

	public void setIsAgree(String isAgree) {
		this.isAgree = isAgree;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "agreement_dt", length = 3594)
	public Date getAgreementDt() {
		return agreementDt;
	}

	public void setAgreementDt(Date agreementDt) {
		this.agreementDt = agreementDt;
	}

	@Column(name = "agreement_status", length = 5)
	public String getAgreementStatus() {
		return agreementStatus;
	}

	public void setAgreementStatus(String agreementStatus) {
		this.agreementStatus = agreementStatus;
	}

	@Column(name = "remarks", length = 255)
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

	@Transient
	public Boolean getDeclarationChkBox() {
		return declarationChkBox;
	}

	public void setDeclarationChkBox(Boolean declarationChkBox) {
		this.declarationChkBox = declarationChkBox;
	}

	@Transient
	public String getMyKadNo() {
		return myKadNo;
	}

	public void setMyKadNo(String myKadNo) {
		this.myKadNo = myKadNo;
	}

}
