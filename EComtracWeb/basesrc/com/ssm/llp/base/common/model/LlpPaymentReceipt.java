package com.ssm.llp.base.common.model;

// Generated Dec 6, 2012 1:24:13 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.envers.Audited;

import com.ssm.llp.ezbiz.model.RobCounterSession;

/**
 * LlpPaymentReceipt generated by hbm2java
 */
@Entity
@Table(name = "rob_payment_receipt")
@Audited
public class LlpPaymentReceipt implements java.io.Serializable {

	private String receiptNo;
	private String taxInvoiceNo;
//	private LlpPaymentTransaction llpPaymentTransaction;
	private String transactionId;
	private Date createDt;
	private String createBy;
	private Date updateDt;
	private String updateBy;
	private Double cashReceived;
	private Double balance;
	private Double totalAmount;
	private RobCounterSession counterSessionId;
	private Integer isCancel;
	private String remarks;
//	private RobPaymentCreditNote paymentCreditNote;
	
	
	private String paymentCardType;
	private String paymentCardBank;

	public LlpPaymentReceipt() {
	}

	public LlpPaymentReceipt(String receiptNo, String taxInvoiceNo,
			LlpPaymentTransaction llpPaymentTransaction, Date createDt,
			String createBy, Date updateDt, String updateBy, Integer isCancel,
			RobCounterSession counterSessionId, Double cashReceived,
			Double balance, Double totalAmount, String remarks) {
		this.receiptNo = receiptNo;
		this.taxInvoiceNo = taxInvoiceNo;
//		this.llpPaymentTransaction = llpPaymentTransaction;
		this.createDt = createDt;
		this.createBy = createBy;
		this.updateDt = updateDt;
		this.updateBy = updateBy;
		this.counterSessionId = counterSessionId;
		this.cashReceived = cashReceived;
		this.balance = balance;
		this.totalAmount = totalAmount;
		this.isCancel = isCancel;
		this.remarks = remarks;
//		this.paymentCreditNote = paymentCreditNote;
	} 

	@Id
    @GenericGenerator(name="receipt_no", strategy="com.ssm.llp.base.hibernate.LlpIdGenerator", 
    parameters={@Parameter(name = "genCode", value = "RECIEPT_RUNNING_NO"),
    			@Parameter(name = "fieldCode", value = "EB"),
    			@Parameter(name = "moduleCode", value = ""),
    			@Parameter(name = "yearMonthDay", value = "yyyyMMdd"),
    			@Parameter(name = "lastNoPattern", value = "000000")})
    @GeneratedValue(generator="receipt_no")
    @Column(name = "receipt_no", length = 50)
	public String getReceiptNo() {
		return this.receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}
	 
	@Column(name = "tax_invoice_no", length = 50)
	public String getTaxInvoiceNo() {
		return taxInvoiceNo;
	}

	public void setTaxInvoiceNo(String taxInvoiceNo) {
		this.taxInvoiceNo = taxInvoiceNo;
	}

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "transaction_id", nullable = false)
//	public LlpPaymentTransaction getLlpPaymentTransaction() {
//		return this.llpPaymentTransaction;
//	}
//
//	public void setLlpPaymentTransaction(
//			LlpPaymentTransaction llpPaymentTransaction) {
//		this.llpPaymentTransaction = llpPaymentTransaction;
//	}

	@Column(name = "transaction_id", nullable = false, length = 50)
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
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
	
	@JoinColumn(name = "counter_session_id", referencedColumnName = "session_id")
	@ManyToOne(fetch = FetchType.EAGER)
	public RobCounterSession getCounterSessionId() {
		return counterSessionId;
	}

	public void setCounterSessionId(RobCounterSession counterSessionId) {
		this.counterSessionId = counterSessionId;
	}

	@Column(name = "cash_received")
	public Double getCashReceived() {
		return cashReceived;
	}

	public void setCashReceived(Double cashReceived) {
		this.cashReceived = cashReceived;
	}

	@Column(name = "balance")
	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	@Column(name = "total_amount")
	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
//
//	@JoinColumn(name = "receipt_no", referencedColumnName = "payment_receipt_no")
//	@ManyToOne(fetch = FetchType.EAGER)
//	public RobPaymentCreditNote getPaymentCreditNote() {
//		return paymentCreditNote;
//	}
//
//	public void setPaymentCreditNote(RobPaymentCreditNote paymentCreditNote) {
//		this.paymentCreditNote = paymentCreditNote;
//	}

	@Column(name = "is_cancel")
	public Integer getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(Integer isCancel) {
		this.isCancel = isCancel;
	}

	@Column(name="remarks")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Column(name="payment_card_type")
	public String getPaymentCardType() {
		return paymentCardType;
	}

	public void setPaymentCardType(String paymentCardType) {
		this.paymentCardType = paymentCardType;
	}

	@Column(name="payment_card_bank")
	public String getPaymentCardBank() {
		return paymentCardBank;
	}

	public void setPaymentCardBank(String paymentCardBank) {
		this.paymentCardBank = paymentCardBank;
	}

}
