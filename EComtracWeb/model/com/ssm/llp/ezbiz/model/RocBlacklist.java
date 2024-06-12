package com.ssm.llp.ezbiz.model;

// Generated Dec 6, 2012 8:57:52 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "blacklist")
public class RocBlacklist implements java.io.Serializable {

	private long srlblacklistkeycode;
	private String vchblacklistid;
	private String vchblacklistcode;
	private String vchentitytype;
	private String vchentityno;
	private String vchofficerid;
	private String vchorderreferenceno;
	private Date dtorderdate;
	private String vchunit;
	private String vchblacklistdesc;
	private String vchnotes;
	private String vchblackliststatus;
	private String vchcreateby;
	private String vchupdateby;
	private Date dtcreatedate;
	private Date dtupdatedate;
	private String vchbranchid;
	private String vchdesignation;

	public RocBlacklist() {
	}

	public RocBlacklist(long srlblacklistkeycode, String vchofficerid,
			String vchcreateby, String vchupdateby, Date dtcreatedate,
			Date dtupdatedate) {
		this.srlblacklistkeycode = srlblacklistkeycode;
		this.vchofficerid = vchofficerid;
		this.vchcreateby = vchcreateby;
		this.vchupdateby = vchupdateby;
		this.dtcreatedate = dtcreatedate;
		this.dtupdatedate = dtupdatedate;
	}

	public RocBlacklist(long srlblacklistkeycode, String vchblacklistid,
			String vchblacklistcode, String vchentitytype, String vchentityno,
			String vchofficerid, String vchorderreferenceno, Date dtorderdate,
			String vchunit, String vchblacklistdesc, String vchnotes,
			String vchblackliststatus, String vchcreateby, String vchupdateby,
			Date dtcreatedate, Date dtupdatedate, String vchbranchid,
			String vchdesignation) {
		this.srlblacklistkeycode = srlblacklistkeycode;
		this.vchblacklistid = vchblacklistid;
		this.vchblacklistcode = vchblacklistcode;
		this.vchentitytype = vchentitytype;
		this.vchentityno = vchentityno;
		this.vchofficerid = vchofficerid;
		this.vchorderreferenceno = vchorderreferenceno;
		this.dtorderdate = dtorderdate;
		this.vchunit = vchunit;
		this.vchblacklistdesc = vchblacklistdesc;
		this.vchnotes = vchnotes;
		this.vchblackliststatus = vchblackliststatus;
		this.vchcreateby = vchcreateby;
		this.vchupdateby = vchupdateby;
		this.dtcreatedate = dtcreatedate;
		this.dtupdatedate = dtupdatedate;
		this.vchbranchid = vchbranchid;
		this.vchdesignation = vchdesignation;
	}

	@Id
	@Column(name = "srlblacklistkeycode", nullable = false)
	public long getSrlblacklistkeycode() {
		return this.srlblacklistkeycode;
	}

	public void setSrlblacklistkeycode(long srlblacklistkeycode) {
		this.srlblacklistkeycode = srlblacklistkeycode;
	}

	@Column(name = "vchblacklistid", length = 10)
	public String getVchblacklistid() {
		return this.vchblacklistid;
	}

	public void setVchblacklistid(String vchblacklistid) {
		this.vchblacklistid = vchblacklistid;
	}

	@Column(name = "vchblacklistcode", length = 10)
	public String getVchblacklistcode() {
		return this.vchblacklistcode;
	}

	public void setVchblacklistcode(String vchblacklistcode) {
		this.vchblacklistcode = vchblacklistcode;
	}

	@Column(name = "vchentitytype", length = 10)
	public String getVchentitytype() {
		return this.vchentitytype;
	}

	public void setVchentitytype(String vchentitytype) {
		this.vchentitytype = vchentitytype;
	}

	@Column(name = "vchentityno", length = 12)
	public String getVchentityno() {
		return this.vchentityno;
	}

	public void setVchentityno(String vchentityno) {
		this.vchentityno = vchentityno;
	}

	@Column(name = "vchofficerid", nullable = false, length = 10)
	public String getVchofficerid() {
		return this.vchofficerid;
	}

	public void setVchofficerid(String vchofficerid) {
		this.vchofficerid = vchofficerid;
	}

	@Column(name = "vchorderreferenceno", length = 30)
	public String getVchorderreferenceno() {
		return this.vchorderreferenceno;
	}

	public void setVchorderreferenceno(String vchorderreferenceno) {
		this.vchorderreferenceno = vchorderreferenceno;
	}

	@Column(name = "dtorderdate", length = 2052)
	public Date getDtorderdate() {
		return this.dtorderdate;
	}

	public void setDtorderdate(Date dtorderdate) {
		this.dtorderdate = dtorderdate;
	}

	@Column(name = "vchunit", length = 10)
	public String getVchunit() {
		return this.vchunit;
	}

	public void setVchunit(String vchunit) {
		this.vchunit = vchunit;
	}

	@Column(name = "vchblacklistdesc", length = 100)
	public String getVchblacklistdesc() {
		return this.vchblacklistdesc;
	}

	public void setVchblacklistdesc(String vchblacklistdesc) {
		this.vchblacklistdesc = vchblacklistdesc;
	}

	@Column(name = "vchnotes", length = 200)
	public String getVchnotes() {
		return this.vchnotes;
	}

	public void setVchnotes(String vchnotes) {
		this.vchnotes = vchnotes;
	}

	@Column(name = "vchblackliststatus", length = 20)
	public String getVchblackliststatus() {
		return this.vchblackliststatus;
	}

	public void setVchblackliststatus(String vchblackliststatus) {
		this.vchblackliststatus = vchblackliststatus;
	}

	@Column(name = "vchcreateby", nullable = false, length = 10)
	public String getVchcreateby() {
		return this.vchcreateby;
	}

	public void setVchcreateby(String vchcreateby) {
		this.vchcreateby = vchcreateby;
	}

	@Column(name = "vchupdateby", nullable = false, length = 10)
	public String getVchupdateby() {
		return this.vchupdateby;
	}

	public void setVchupdateby(String vchupdateby) {
		this.vchupdateby = vchupdateby;
	}

	@Column(name = "dtcreatedate", nullable = false, length = 3594)
	public Date getDtcreatedate() {
		return this.dtcreatedate;
	}

	public void setDtcreatedate(Date dtcreatedate) {
		this.dtcreatedate = dtcreatedate;
	}

	@Column(name = "dtupdatedate", nullable = false, length = 3594)
	public Date getDtupdatedate() {
		return this.dtupdatedate;
	}

	public void setDtupdatedate(Date dtupdatedate) {
		this.dtupdatedate = dtupdatedate;
	}

	@Column(name = "vchbranchid", length = 10)
	public String getVchbranchid() {
		return this.vchbranchid;
	}

	public void setVchbranchid(String vchbranchid) {
		this.vchbranchid = vchbranchid;
	}

	@Column(name = "vchdesignation", length = 20)
	public String getVchdesignation() {
		return this.vchdesignation;
	}

	public void setVchdesignation(String vchdesignation) {
		this.vchdesignation = vchdesignation;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof RocBlacklist))
			return false;
		RocBlacklist castOther = (RocBlacklist) other;

		return (this.getSrlblacklistkeycode() == castOther
				.getSrlblacklistkeycode())
				&& ((this.getVchblacklistid() == castOther.getVchblacklistid()) || (this
						.getVchblacklistid() != null
						&& castOther.getVchblacklistid() != null && this
						.getVchblacklistid().equals(
								castOther.getVchblacklistid())))
				&& ((this.getVchblacklistcode() == castOther
						.getVchblacklistcode()) || (this.getVchblacklistcode() != null
						&& castOther.getVchblacklistcode() != null && this
						.getVchblacklistcode().equals(
								castOther.getVchblacklistcode())))
				&& ((this.getVchentitytype() == castOther.getVchentitytype()) || (this
						.getVchentitytype() != null
						&& castOther.getVchentitytype() != null && this
						.getVchentitytype()
						.equals(castOther.getVchentitytype())))
				&& ((this.getVchentityno() == castOther.getVchentityno()) || (this
						.getVchentityno() != null
						&& castOther.getVchentityno() != null && this
						.getVchentityno().equals(castOther.getVchentityno())))
				&& ((this.getVchofficerid() == castOther.getVchofficerid()) || (this
						.getVchofficerid() != null
						&& castOther.getVchofficerid() != null && this
						.getVchofficerid().equals(castOther.getVchofficerid())))
				&& ((this.getVchorderreferenceno() == castOther
						.getVchorderreferenceno()) || (this
						.getVchorderreferenceno() != null
						&& castOther.getVchorderreferenceno() != null && this
						.getVchorderreferenceno().equals(
								castOther.getVchorderreferenceno())))
				&& ((this.getDtorderdate() == castOther.getDtorderdate()) || (this
						.getDtorderdate() != null
						&& castOther.getDtorderdate() != null && this
						.getDtorderdate().equals(castOther.getDtorderdate())))
				&& ((this.getVchunit() == castOther.getVchunit()) || (this
						.getVchunit() != null && castOther.getVchunit() != null && this
						.getVchunit().equals(castOther.getVchunit())))
				&& ((this.getVchblacklistdesc() == castOther
						.getVchblacklistdesc()) || (this.getVchblacklistdesc() != null
						&& castOther.getVchblacklistdesc() != null && this
						.getVchblacklistdesc().equals(
								castOther.getVchblacklistdesc())))
				&& ((this.getVchnotes() == castOther.getVchnotes()) || (this
						.getVchnotes() != null
						&& castOther.getVchnotes() != null && this
						.getVchnotes().equals(castOther.getVchnotes())))
				&& ((this.getVchblackliststatus() == castOther
						.getVchblackliststatus()) || (this
						.getVchblackliststatus() != null
						&& castOther.getVchblackliststatus() != null && this
						.getVchblackliststatus().equals(
								castOther.getVchblackliststatus())))
				&& ((this.getVchcreateby() == castOther.getVchcreateby()) || (this
						.getVchcreateby() != null
						&& castOther.getVchcreateby() != null && this
						.getVchcreateby().equals(castOther.getVchcreateby())))
				&& ((this.getVchupdateby() == castOther.getVchupdateby()) || (this
						.getVchupdateby() != null
						&& castOther.getVchupdateby() != null && this
						.getVchupdateby().equals(castOther.getVchupdateby())))
				&& ((this.getDtcreatedate() == castOther.getDtcreatedate()) || (this
						.getDtcreatedate() != null
						&& castOther.getDtcreatedate() != null && this
						.getDtcreatedate().equals(castOther.getDtcreatedate())))
				&& ((this.getDtupdatedate() == castOther.getDtupdatedate()) || (this
						.getDtupdatedate() != null
						&& castOther.getDtupdatedate() != null && this
						.getDtupdatedate().equals(castOther.getDtupdatedate())))
				&& ((this.getVchbranchid() == castOther.getVchbranchid()) || (this
						.getVchbranchid() != null
						&& castOther.getVchbranchid() != null && this
						.getVchbranchid().equals(castOther.getVchbranchid())))
				&& ((this.getVchdesignation() == castOther.getVchdesignation()) || (this
						.getVchdesignation() != null
						&& castOther.getVchdesignation() != null && this
						.getVchdesignation().equals(
								castOther.getVchdesignation())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getSrlblacklistkeycode();
		result = 37
				* result
				+ (getVchblacklistid() == null ? 0 : this.getVchblacklistid()
						.hashCode());
		result = 37
				* result
				+ (getVchblacklistcode() == null ? 0 : this
						.getVchblacklistcode().hashCode());
		result = 37
				* result
				+ (getVchentitytype() == null ? 0 : this.getVchentitytype()
						.hashCode());
		result = 37
				* result
				+ (getVchentityno() == null ? 0 : this.getVchentityno()
						.hashCode());
		result = 37
				* result
				+ (getVchofficerid() == null ? 0 : this.getVchofficerid()
						.hashCode());
		result = 37
				* result
				+ (getVchorderreferenceno() == null ? 0 : this
						.getVchorderreferenceno().hashCode());
		result = 37
				* result
				+ (getDtorderdate() == null ? 0 : this.getDtorderdate()
						.hashCode());
		result = 37 * result
				+ (getVchunit() == null ? 0 : this.getVchunit().hashCode());
		result = 37
				* result
				+ (getVchblacklistdesc() == null ? 0 : this
						.getVchblacklistdesc().hashCode());
		result = 37 * result
				+ (getVchnotes() == null ? 0 : this.getVchnotes().hashCode());
		result = 37
				* result
				+ (getVchblackliststatus() == null ? 0 : this
						.getVchblackliststatus().hashCode());
		result = 37
				* result
				+ (getVchcreateby() == null ? 0 : this.getVchcreateby()
						.hashCode());
		result = 37
				* result
				+ (getVchupdateby() == null ? 0 : this.getVchupdateby()
						.hashCode());
		result = 37
				* result
				+ (getDtcreatedate() == null ? 0 : this.getDtcreatedate()
						.hashCode());
		result = 37
				* result
				+ (getDtupdatedate() == null ? 0 : this.getDtupdatedate()
						.hashCode());
		result = 37
				* result
				+ (getVchbranchid() == null ? 0 : this.getVchbranchid()
						.hashCode());
		result = 37
				* result
				+ (getVchdesignation() == null ? 0 : this.getVchdesignation()
						.hashCode());
		return result;
	}

}
