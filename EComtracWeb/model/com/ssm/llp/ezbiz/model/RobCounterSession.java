package com.ssm.llp.ezbiz.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name = "rob_counter_session")
@Audited
public class RobCounterSession implements Serializable {

	private Integer sessionId;
	private String userId;
	private String floorLevel;
	private String branch;
	private Date checkinDate;
	private Date checkoutDate;
	private String checkoutBy;
	private String balancingStatus;
	private Date createDt;
	private String createBy;
	private Date approveDt;
	private String approveBy;
	private Date verifyDt;
	private String verifyBy;
	private Date balancingDt;
	private String balancingBy;
	private Integer isOpen;
	private RobCounterCollection counterIpAddress;
	private RobCounterBankinSlip counterBankinSlipNo;
	
//	private List<LlpPaymentTransaction> llpPaymentTransactionList;
	
	public RobCounterSession(Integer sessionId, String branch,
			RobCounterCollection counterIpAddress, String userId,
			String floorLevel, Date checkinDate, Date checkoutDate, String checkoutBy,
			String balancingStatus, Date createDt, String createBy,
			Date approveDt, String approveBy, Integer isOpen,
			Date verifyDt, String verifyBy, RobCounterBankinSlip counterBankinSlipNo,
			Date balancingDt, String balancingBy) {
		super();
		this.sessionId = sessionId;
		this.counterIpAddress = counterIpAddress;
		this.userId = userId;
		this.floorLevel = floorLevel;
		this.checkinDate = checkinDate;
		this.checkoutDate = checkoutDate;
		this.balancingStatus = balancingStatus;
		this.createDt = createDt;
		this.createBy = createBy;
		this.approveDt = approveDt;
		this.approveBy = approveBy;
		this.isOpen = isOpen;
		this.checkoutBy = checkoutBy;
		this.verifyDt = verifyDt;
		this.verifyBy = verifyBy;
		this.balancingDt = balancingDt;
		this.isOpen = isOpen;
		this.counterBankinSlipNo = counterBankinSlipNo;
		this.branch = branch;
//		this.llpPaymentTransactionList = llpPaymentTransactionList;
	}

	public RobCounterSession() {
		super();
	}

	@Id
	@Column(name = "session_id", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getSessionId() {
		return sessionId;
	}

	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}

	@JoinColumn(name = "counter_ip_address", referencedColumnName = "ip_address")
	@ManyToOne(fetch = FetchType.EAGER)
	public RobCounterCollection getCounterIpAddress() {
		return counterIpAddress;
	}

	public void setCounterIpAddress(RobCounterCollection counterIpAddress) {
		this.counterIpAddress = counterIpAddress;
	}

	@Column(name = "user_id")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "floor_level")
	public String getFloorLevel() {
		return floorLevel;
	}

	public void setFloorLevel(String floorLevel) {
		this.floorLevel = floorLevel;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "checkin_date")
	public Date getCheckinDate() {
		return checkinDate;
	}

	public void setCheckinDate(Date checkinDate) {
		this.checkinDate = checkinDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "checkout_date")
	public Date getCheckoutDate() {
		return checkoutDate;
	}

	public void setCheckoutDate(Date checkoutDate) {
		this.checkoutDate = checkoutDate;
	}
	
	@Column(name = "checkout_by")
	public String getCheckoutBy() {
		return checkoutBy;
	}

	public void setCheckoutBy(String checkoutBy) {
		this.checkoutBy = checkoutBy;
	}

	@Column(name = "balancing_status")
	public String getBalancingStatus() {
		return balancingStatus;
	}

	public void setBalancingStatus(String balancingStatus) {
		this.balancingStatus = balancingStatus;
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
	@Column(name = "approve_dt",  length = 3594)
	public Date getApproveDt() {
		return this.approveDt;
	}

	public void setApproveDt(Date approveDt) {
		this.approveDt = approveDt;
	}

	@Column(name = "approve_by", length = 50)
	public String getApproveBy() {
		return this.approveBy;
	}

	public void setApproveBy(String approveBy) {
		this.approveBy = approveBy;
	}
	
	@Column(name = "is_open")
	public Integer getIsOpen() {
		return this.isOpen;
	}

	public void setIsOpen(Integer isOpen) {
		this.isOpen = isOpen;
	}

	@Column(name = "verify_dt")
	public Date getVerifyDt() {
		return verifyDt;
	}

	public void setVerifyDt(Date verifyDt) {
		this.verifyDt = verifyDt;
	}

	@Column(name = "verify_by")
	public String getVerifyBy() {
		return verifyBy;
	}

	public void setVerifyBy(String verifyBy) {
		this.verifyBy = verifyBy;
	}

	@Column(name = "balancing_dt")
	public Date getBalancingDt() {
		return balancingDt;
	}

	public void setBalancingDt(Date balancingDt) {
		this.balancingDt = balancingDt;
	}

	@Column(name = "balancing_by")
	public String getBalancingBy() {
		return balancingBy;
	}

	public void setBalancingBy(String balancingBy) {
		this.balancingBy = balancingBy;
	}

	@JoinColumn(name = "counter_bankin_slip_no", referencedColumnName = "bankin_slip_no")
	@ManyToOne(fetch = FetchType.EAGER)
	public RobCounterBankinSlip getCounterBankinSlipNo() {
		return counterBankinSlipNo;
	}

	public void setCounterBankinSlipNo(RobCounterBankinSlip counterBankinSlipNo) {
		this.counterBankinSlipNo = counterBankinSlipNo;
	}
	
	@Column(name = "branch")
	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}
	
//	@OneToMany(mappedBy = "counterSessionId", fetch = FetchType.LAZY)
//	public List<LlpPaymentTransaction> getPaymentTransactionList() {
//		return llpPaymentTransactionList;
//	}
//
//	public void setPaymentTransactionList(List<LlpPaymentTransaction> llpPaymentTransactionList) {
//		this.llpPaymentTransactionList = llpPaymentTransactionList;
//	}

}
