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

@Entity
@Table(name = "rob_training_dashboard")
//@Audited
public class RobTrainingDashboard implements Serializable{
	
	private int dashboardId;
	private int param1;
	private int param2;
	private int param3;
	private int param4;
	private int param5;
	private int param6;
	private int param7;
	private int param8;
	private int param9;
	private int param10;
	private int param11;
	private int param12;
	private int total;
	private String type;
	private String status;
	private String updateBy;
	private String createBy;
	private Date updateDt;
	private Date createDt;
	private int pax;
	private int year;
	private Date transDt;

	public RobTrainingDashboard() {
		super();
	}

	public RobTrainingDashboard(int dashboardId, int param1,int param2,int param3,int param4,int param5,int param6,int param7,int param8,int param9,
			int param10,int param11,int param12,int total,String type,String status,String updateBy,String createBy,Date updateDt,Date createDt,int pax,
			int year,Date transDt) {
		super();
		this.dashboardId = dashboardId;
		this.param1 = param1;
		this.param2 = param2;
		this.param3 = param3;
		this.param4 = param4;
		this.param5 = param5;
		this.param6 = param6;
		this.param7 = param7;
		this.param8 = param8;
		this.param9 = param9;
		this.param10 = param10;
		this.param11 = param11;
		this.param12 = param12;
		this.param9 = param9;
		this.total = total;
		this.type = type;
		this.status = status;
		this.pax = pax;
		this.year = year;
		this.transDt = transDt;
		this.createDt = createDt;
		this.createBy = createBy;
		this.updateDt = updateDt;
		this.updateBy = updateBy;
	}

	@Id
	@Column(name = "dashboard_id", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getDashboardId() {
		return dashboardId;
	}

	public void setDashboardId(int dashboardId) {
		this.dashboardId = dashboardId;
	}

	@Column(name = "param_1")
	public int getParam1() {
		return param1;
	}

	public void setParam1(int param1) {
		this.param1 = param1;
	}
	@Column(name = "param_2")
	public int getParam2() {
		return param2;
	}

	public void setParam2(int param2) {
		this.param2 = param2;
	}
	@Column(name = "param_3")
	public int getParam3() {
		return param3;
	}

	public void setParam3(int param3) {
		this.param3 = param3;
	}
	@Column(name = "param_4")
	public int getParam4() {
		return param4;
	}

	public void setParam4(int param4) {
		this.param4 = param4;
	}
	@Column(name = "param_5")
	public int getParam5() {
		return param5;
	}

	public void setParam5(int param5) {
		this.param5 = param5;
	}
	@Column(name = "param_6")
	public int getParam6() {
		return param6;
	}

	public void setParam6(int param6) {
		this.param6 = param6;
	}
	@Column(name = "param_7")
	public int getParam7() {
		return param7;
	}

	public void setParam7(int param7) {
		this.param7 = param7;
	}
	@Column(name = "param_8")
	public int getParam8() {
		return param8;
	}

	public void setParam8(int param8) {
		this.param8 = param8;
	}
	@Column(name = "param_9")
	public int getParam9() {
		return param9;
	}

	public void setParam9(int param9) {
		this.param9 = param9;
	}
	@Column(name = "param_10")
	public int getParam10() {
		return param10;
	}

	public void setParam10(int param10) {
		this.param10 = param10;
	}
	@Column(name = "param_11")
	public int getParam11() {
		return param11;
	}

	public void setParam11(int param11) {
		this.param11 = param11;
	}
	@Column(name = "param_12")
	public int getParam12() {
		return param12;
	}

	public void setParam12(int param12) {
		this.param12 = param12;
	}
	@Column(name = "total")
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	@Column(name = "type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	@Column(name = "status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	@Column(name = "update_by", nullable = true, length = 50)
	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	@Column(name = "create_by", nullable = true, length = 50)
	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_dt", nullable = true, length = 3594)
	public Date getUpdateDt() {
		return updateDt;
	}

	public void setUpdateDt(Date updateDt) {
		this.updateDt = updateDt;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_dt", nullable = true, length = 3594)
	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}
	@Column(name = "pax")
	public int getPax() {
		return pax;
	}

	public void setPax(int pax) {
		this.pax = pax;
	}
	@Column(name = "year")
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	@Column(name = "trans_dt")
	public Date getTransDt() {
		return transDt;
	}

	public void setTransDt(Date transDt) {
		this.transDt = transDt;
	}

	
}
