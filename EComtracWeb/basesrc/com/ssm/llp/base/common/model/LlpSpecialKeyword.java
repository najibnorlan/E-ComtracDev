package com.ssm.llp.base.common.model;

// Generated Dec 10, 2012 3:34:56 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

/**
 * Specialkeywords generated by hbm2java
 */
@Entity
@Table(name = "rob_specialkeywords")
@Audited
public class LlpSpecialKeyword implements java.io.Serializable {

	private long id_keyword;
	private String vchkeywords;
	private String chrkeywordtype;
	private String vchcreateby;
	private Date dtcreatedate;
	private String vchupdateby;
	private Date dtupdatedate;

	public LlpSpecialKeyword() {
	}

	@Id
	@Column(name = "id_keyword", unique = true, nullable = false, updatable=false)
	public long getId_keyword() {
		return this.id_keyword;
	}

	public void setId_keyword(long id_keyword) {
		this.id_keyword = id_keyword;
	}
	
	@Column(name = "vchkeywords", nullable = false, length = 50)
	public String getVchkeywords() {
		return this.vchkeywords;
	}

	public void setVchkeywords(String vchkeywords) {
		this.vchkeywords = vchkeywords;
	}

	@Column(name = "chrkeywordtype", length = 1)
	public String getChrkeywordtype() {
		return this.chrkeywordtype;
	}

	public void setChrkeywordtype(String chrkeywordtype) {
		this.chrkeywordtype = chrkeywordtype;
	}

	@Column(name = "vchcreateby", nullable = false, length = 25)
	public String getVchcreateby() {
		return this.vchcreateby;
	}

	public void setVchcreateby(String vchcreateby) {
		this.vchcreateby = vchcreateby;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dtcreatedate", nullable = false, length = 3594)
	public Date getDtcreatedate() {
		return this.dtcreatedate;
	}

	public void setDtcreatedate(Date dtcreatedate) {
		this.dtcreatedate = dtcreatedate;
	}

	@Column(name = "vchupdateby", nullable = false, length = 10)
	public String getVchupdateby() {
		return this.vchupdateby;
	}

	public void setVchupdateby(String vchupdateby) {
		this.vchupdateby = vchupdateby;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dtupdatedate", nullable = false, length = 3594)
	public Date getDtupdatedate() {
		return this.dtupdatedate;
	}

	public void setDtupdatedate(Date dtupdatedate) {
		this.dtupdatedate = dtupdatedate;
	}


}
