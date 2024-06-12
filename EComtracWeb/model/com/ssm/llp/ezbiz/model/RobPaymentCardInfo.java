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
@Table(name = "rob_payment_card_info")
@Audited
public class RobPaymentCardInfo implements java.io.Serializable {
	@Id
	@Column(name = "bin_no", unique = true, nullable = false)
	private String binNo;

    @Column(name = "card_brand")
	private String cardBrand;
	
    @Column(name = "card_type")
	private String cardType;
	
    @Column(name = "card_category")
	private String cardCategory;
	
    @Column(name = "card_issuer")
	private String cardIssuer;
    
    @Column(name = "card_issuer_code")
   	private String cardIssuerCode;
	
    @Column(name = "country")
	private String country;
	
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
	
	public RobPaymentCardInfo() {
		super();
	}

	public String getBinNo() {
		return binNo;
	}

	public void setBinNo(String binNo) {
		this.binNo = binNo;
	}

	public String getCardBrand() {
		return cardBrand;
	}

	public void setCardBrand(String cardBrand) {
		this.cardBrand = cardBrand;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardCategory() {
		return cardCategory;
	}

	public void setCardCategory(String cardCategory) {
		this.cardCategory = cardCategory;
	}

	public String getCardIssuer() {
		return cardIssuer;
	}

	public void setCardIssuer(String cardIssuer) {
		this.cardIssuer = cardIssuer;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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

	public String getCardIssuerCode() {
		return cardIssuerCode;
	}

	public void setCardIssuerCode(String cardIssuerCode) {
		this.cardIssuerCode = cardIssuerCode;
	}

}
