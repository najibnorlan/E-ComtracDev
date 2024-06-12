package com.ssm.llp.ezbiz.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

@Entity
@Table(name = "rob_counter_balancing")
@Audited

public class RobCounterBalancing implements Serializable{

	private Integer balancingId;
	private Double bankNote;
	private Integer quantity;
	private Double amount;
	private RobCounterSession counterSessionId;
	private Date createDt;
	private String createBy;
	private Date updateDt;
	private String updateBy;
	
	public RobCounterBalancing() {
		super();
	}

	public RobCounterBalancing(Integer balancingId, Double bankNote, Integer quantity,
			Double amount, RobCounterSession counterSessionId) {
		super();
		this.balancingId = balancingId;
		this.bankNote = bankNote;
		this.quantity = quantity;
		this.amount = amount;
		this.counterSessionId = counterSessionId;
	}

	@Id
	@Column(name = "balancing_id", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getBalancingId() {
		return balancingId;
	}

	public void setBalancingId(Integer balancingId) {
		this.balancingId = balancingId;
	}

	@Column(name = "bank_note")
	public Double getBankNote() {
		return bankNote;
	}

	public void setBankNote(Double bankNote) {
		this.bankNote = bankNote;
	}

	@Column(name = "quantity")
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Column(name = "amount")
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@JoinColumn(name = "counter_session_id", referencedColumnName = "session_id")
	@ManyToOne(fetch = FetchType.LAZY)
	public RobCounterSession getCounterSessionId() {
		return counterSessionId;
	}

	public void setCounterSessionId(RobCounterSession counterSessionId) {
		this.counterSessionId = counterSessionId;
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
