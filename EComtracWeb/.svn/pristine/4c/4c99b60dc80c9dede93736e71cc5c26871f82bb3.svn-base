package com.ssm.llp.ezbiz.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

@Entity
@Table(name = "rob_pos_terminal_transaction")
@Audited
public class RobPosTerminalTransaction implements java.io.Serializable {
	@Id
	@Column(name = "terminal_txt_id", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer terminalTxtId;
	
    @Column(name = "counter_collection_id")
	private Integer counterCollectionId;
	
    @Column(name = "counter_session_id")
	private Integer counterSessionId;
    
    @Column(name = "payment_transaction_id")
   	private String paymentTransactionId;
	
    @Column(name = "command")
	private String command;
	
    @Column(name = "status")
	private String status;
	
    @Column(name = "credit_card_no")
	private String creditCardNo;
	
    @Column(name = "credit_card_exp_dt")
	private String creditCardExpDt;
	
    @Column(name = "credit_card_type")
	private String creditCardType;
	
    @Column(name = "approval_code")
	private String approvalCode;
	
    @Column(name = "trans_amount")
	private double transAmount;
	
    @Column(name = "net_amount")
	private double netAmount;
	
    @Column(name = "trace_no")
	private String traceNo;
	
    @Column(name = "invoice_no")
	private String invoiceNo;
	
    @Column(name = "cashier_no")
	private String cashierNo;
	
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_dt", nullable = false, length = 3594)
	private Date createDt;
	
    @Column(name = "create_by")
	private String createBy;
	
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_dt", nullable = false, length = 3594)
	private Date updateDt;
	
    @Column(name = "update_by")
	private String updateBy;   
	
	public RobPosTerminalTransaction(String posCommand,Integer counterId, Integer sessionId, String paymentTrxNo, Double trnAmt) {
		super();
		this.command = posCommand;
		this.counterCollectionId = counterId;
		this.counterSessionId = sessionId;
		this.paymentTransactionId = paymentTrxNo;
		this.transAmount = trnAmt;
	}

	public Integer getTerminalTxtId() {
		return terminalTxtId;
	}

	public void setTerminalTxtId(Integer terminalTxtId) {
		this.terminalTxtId = terminalTxtId;
	}

	public Integer getCounterCollectionId() {
		return counterCollectionId;
	}

	public void setCounterCollectionId(Integer counterCollectionId) {
		this.counterCollectionId = counterCollectionId;
	}

	public Integer getCounterSessionId() {
		return counterSessionId;
	}

	public void setCounterSessionId(Integer counterSessionId) {
		this.counterSessionId = counterSessionId;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreditCardNo() {
		return creditCardNo;
	}

	public void setCreditCardNo(String creditCardNo) {
		this.creditCardNo = creditCardNo;
	}

	public String getCreditCardExpDt() {
		return creditCardExpDt;
	}

	public void setCreditCardExpDt(String creditCardExpDt) {
		this.creditCardExpDt = creditCardExpDt;
	}

	public String getCreditCardType() {
		return creditCardType;
	}

	public void setCreditCardType(String creditCardType) {
		this.creditCardType = creditCardType;
	}

	public String getApprovalCode() {
		return approvalCode;
	}

	public void setApprovalCode(String approvalCode) {
		this.approvalCode = approvalCode;
	}

	public double getTransAmount() {
		return transAmount;
	}

	public void setTransAmount(double transAmount) {
		this.transAmount = transAmount;
	}

	public double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}

	public String getTraceNo() {
		return traceNo;
	}

	public void setTraceNo(String traceNo) {
		this.traceNo = traceNo;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getCashierNo() {
		return cashierNo;
	}

	public void setCashierNo(String cashierNo) {
		this.cashierNo = cashierNo;
	}

	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getUpdateDt() {
		return updateDt;
	}

	public void setUpdateDt(Date updateDt) {
		this.updateDt = updateDt;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getPaymentTransactionId() {
		return paymentTransactionId;
	}

	public void setPaymentTransactionId(String paymentTransactionId) {
		this.paymentTransactionId = paymentTransactionId;
	}
}
