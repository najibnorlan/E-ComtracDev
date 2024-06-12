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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "rob_counter_bankin_slip")
@Audited
public class RobCounterBankinSlip  implements Serializable{
	
	private String bankinSlipNo;
	private String branch;
	private String floor;
	private String yearCreated;
	private Double amount;
	private Date createDt;
	private String createBy;
	private Date updateDt;
	private String updateBy;
	private List<RobCounterSession> robCounterSessionList;
	
	public RobCounterBankinSlip(){
		super();
	}
	public RobCounterBankinSlip(String bankinSlipNo, String branch, String yearCreated,
			Double amount, Date createDt, String createBy, Date updateDt,
			String updateBy, List<RobCounterSession> robCounterSessionList) {
		super();
		this.bankinSlipNo = bankinSlipNo;
		this.branch = branch;
		this.amount = amount;
		this.createDt = createDt;
		this.createBy = createBy;
		this.updateDt = updateDt;
		this.updateBy = updateBy;
		this.yearCreated = yearCreated;
		this.robCounterSessionList = robCounterSessionList;
	}
	
	@Id
	@GenericGenerator(name="bankin_no", strategy="com.ssm.llp.base.hibernate.LlpIdGenerator", 
		    parameters={@Parameter(name = "genCode", value = "BANKIN_SLIP_RUNNING_NO"),
		    			@Parameter(name = "fieldCode", value = ""),
		    			@Parameter(name = "moduleCode", value = ""),
		    			@Parameter(name = "yearMonthDay", value = "yyyy"),
		    			@Parameter(name = "lastNoPattern", value = "00000")})
	@GeneratedValue(generator="bankin_no")
	@Column(name = "bankin_slip_no")
	public String getBankinSlipNo() {
		return bankinSlipNo;
	}

	public void setBankinSlipNo(String bankinSlipNo) {
		this.bankinSlipNo = bankinSlipNo;
	}

	@Column(name = "branch")
	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
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

	@OneToMany(mappedBy = "counterBankinSlipNo", fetch = FetchType.LAZY)
	public List<RobCounterSession> getRobCounterSessionList() {
		return robCounterSessionList;
	}

	public void setRobCounterSessionList(
			List<RobCounterSession> robCounterSessionList) {
		this.robCounterSessionList = robCounterSessionList;
	}

	@Column(name = "year_created")
	public String getYearCreated() {
		return yearCreated;
	}

	public void setYearCreated(String yearCreated) {
		this.yearCreated = yearCreated;
	}
	
	@Column(name = "floor_level")
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	} 
	
}
