package com.ssm.llp.mod1.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
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
@Audited //ISAM error-fixed by create table rob_user_oku_aud manually
@Table(name = "rob_user_oku")
public class RobUserOku implements java.io.Serializable {

    private long srlOku;
    private String okuRefNo;
    private LlpUserProfile userProfile;
    private long idRegUser;
    private Date applicationDt;
    private Date approvalDt;
    private String approveBy;
    private String okuCategory;
    private String okuCardNo;
    private String isHasSupportingDoc;
    private LlpFileData docDataId;
    private String okuRegStatus;
    private String remarks;
   // private String isOku;
	private Date createDt;
	private String createBy;
	private Date updateDt;
	private String updateBy;
	
	//transient
	private List<FileUpload> userOkuFileUpload; //upload attachment
	private Boolean declarationChkBox = Boolean.FALSE;
	private Boolean discardAppChkBox = Boolean.FALSE;
	private List<RobFormNotes> listRobFormNotes = new ArrayList<RobFormNotes>(0);
	private String queryAnswer;
	
    public RobUserOku() {
	}
	
    public RobUserOku(long srlOku, String okuRefNo, LlpUserProfile userProfile,
    		long idRegUser, Date applicationDt, Date approvalDt,
    		String approveBy, String okuCategory, String okuCardNo,
    		String okuRegStatus, String remarks, String isOku, LlpFileData docDataId) {
		this.srlOku = srlOku;
		this.okuRefNo = okuRefNo;
		this.userProfile = userProfile;
		this.idRegUser = idRegUser;
		this.applicationDt = applicationDt;
		this.approvalDt = approvalDt;
		this.approveBy = approveBy;
		this.okuCategory = okuCategory;
		this.okuCardNo = okuCardNo;
		this.okuRegStatus = okuRegStatus;
		this.remarks = remarks;
		this.docDataId=docDataId;
	}
	
    public RobUserOku(long srlOku,String okuRefNo) {
		this.srlOku = srlOku;
		this.okuRefNo = okuRefNo;
	}
    
    
	@Id
    @GenericGenerator(name="ref_no", strategy="com.ssm.llp.base.hibernate.LlpIdGenerator", 
    parameters={@Parameter(name = "genCode", value = "ROB_USER_OKU_RUNNING_NO"),
    			@Parameter(name = "fieldCode", value = ""),
    			@Parameter(name = "moduleCode", value = "OKU"),
    			@Parameter(name = "yearMonthDay", value = "yyyyMMdd"),
    			@Parameter(name = "lastNoPattern", value = "0000")})
    @GeneratedValue(generator="ref_no")
	@Column(name = "oku_ref_no", nullable = false, length = 25)
	public String getOkuRefNo() {
		return okuRefNo;
	}	
	
	public void setOkuRefNo(String okuRefNo) {
		this.okuRefNo = okuRefNo;
	}
	
	
	@Column(name = "srl_oku", columnDefinition="serial", unique = true, nullable = false, updatable=false,insertable = false)
	public long getSrlOku() {
		return srlOku;
	}

	public void setSrlOku(long srlOku) {
		this.srlOku = srlOku;
	}

	@Column(name = "id_reg_user", nullable = false)
	public long getIdRegUser() {
		return idRegUser;
	}

	public void setIdRegUser(long idRegUser) {
		this.idRegUser = idRegUser;
	}
	

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "application_dt", length = 3594)
	public Date getApplicationDt() {
		return applicationDt;
	}

	public void setApplicationDt(Date applicationDt) {
		this.applicationDt = applicationDt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "approval_dt", length = 3594)
	public Date getApprovalDt() {
		return approvalDt;
	}

	public void setApprovalDt(Date approvalDt) {
		this.approvalDt = approvalDt;
	}

	@Column(name = "approve_by", length = 50)
	public String getApproveBy() {
		return approveBy;
	}

	public void setApproveBy(String approveBy) {
		this.approveBy = approveBy;
	}

	@Column(name = "oku_category", length = 150)
	public String getOkuCategory() {
		return okuCategory;
	}

	public void setOkuCategory(String okuCategory) {
		this.okuCategory = okuCategory;
	}

	@Column(name = "oku_card_no", length = 50)
	public String getOkuCardNo() {
		return okuCardNo;
	}

	public void setOkuCardNo(String okuCardNo) {
		this.okuCardNo = okuCardNo;
	}
	
	@Column(name = "is_has_supporting_doc", length = 1)
	public String getIsHasSupportingDoc() {
		return isHasSupportingDoc;
	}

	public void setIsHasSupportingDoc(String isHasSupportingDoc) {
		this.isHasSupportingDoc = isHasSupportingDoc;
	}
	

	@OneToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL) //initialize LAZY di daoImpl..
    @JoinColumn(name="doc_data_id")
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	public LlpFileData getDocDataId() {
		return docDataId;
	}

	public void setDocDataId(LlpFileData docDataId) {
		this.docDataId = docDataId;
	}
	
	@OneToOne(fetch = FetchType.EAGER) //OneToMany or ManyToOne ??
    @JoinColumn(name="user_ref_no") //field table rob_user_oku..guna referencedColumnName utk join bukan primary key.
	public LlpUserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(LlpUserProfile userProfile) {
		this.userProfile = userProfile;
	}

	
	//	@Transient ??
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "robFormCode")
	@OrderBy("robFormNoteId ASC")
	public List<RobFormNotes> getListRobFormNotes() {
		return listRobFormNotes;
	}

	public void setListRobFormNotes(List<RobFormNotes> listRobFormNotes) {
		this.listRobFormNotes = listRobFormNotes;
	}
	
	@Column(name = "oku_reg_status", length = 50)
	public String getOkuRegStatus() {
		return okuRegStatus;
	}

	public void setOkuRegStatus(String okuRegStatus) {
		this.okuRegStatus = okuRegStatus;
	}

//	@Column(name = "is_oku", nullable = false, length = 1)
//	public String getIsOku() {
//		return isOku;
//	}
//
//	public void setIsOku(String isOku) {
//		this.isOku = isOku;
//	}

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
	public List<FileUpload> getUserOkuFileUpload() {
		return userOkuFileUpload;
	}

	public void setUserOkuFileUpload(List<FileUpload> userOkuFileUpload) {
		this.userOkuFileUpload = userOkuFileUpload;
	}

	@Transient
	public Boolean getDeclarationChkBox() {
		return declarationChkBox;
	}

	public void setDeclarationChkBox(Boolean declarationChkBox) {
		this.declarationChkBox = declarationChkBox;
	}

	@Transient
	public String getQueryAnswer() {
		return queryAnswer;
	}

	public void setQueryAnswer(String queryAnswer) {
		this.queryAnswer = queryAnswer;
	}

	@Transient
	public Boolean getDiscardAppChkBox() {
		return discardAppChkBox;
	}

	public void setDiscardAppChkBox(Boolean discardAppChkBox) {
		this.discardAppChkBox = discardAppChkBox;
	}

}
