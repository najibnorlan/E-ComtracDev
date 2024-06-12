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
@Audited
@Table(name = "rob_name_type_stat_report")
public class RobNameTypeStatReport implements Serializable{
	
	private Integer statId;
	private String statMonth;
	private String statYear;
	private Integer tradeName;
	private Integer personalName;
	private String status;
	private Date createDt;
	private String createBy;
	private Date updateDt;
	private String updateBy;

	public RobNameTypeStatReport() {
		super();
	}
	
	public RobNameTypeStatReport(Integer statId, String statMonth,
			String statYear, Integer tradeName, Integer personalName,
			String status, Date createDt, String createBy, Date updateDt,
			String updateBy) {
		super();
		this.statId = statId;
		this.statMonth = statMonth;
		this.statYear = statYear;
		this.tradeName = tradeName;
		this.personalName = personalName;
		this.status = status;
		this.createDt = createDt;
		this.createBy = createBy;
		this.updateDt = updateDt;
		this.updateBy = updateBy;
	}

	@Id
	@Column(name = "stat_id", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getStatId() {
		return statId;
	}

	public void setStatId(Integer statId) {
		this.statId = statId;
	}

	@Column(name = "stat_month")
	public String getStatMonth() {
		return statMonth;
	}

	public void setStatMonth(String statMonth) {
		this.statMonth = statMonth;
	}

	@Column(name = "stat_year")
	public String getStatYear() {
		return statYear;
	}

	public void setStatYear(String statYear) {
		this.statYear = statYear;
	}

	@Column(name = "trade_name")
	public Integer getTradeName() {
		return tradeName;
	}

	public void setTradeName(Integer tradeName) {
		this.tradeName = tradeName;
	}

	@Column(name = "personal_name")
	public Integer getPersonalName() {
		return personalName;
	}

	public void setPersonalName(Integer personalName) {
		this.personalName = personalName;
	}

	@Column(name = "status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_dt", nullable = false, length = 3594)
	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	@Column(name = "create_by", nullable = false, length = 50)
	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_dt", nullable = false, length = 3594)
	public Date getUpdateDt() {
		return updateDt;
	}

	public void setUpdateDt(Date updateDt) {
		this.updateDt = updateDt;
	}

	@Column(name = "update_by", nullable = false, length = 50)
	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}


}
