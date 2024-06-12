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

import org.hibernate.envers.Audited;

@Entity
@Table(name = "rob_counter_collection")
@Audited
public class RobCounterCollection implements Serializable {

	private Integer robCounterCollectionId;
	private String ipAddress;
	private String counterName;
	private String floorLevel;
	private String counterType;
	private String branch;
	private Date createDt;
	private String createBy;
	private Date updateDt;
	private String updateBy;
	private String isActive;
	private String softDelete;
	private List<RobCounterSession> robCounterSessionList;
	
	public RobCounterCollection() {
		super();
	}

	public RobCounterCollection(String ipAddress, String counterName,
			String floorLevel, String counterType, String branch,
			Date createDt, String createBy, Date updateDt, String updateBy,
			List<RobCounterSession> robCounterSessionList) {
		super();
		this.ipAddress = ipAddress;
		this.counterName = counterName;
		this.floorLevel = floorLevel;
		this.counterType = counterType;
		this.branch = branch;
		this.createDt = createDt;
		this.createBy = createBy;
		this.updateDt = updateDt;
		this.updateBy = updateBy;
		this.robCounterSessionList = robCounterSessionList;
	}
	
	@Id
	@Column(name = "rob_counter_collection_id", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getRobCounterCollectionId() {
		return robCounterCollectionId;
	}

	public void setRobCounterCollectionId(Integer robCounterCollectionId) {
		this.robCounterCollectionId = robCounterCollectionId;
	}
	
	@Column(name = "ip_address", unique=true)
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	@Column(name = "counter_name", length = 50)
	public String getCounterName() {
		return counterName;
	}
	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}
	
	@Column(name = "floor_level", length = 50)
	public String getFloorLevel() {
		return floorLevel;
	}
	public void setFloorLevel(String floorLevel) {
		this.floorLevel = floorLevel;
	}
	
	@Column(name = "counter_type", length = 50)
	public String getCounterType() {
		return counterType;
	}
	public void setCounterType(String counterType) {
		this.counterType = counterType;
	}
	
	@Column(name = "branch", length = 50)
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
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

	@OneToMany(mappedBy = "counterIpAddress", fetch = FetchType.LAZY)
	public List<RobCounterSession> getCounterSession() {
		return robCounterSessionList;
	}

	public void setCounterSession(List<RobCounterSession> robCounterSessionList) {
		this.robCounterSessionList = robCounterSessionList;
	}

	@Column(name = "is_active")
	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	@Column(name = "soft_delete")
	public String getSoftDelete() {
		return softDelete;
	}

	public void setSoftDelete(String softDelete) {
		this.softDelete = softDelete;
	}


	
}
