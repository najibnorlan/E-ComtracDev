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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.ssm.llp.base.common.model.LlpFileData;

@Entity
@Table(name = "rob_training_reprintcert")
//@Audited
public class RobTrainingReprintcert implements Serializable{

	private String certRefNo;
	private String transactionCode;
	private String trainingCode;
	private String trainingName;
	private String icNo;
	private String status;
	private Double amount;
	private Double gstAmount;
	private String lodgerId;
	private String lodgerName;
	private String paymentChannel;
	private String registrationChannel;
	private String receiptNo;
	private String invoiceNo;
	private String remarks;
	private Long fileId;
	private Date createDt;
	private String createBy;
	private Date updateDt;
	private String updateBy;
	private LlpFileData certificateFileData;

	public RobTrainingReprintcert() {
		super();
	}

	public RobTrainingReprintcert(String certRefNo,String transactionCode, String trainingCode, String trainingName, String icNo, String status, Double amount, Double gstAmount, String lodgerId, String lodgerName, String paymentChannel, String registrationChannel,
			String receiptNo, String invoiceNo, String remarks, Date createDt, String createBy,
			Date updateDt, String updateBy) {
		super();
		this.certRefNo = certRefNo;
		this.transactionCode = transactionCode;
		this.trainingCode = trainingCode;
		this.trainingName = trainingName;
		this.icNo = icNo;
		this.status = status;
		this.amount = amount;
		this.gstAmount = gstAmount;
		this.lodgerId = lodgerId;
		this.lodgerName = lodgerName;
		this.paymentChannel = paymentChannel;
		this.registrationChannel = registrationChannel;
		this.receiptNo = receiptNo;
		this.invoiceNo = invoiceNo;
		this.remarks = remarks;
		this.createDt = createDt;
		this.createBy = createBy;
		this.updateDt = updateDt;
		this.updateBy = updateBy;
	}

	@Id
	@GenericGenerator(name = "trans_code", strategy = "com.ssm.llp.base.hibernate.LlpIdGenerator", parameters = {
			@Parameter(name = "genCode", value = "COMTRAC_RUNNING_NO"), @Parameter(name = "fieldCode", value = "RC"),
			@Parameter(name = "moduleCode", value = ""), @Parameter(name = "yearMonthDay", value = "yyyyMMdd"),
			@Parameter(name = "lastNoPattern", value = "00000") })
	@GeneratedValue(generator = "trans_code")
	@Column(name = "cert_ref_no", nullable = false, length = 50)
	public String getCertRefNo() {
		return certRefNo;
	}

	public void setCertRefNo(String certRefNo) {
		this.certRefNo = certRefNo;
	}

	@Column(name = "transaction_code")
	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	@Column(name = "training_code")
	public String getTrainingCode() {
		return trainingCode;
	}

	public void setTrainingCode(String trainingCode) {
		this.trainingCode = trainingCode;
	}

	@Column(name = "training_name")
	public String getTrainingName() {
		return trainingName;
	}

	public void setTrainingName(String trainingName) {
		this.trainingName = trainingName;
	}

	@Column(name = "ic_no")
	public String getIcNo() {
		return icNo;
	}

	public void setIcNo(String icNo) {
		this.icNo = icNo;
	}

	@Column(name = "file_id")
	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	@Column(name = "status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "amount")
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@Column(name = "gst_amount")
	public Double getGstAmount() {
		return gstAmount;
	}

	public void setGstAmount(Double gstAmount) {
		this.gstAmount = gstAmount;
	}

	@Column(name = "lodger_id")
	public String getLodgerId() {
		return lodgerId;
	}

	public void setLodgerId(String lodgerId) {
		this.lodgerId = lodgerId;
	}

	@Column(name = "lodger_name")
	public String getLodgerName() {
		return lodgerName;
	}

	public void setLodgerName(String lodgerName) {
		this.lodgerName = lodgerName;
	}

	@Column(name = "payment_channel")
	public String getPaymentChannel() {
		return paymentChannel;
	}

	public void setPaymentChannel(String paymentChannel) {
		this.paymentChannel = paymentChannel;
	}

	@Column(name = "registration_channel")
	public String getRegistrationChannel() {
		return registrationChannel;
	}

	public void setRegistrationChannel(String registrationChannel) {
		this.registrationChannel = registrationChannel;
	}

	@Column(name = "receipt_no")
	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	@Column(name = "invoice_no")
	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	@Column(name = "remarks")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_dt", nullable = true, length = 3594)
	public Date getCreateDt() {
		return this.createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	@Column(name = "create_by", nullable = true, length = 50)
	public String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_dt", nullable = true, length = 3594)
	public Date getUpdateDt() {
		return this.updateDt;
	}

	public void setUpdateDt(Date updateDt) {
		this.updateDt = updateDt;
	}

	@Column(name = "update_by", nullable = true, length = 50)
	public String getUpdateBy() {
		return this.updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
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
}
