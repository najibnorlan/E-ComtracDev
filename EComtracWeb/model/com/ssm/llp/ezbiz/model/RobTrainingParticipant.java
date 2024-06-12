package com.ssm.llp.ezbiz.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.ssm.llp.base.common.model.LlpFileData;

@Entity
@Table(name = "rob_training_participant")
@Audited
public class RobTrainingParticipant implements Serializable {

	private Integer participantId;
	private String name;
	private String icNo;
	private Date dob;
	private String lsNo;
	private String membershipNo;
	private String designation;
	private String company;
	private String address1;
	private String address2;
	private String address3;
	private String telNo;
	private String faxNo;
	private String email;
	private Double amount;
	private Double gstAmount;
	private String gstCode;
	private String feeType;
	private String isAttend;
	private Date createDt;
	private String createBy;
	private Date updateDt;
	private String updateBy;
	private String postcode;
	private String city;
	private String state;
	private String diet;
	private String gender;
	private RobTrainingTransaction robTrainingTransaction;
	private String isEligible;
	private Long fileId;
	private LlpFileData certificateFileData;
	private String remarkAbsent;
	private String occupation_code;
	private String job_title;
	private Boolean checkIsAttend;
	private Boolean checkIsEligible;
	private Boolean checkIsRefund;
	private String isRefund;

	public RobTrainingParticipant() {
		super();
	}

	public RobTrainingParticipant(Integer participantId, String name, String icNo, Date dob, String lsNo,
			String membershipNo, String designation, String company, String address1, String address2, String address3,
			String telNo, String faxNo, String email, Double amount, Date createDt, String createBy, Date updateDt,
			String updateBy, String postcode, String city, String state, String diet,
			RobTrainingTransaction robTrainingTransaction, String feeType, String remarkAbsent, String gender,
			String occupation_code, String job_title, Boolean checkIsAttend, Boolean checkIsEligible,
			Boolean checkIsRefund, String isRefund) {
		super();
		this.participantId = participantId;
		this.name = name;
		this.icNo = icNo;
		this.dob = dob;
		this.lsNo = lsNo;
		this.membershipNo = membershipNo;
		this.designation = designation;
		this.company = company;
		this.address1 = address1;
		this.address2 = address2;
		this.address3 = address3;
		this.telNo = telNo;
		this.faxNo = faxNo;
		this.email = email;
		this.amount = amount;
		this.createDt = createDt;
		this.createBy = createBy;
		this.updateDt = updateDt;
		this.updateBy = updateBy;
		this.postcode = postcode;
		this.city = city;
		this.state = state;
		this.diet = diet;
		this.robTrainingTransaction = robTrainingTransaction;
		this.feeType = feeType;
		this.remarkAbsent = remarkAbsent;
		this.gender = gender;
		this.occupation_code = occupation_code;
		this.job_title = job_title;
		this.isRefund = isRefund;
	}

	@Id
	@Column(name = "participant_id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getParticipantId() {
		return participantId;
	}

	public void setParticipantId(Integer participantId) {
		this.participantId = participantId;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "ic_no")
	public String getIcNo() {
		return icNo;
	}

	public void setIcNo(String icNo) {
		this.icNo = icNo;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "dob")
	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	@Column(name = "ls_no")
	public String getLsNo() {
		return lsNo;
	}

	public void setLsNo(String lsNo) {
		this.lsNo = lsNo;
	}

	@Column(name = "membership_no")
	public String getMembershipNo() {
		return membershipNo;
	}

	public void setMembershipNo(String membershipNo) {
		this.membershipNo = membershipNo;
	}

	@Column(name = "designation")
	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	@Column(name = "occupation_code")
	public String getOccupation_code() {
		return occupation_code;
	}

	public void setOccupation_code(String occupation_code) {
		this.occupation_code = occupation_code;
	}

	@Column(name = "job_title")
	public String getJob_title() {
		return job_title;
	}

	public void setJob_title(String job_title) {
		this.job_title = job_title;
	}

	@Column(name = "company")
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@Column(name = "address_1")
	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	@Column(name = "address_2")
	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	@Column(name = "address_3")
	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	@Column(name = "tel_no")
	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	@Column(name = "fax_no")
	public String getFaxNo() {
		return faxNo;
	}

	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}

	@Column(name = "email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "amount")
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
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

	@JoinColumn(name = "transaction_code", referencedColumnName = "transaction_code")
	@ManyToOne(fetch = FetchType.LAZY)
	public RobTrainingTransaction getRobTrainingTransaction() {
		return robTrainingTransaction;
	}

	public void setRobTrainingTransaction(RobTrainingTransaction robTrainingTransaction) {
		this.robTrainingTransaction = robTrainingTransaction;
	}

	@Column(name = "fee_type")
	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	@Column(name = "gender")
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Override
	public boolean equals(Object o) {

		if (o == this)
			return true;
		if (!(o instanceof RobTrainingParticipant)) {
			return false;
		}

		RobTrainingParticipant participant = (RobTrainingParticipant) o;

		return participant.icNo.equals(icNo);
	}

	@Column(name = "gst_amount")
	public Double getGstAmount() {
		return gstAmount;
	}

	public void setGstAmount(Double gstAmount) {
		this.gstAmount = gstAmount;
	}

	@Column(name = "is_attend")
	public String getIsAttend() {
		return isAttend;
	}

	public void setIsAttend(String isAttend) {
		this.isAttend = isAttend;
	}

	@Column(name = "postcode")
	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	@Column(name = "city")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "state")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name = "diet")
	public String getDiet() {
		return diet;
	}

	public void setDiet(String diet) {
		this.diet = diet;
	}

	@Column(name = "gst_code")
	public String getGstCode() {
		return gstCode;
	}

	public void setGstCode(String gstCode) {
		this.gstCode = gstCode;
	}

	@Column(name = "is_eligible")
	public String getIsEligible() {
		return isEligible;
	}

	public void setIsEligible(String isEligible) {
		this.isEligible = isEligible;
	}

	@Column(name = "file_id")
	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "file_id", insertable = false, updatable = false)
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	public LlpFileData getCertificateFileData() {
		return certificateFileData;
	}

	public void setCertificateFileData(LlpFileData certificateFileData) {
		this.certificateFileData = certificateFileData;
	}

	@Transient
	public Boolean getCheckIsAttend() {
		return checkIsAttend;
	}

	public void setCheckIsAttend(Boolean checkIsAttend) {
		this.checkIsAttend = checkIsAttend;
	}

	@Transient
	public Boolean getCheckIsEligible() {
		return checkIsEligible;
	}

	public void setCheckIsEligible(Boolean checkIsEligible) {
		this.checkIsEligible = checkIsEligible;
	}

	@Transient
	public Boolean getCheckIsRefund() {
		return checkIsRefund;
	}

	public void setCheckIsRefund(Boolean checkIsRefund) {
		this.checkIsRefund = checkIsRefund;
	}

	@Column(name = "remark_absent")
	public String getRemarkAbsent() {
		return remarkAbsent;
	}

	public void setRemarkAbsent(String remarkAbsent) {
		this.remarkAbsent = remarkAbsent;
	}

	@Column(name = "is_refund")
	public String getIsRefund() {
		return isRefund;
	}

	public void setIsRefund(String isRefund) {
		this.isRefund = isRefund;
	}
	
	
}
