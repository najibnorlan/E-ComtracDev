package com.ssm.supplyinfo.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.ssm.llp.base.common.model.LlpFileData;

@Entity
@Table(name = "supply_info_trans_dtl")
@Audited
public class SupplyInfoTransDtl implements Serializable {

	@Id
	@Column(name = "trans_dtl_id", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long transDtlId;
    
    @Column(name = "hdr_trans_code", nullable = false, length = 25)
	private String hdrTransCode;
    
    @Column(name = "entity_no", nullable = false, length = 25)
	private String entityNo;
    
    @Column(name = "entity_type", nullable = false, length = 10)
	private String entityType;
    

    @Column(name = "entity_name")
	private String entityName;
    
    @Column(name = "prod_type", nullable = false, length = 25)
	private String prodType;
    
    @Column(name = "prod_code", nullable = false, length = 25)
	private String prodCode;
    
    @Column(name = "prod_locale", nullable = false, length = 10)
	private String prodLocale;
    
    @Column(name = "amt", precision = 16, scale=2)
	private double amt;
    
    @Column(name = "tax_amt", precision = 16, scale=2)
   	private double taxAmt;
    
    @OneToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn(name="file_data_id")
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	private SupplyInfoFileData fileData;
    
    @Column(name = "create_by", length = 50)
	private String createBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_dt", nullable = false, length = 3594)
	private Date createDt;
	
	@Column(name = "update_by", length = 50)
	private String updateBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_dt", nullable = false, length = 3594)
	private Date updateDt;
	
	@Version
	@Column(name = "row_version")
	private long rowVersion;
	
	public SupplyInfoTransDtl() {
		
	}
	public SupplyInfoTransDtl(String entityType, String entityNo, String entityName, String prodType, String prodCode, double amt, String prodLocale) {
		this.entityType = entityType;
		this.entityNo=entityNo;
		this.entityName=entityName;
		this.prodType=prodType;
		this.prodCode=prodCode;
		this.amt=amt;
		this.prodLocale=prodLocale;
	}

	public Long getTransDtlId() {
		return transDtlId;
	}

	public void setTransDtlId(Long transDtlId) {
		this.transDtlId = transDtlId;
	}
	
	public String getProdType() {
		return prodType;
	}
	public void setProdType(String prodType) {
		this.prodType = prodType;
	}
	public String getProdLocale() {
		return prodLocale;
	}
	public void setProdLocale(String prodLocale) {
		this.prodLocale = prodLocale;
	}

	public String getEntityNo() {
		return entityNo;
	}

	public void setEntityNo(String entityNo) {
		this.entityNo = entityNo;
	}

	public double getAmt() {
		return amt;
	}

	public void setAmt(double amt) {
		this.amt = amt;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getProdCode() {
		return prodCode;
	}

	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	public String getHdrTransCode() {
		return hdrTransCode;
	}

	public void setHdrTransCode(String hdrTransCode) {
		this.hdrTransCode = hdrTransCode;
	}

	public SupplyInfoFileData getFileData() {
		return fileData;
	}

	public void setFileData(SupplyInfoFileData fileData) {
		this.fileData = fileData;
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

	public long getRowVersion() {
		return rowVersion;
	}

	public void setRowVersion(long rowVersion) {
		this.rowVersion = rowVersion;
	}

	public double getTaxAmt() {
		return taxAmt;
	}

	public void setTaxAmt(double taxAmt) {
		this.taxAmt = taxAmt;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
}
