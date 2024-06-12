package com.ssm.llp.ezbiz.model;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class RobParticipantRegTraining implements Serializable{
	
	//training participant
	private Integer participantId;
	private String name;
	private String icNo;
	private String lsNo;
	private String membershipNo;
	private String designation;
	private String company;
	
	private Date createDt;
	private String createBy;
	private Date updateDt;
	private String updateBy;
	
//	private String address1;
//	private String address2;
//	private String address3;
//	private String telNo;
//	private String faxNo;
//	private String email;
//	private Double amount;
//	private Double gstAmount;
//	private String gstCode;
//	private String feeType;
//	private String isAttend;
//  private String postcode;
//  private String city;
//  private String state;
//	private String diet;
//	private RobTrainingTransaction robTrainingTransaction;
//	private String isEligible;
//	private Long fileId;
//	private LlpFileData certificateFileData;
	
	//training config
	private Integer trainingId;
	private String trainingCode;
	private String trainingName;
	private String trainingDesc;
	private Date trainingStartDt;
	private Date trainingEndDt;
	
	//training transaction
	private String transactionCode;
	private String trainingTransactionStatus;
	private String lodgerId;
	private String lodgerName;
	
	
	public RobParticipantRegTraining() {
		super();
	}
	
	public RobParticipantRegTraining(Integer participantId, String name,
			String icNo, String lsNo, String membershipNo, String designation,
			String company, Integer trainingId, String trainingCode, String trainingName,
			String trainingDesc, Date trainingStartDt, Date trainingEndDt, String trainingTransactionStatus,
			Date createDt, String createBy, Date updateDt, String updateBy, String lodgerId, String lodgerName, String transactionCode ) {
		super();
		this.participantId = participantId;
		this.name = name;
		this.icNo = icNo;
		this.lsNo = lsNo;
		this.membershipNo = membershipNo;
		this.designation = designation;
		this.company = company;
		this.trainingId = trainingId;
		this.trainingCode = trainingCode;
		this.trainingName = trainingName;
		this.trainingDesc = trainingDesc;
		this.trainingStartDt = trainingStartDt;
		this.trainingEndDt = trainingEndDt;
		this.trainingTransactionStatus = trainingTransactionStatus;
		this.createDt = createDt;
		this.createBy = createBy;
		this.updateDt = updateDt;
		this.updateBy = updateBy;
		this.lodgerId = lodgerId;
		this.lodgerName = lodgerName;
		this.transactionCode = transactionCode;
		
	}

	public Integer getParticipantId() {
		return participantId;
	}

	public void setParticipantId(Integer participantId) {
		this.participantId = participantId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcNo() {
		return icNo;
	}

	public void setIcNo(String icNo) {
		this.icNo = icNo;
	}

	public String getLsNo() {
		return lsNo;
	}

	public void setLsNo(String lsNo) {
		this.lsNo = lsNo;
	}

	public String getMembershipNo() {
		return membershipNo;
	}

	public void setMembershipNo(String membershipNo) {
		this.membershipNo = membershipNo;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}


	public Date getCreateDt() {
		return this.createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	public String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getUpdateDt() {
		return this.updateDt;
	}

	public void setUpdateDt(Date updateDt) {
		this.updateDt = updateDt;
	}

	public String getUpdateBy() {
		return this.updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Integer getTrainingId() {
		return trainingId;
	}

	public void setTrainingId(Integer trainingId) {
		this.trainingId = trainingId;
	}

	public String getTrainingCode() {
		return trainingCode;
	}

	public void setTrainingCode(String trainingCode) {
		this.trainingCode = trainingCode;
	}

	public String getTrainingName() {
		return trainingName;
	}

	public void setTrainingName(String trainingName) {
		this.trainingName = trainingName;
	}

	public String getTrainingDesc() {
		return trainingDesc;
	}

	public void setTrainingDesc(String trainingDesc) {
		this.trainingDesc = trainingDesc;
	}

	public Date getTrainingStartDt() {
		return trainingStartDt;
	}

	public void setTrainingStartDt(Date trainingStartDt) {
		this.trainingStartDt = trainingStartDt;
	}

	public Date getTrainingEndDt() {
		return trainingEndDt;
	}

	public void setTrainingEndDt(Date trainingEndDt) {
		this.trainingEndDt = trainingEndDt;
	}

	public String getTrainingTransactionStatus() {
		return trainingTransactionStatus;
	}

	public void setTrainingTransactionStatus(String trainingTransactionStatus) {
		this.trainingTransactionStatus = trainingTransactionStatus;
	}

	public String getLodgerId() {
		return lodgerId;
	}

	public void setLodgerId(String lodgerId) {
		this.lodgerId = lodgerId;
	}

	public String getLodgerName() {
		return lodgerName;
	}

	public void setLodgerName(String lodgerName) {
		this.lodgerName = lodgerName;
	}

	
	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

//	@Override
//    public boolean equals(Object o) {
//
//        if (o == this) return true;
//        if (!(o instanceof RobParticipantRegTraining)) {
//            return false;
//        }
//
//        RobParticipantRegTraining participant = (RobParticipantRegTraining) o;
//
//        return participant.icNo.equals(icNo);
//    }

}


