package com.ssm.llp.ezbiz.model;

import java.io.Serializable;
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

@Entity
@Table(name = "rob_training_transaction")
@Audited
public class RobTrainingTransaction implements Serializable {

	private String transactionCode;
	private String status;
	private Double amount;
	private Double gstAmount;
	private Integer totalPax;
	private String lodgerId;
	private String lodgerName;
	private String paymentChannel;
	private String registrationChannel;
	private String receiptNo;
	private String invoiceNo;
	private String louLoaRefNo;
	private String remarks;
	private Date createDt;
	private String createBy;
	private Date updateDt;
	private String updateBy;
	private RobTrainingConfig trainingId;
	private List<RobTrainingParticipant> robTrainingParticipantList;

	private List<FileUpload> lpoFileUpload;
	private LlpFileData lpoDocData;
	private String isHasLpoDoc;

	public RobTrainingTransaction() {
		super();
	}

	public RobTrainingTransaction(String transactionCode, String status, Double amount, Double gstAmount,
			Integer totalPax, String lodgerId, String lodgerName, String paymentChannel, String registrationChannel,
			String receiptNo, String invoiceNo, String louLoaRefNo, String remarks, Date createDt, String createBy,
			Date updateDt, String updateBy, RobTrainingConfig trainingId,
			List<RobTrainingParticipant> robTrainingParticipantList) {
		super();
		this.transactionCode = transactionCode;
		this.status = status;
		this.amount = amount;
		this.gstAmount = gstAmount;
		this.totalPax = totalPax;
		this.lodgerId = lodgerId;
		this.lodgerName = lodgerName;
		this.paymentChannel = paymentChannel;
		this.registrationChannel = registrationChannel;
		this.receiptNo = receiptNo;
		this.invoiceNo = invoiceNo;
		this.louLoaRefNo = louLoaRefNo;
		this.remarks = remarks;
		this.createDt = createDt;
		this.createBy = createBy;
		this.updateDt = updateDt;
		this.updateBy = updateBy;
		this.trainingId = trainingId;
		this.robTrainingParticipantList = robTrainingParticipantList;
	}

	@Id
	@GenericGenerator(name = "trans_code", strategy = "com.ssm.llp.base.hibernate.LlpIdGenerator", parameters = {
			@Parameter(name = "genCode", value = "COMTRAC_RUNNING_NO"), @Parameter(name = "fieldCode", value = "CP"),
			@Parameter(name = "moduleCode", value = ""), @Parameter(name = "yearMonthDay", value = "yyyyMMdd"),
			@Parameter(name = "lastNoPattern", value = "00000") })
	@GeneratedValue(generator = "trans_code")
	@Column(name = "transaction_code", nullable = false, length = 50)
	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	@JoinColumn(name = "training_id", referencedColumnName = "training_id")
	@ManyToOne(fetch = FetchType.EAGER)
	public RobTrainingConfig getTrainingId() {
		return trainingId;
	}

	public void setTrainingId(RobTrainingConfig trainingId) {
		this.trainingId = trainingId;
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

	@Column(name = "total_pax")
	public Integer getTotalPax() {
		return totalPax;
	}

	public void setTotalPax(Integer totalPax) {
		this.totalPax = totalPax;
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

	@Column(name = "lou_loa_ref_no")
	public String getLouLoaRefNo() {
		return louLoaRefNo;
	}

	public void setLouLoaRefNo(String louLoaRefNo) {
		this.louLoaRefNo = louLoaRefNo;
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

	@OneToMany(mappedBy = "robTrainingTransaction", fetch = FetchType.LAZY)
	public List<RobTrainingParticipant> getRobTrainingParticipantList() {
		return robTrainingParticipantList;
	}

	public void setRobTrainingParticipantList(List<RobTrainingParticipant> robTrainingParticipantList) {
		this.robTrainingParticipantList = robTrainingParticipantList;
	}

	@Transient
	public List<FileUpload> getlpoFileUpload() {
		return lpoFileUpload;
	}

	public void setFileUploadTmp(List<FileUpload> lpoFileUpload) {
		this.lpoFileUpload = lpoFileUpload;
	}

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)//usual is FetchType.LAZY
	@JoinColumn(name = "lpo_doc_id")
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	public LlpFileData getLpoDocData() {
		return lpoDocData;
	}

	public void setLpoDocData(LlpFileData lpoDocData) {
		this.lpoDocData = lpoDocData;
	}

	@Column(name = "is_has_lpo_doc", length = 1)
	public String getIsHasLpoDoc() {
		return isHasLpoDoc;
	}

	public void setIsHasLpoDoc(String isHasLpoDoc) {
		this.isHasLpoDoc = isHasLpoDoc;
	}
}
