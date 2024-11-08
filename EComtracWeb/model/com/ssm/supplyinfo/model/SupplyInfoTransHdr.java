package com.ssm.supplyinfo.model;


// Generated Aug 4, 2015 4:58:53 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.envers.Audited;

/**
 * RobRenewal generated by hbm2java
 */
@Entity
@Table(name = "supply_info_trans_hdr")
@Audited
public class SupplyInfoTransHdr implements java.io.Serializable {
	
	private String transCode;
	private String status;
	private Date paymentDt;
	private double totalAmt;
	private double taxAmt;
	private Date expDate;
	private Date createDt;
	private String ownerBy;
	private String createBy;
	private Date updateDt;
	private String updateBy;
	private long rowVersion;

	
	private List<SupplyInfoTransDtl> listSupplyInfoTransDtl; 
	
	public SupplyInfoTransHdr() {
	}


	@Id
    @GenericGenerator(name="ref_no", strategy="com.ssm.llp.base.hibernate.LlpIdGenerator", 
    parameters={@Parameter(name = "genCode", value = "SUPP_INFO_TRANS_NO"),
    			@Parameter(name = "fieldCode", value = "SP"),
    			@Parameter(name = "moduleCode", value = ""),
    			@Parameter(name = "yearMonthDay", value = "yyyyMMdd"),
    			@Parameter(name = "lastNoPattern", value = "000000")})
    @GeneratedValue(generator="ref_no")
	@Column(name = "trans_code", nullable = false, length = 50)
	public String getTransCode() {
		return this.transCode;
	}

	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "exp_date", length = 2052)
	public Date getExpDate() {
		return this.expDate;
	}

	public void setExpDate(Date expDate) {
		this.expDate = expDate;
	}

	@Column(name = "total_amt", precision = 16, scale=2)
	public double getTotalAmt() {
		return this.totalAmt;
	}

	public void setTotalAmt(double totalAmt) {
		this.totalAmt = totalAmt;
	}

	@Column(name = "status", length = 10)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "payment_dt", length = 2052)
	public Date getPaymentDt() {
		return paymentDt;
	}

	public void setPaymentDt(Date paymentDt) {
		this.paymentDt = paymentDt;
	}
		
	@Column(name = "tax_amt", precision = 16, scale=2)
	public double getTaxAmt() {
		return taxAmt;
	}

	public void setTaxAmt(double taxAmt) {
		this.taxAmt = taxAmt;
	}
	
		
	@Version
	@Column(name = "row_version")
	public long getRowVersion() {
		return rowVersion;
	}

	public void setRowVersion(long rowVersion) {
		this.rowVersion = rowVersion;
	}


	@OneToMany(fetch = FetchType.LAZY, mappedBy = "hdrTransCode")
	public List<SupplyInfoTransDtl> getListSupplyInfoTransDtl() {
		return listSupplyInfoTransDtl;
	}


	public void setListSupplyInfoTransDtl(List<SupplyInfoTransDtl> listSupplyInfoTransDtl) {
		this.listSupplyInfoTransDtl = listSupplyInfoTransDtl;
	}


	@Column(name = "owner_by", nullable = false, length = 50)
	public String getOwnerBy() {
		return ownerBy;
	}


	public void setOwnerBy(String ownerBy) {
		this.ownerBy = ownerBy;
	}
}
