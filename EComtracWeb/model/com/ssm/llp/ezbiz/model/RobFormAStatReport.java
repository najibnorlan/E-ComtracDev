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
@Table(name = "rob_form_a_stat_report")
public class RobFormAStatReport implements Serializable{
	
	private Integer statId;
	private String statMonth;
	private String statYear;
	private Integer onlineSeller;
	private Integer student;
	private Integer oku;
	private Integer incubator;
	private Integer statTotal;
	private String formAStatus;
	private Date createDt;
	private String createBy;
	private Date updateDt;
	private String updateBy;
	
	public RobFormAStatReport(){
	}
	
	public RobFormAStatReport(Integer statId, String statMonth,
			String statYear, Integer onlineSeller, Integer student,
			Integer oku, Integer incubator, Integer statTotal, 
			String formAStatus, Date createDt,
			String createBy, Date updateDt, String updateBy) {
		this.statId = statId;
		this.statMonth = statMonth;
		this.statYear = statYear;
		this.onlineSeller = onlineSeller;
		this.student = student;
		this.oku = oku;
		this.incubator = incubator;
		this.statTotal = statTotal;
		this.formAStatus = formAStatus;
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

	@Column(name = "online_seller")
	public Integer getOnlineSeller() {
		return onlineSeller;
	}

	public void setOnlineSeller(Integer onlineSeller) {
		this.onlineSeller = onlineSeller;
	}

	@Column(name = "student")
	public Integer getStudent() {
		return student;
	}

	public void setStudent(Integer student) {
		this.student = student;
	}

	@Column(name = "oku")
	public Integer getOku() {
		return oku;
	}

	public void setOku(Integer oku) {
		this.oku = oku;
	}

	@Column(name = "incubator")
	public Integer getIncubator() {
		return incubator;
	}

	public void setIncubator(Integer incubator) {
		this.incubator = incubator;
	}

	@Column(name = "stat_total")
	public Integer getStatTotal() {
		return statTotal;
	}

	public void setStatTotal(Integer statTotal) {
		this.statTotal = statTotal;
	}
	
	@Column(name = "form_a_status")
	public String getFormAStatus() {
		return formAStatus;
	}

	public void setFormAStatus(String formAStatus) {
		this.formAStatus = formAStatus;
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
