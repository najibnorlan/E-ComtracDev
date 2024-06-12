package com.ssm.llp.base.common.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import com.ssm.llp.base.envers.RevListener;

@Entity
@RevisionEntity(RevListener.class)
@Table(name = "revinfo")
public class RevEntity implements Serializable {
	
//	private static final long serialVersionUID = 7036526140804172112L;
	private int rev;
	private Long revtstmp;
	private String userName;
	
	public RevEntity() {
	}

	public RevEntity(int rev) {
		this.rev = rev;
	}

	public RevEntity(int rev, Long revtstmp, String userName) {
		this.rev = rev;
		this.revtstmp = revtstmp;
		this.userName = userName;
	}

	
	@Id
	@RevisionNumber
	@Column(name = "rev", unique = true, nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
	public int getRev() {
		return this.rev;
	}

	public void setRev(int rev) {
		this.rev = rev;
	}

	@RevisionTimestamp
	@Column(name = "revtstmp")
	public Long getRevtstmp() {
		return this.revtstmp;
	}

	public void setRevtstmp(Long revtstmp) {
		this.revtstmp = revtstmp;
	}

	@Column(name = "user_name", length = 50)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}