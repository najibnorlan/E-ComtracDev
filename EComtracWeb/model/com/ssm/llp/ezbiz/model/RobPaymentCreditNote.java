package com.ssm.llp.ezbiz.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.envers.Audited;

import com.ssm.llp.base.common.model.LlpPaymentReceipt;

@Entity
@Table(name = "rob_payment_credit_note")
@Audited
public class RobPaymentCreditNote implements Serializable {

	private String creditNoteNo;
	private Double amount;
	private Date createDt;
	private String createBy;
	private Date updateDt;
	private String updateBy;
	private LlpPaymentReceipt paymentReceiptNo;
	private RobCounterSession counterSessionId;
	private String cnTransactionNo;
	private Date approveDt;
	private String approveBy;
	private String reason;
	
	public RobPaymentCreditNote(){
		super();
	}
	
	public RobPaymentCreditNote(String creditNoteNo, LlpPaymentReceipt paymentReceiptNo,
			Double amount, Date createDt, String createBy, Date updateDt,
			String updateBy, RobCounterSession counterSessionId,
			String cnTransactionNo) {
		super();
		this.creditNoteNo = creditNoteNo;
		this.paymentReceiptNo = paymentReceiptNo;
		this.amount = amount;
		this.createDt = createDt;
		this.createBy = createBy;
		this.updateDt = updateDt;
		this.updateBy = updateBy;
		this.counterSessionId = counterSessionId;
		this.cnTransactionNo = cnTransactionNo;
	}

	@Id
	@GenericGenerator(name="credit_note_no", strategy="com.ssm.llp.base.hibernate.LlpIdGenerator", 
		    parameters={@Parameter(name = "genCode", value = "CREDIT_NOTE_RUNNING_NO"),
		    			@Parameter(name = "fieldCode", value = "EBCN"),
		    			@Parameter(name = "moduleCode", value = ""),
		    			@Parameter(name = "yearMonthDay", value = ""),
		    			@Parameter(name = "lastNoPattern", value = "000000000")})
	@GeneratedValue(generator="credit_note_no")
	@Column(name = "credit_note_no")
	public String getCreditNoteNo() {
		return creditNoteNo;
	}

	public void setCreditNoteNo(String creditNoteNo) {
		this.creditNoteNo = creditNoteNo;
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
	
	@OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name="payment_receipt_no")
	public LlpPaymentReceipt getPaymentReceiptNo() {
		return paymentReceiptNo;
	}

	public void setPaymentReceiptNo(LlpPaymentReceipt paymentReceiptNo) {
		this.paymentReceiptNo = paymentReceiptNo;
	}

	@JoinColumn(name = "counter_session_id", referencedColumnName = "session_id")
	@ManyToOne(fetch = FetchType.LAZY)
	public RobCounterSession getCounterSessionId() {
		return counterSessionId;
	}

	public void setCounterSessionId(RobCounterSession counterSessionId) {
		this.counterSessionId = counterSessionId;
	}

	@Column(name = "cn_transaction_no")
	public String getCnTransactionNo() {
		return cnTransactionNo;
	}

	public void setCnTransactionNo(String cnTransactionNo) {
		this.cnTransactionNo = cnTransactionNo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "approve_dt")
	public Date getApproveDt() {
		return approveDt;
	}

	public void setApproveDt(Date approveDt) {
		this.approveDt = approveDt;
	}

	@Column(name = "approve_by")
	public String getApproveBy() {
		return approveBy;
	}

	public void setApproveBy(String approveBy) {
		this.approveBy = approveBy;
	}

	@Column(name = "reason")
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
