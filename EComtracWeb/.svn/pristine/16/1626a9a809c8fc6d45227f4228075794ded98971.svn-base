package com.ssm.llp.mod1.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.envers.Audited;

/**
 * Represents contacts within the example application.
 *
 * <p>User: Nick Heudecker</p>
 */
@Entity
@Audited
@Table(name = "contacts")
public class Contact implements java.io.Serializable {

    private long id;
    private String refNo;
    private String firstName;
    private String lastName;
    private String email;
    private String notes;
    private String group;
	private Date createDt;
	private String createBy;
	private Date updateDt;
	private String updateBy;

    public Contact() {
	}
	
    public Contact(long id, String firstname, String lastname,
			String email, String notes, String contactgroup) {
		this.id = id;
		this.firstName = firstname;
		this.lastName = lastname;
		this.email = email;
		this.notes = notes;
		this.group = contactgroup;
	}
	
    public Contact(long contactId) {
		this.id = contactId;
	}
    
    @Id
    @GenericGenerator(name="ref_no", strategy="com.ssm.llp.base.hibernate.LlpIdGenerator", 
    parameters={@Parameter(name = "genCode", value = "CONTACT_RUNNING_NO"),
    			@Parameter(name = "fieldCode", value = "LLP"),
    			@Parameter(name = "moduleCode", value = "CON"),
    			@Parameter(name = "yearMonthDay", value = "yyyyMMdd"),
    			@Parameter(name = "lastNoPattern", value = "#")})
    @GeneratedValue(generator="ref_no")
    @Column(name = "ref_no", length = 50)
    public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
	
    
	@Column(name = "contact_id", unique = true, nullable = false, updatable=false)
	public long getId() {
		return this.id;
	}

	public void setId(long contactId) {
		this.id = contactId;
	}
    
    @Column(name = "firstName", length = 15)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    @Column(name = "lastName", length = 20)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "email", length = 150)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    
    @Column(name = "notes", length = 500)
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Column(name = "contactgroup", length = 15)
    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
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
}
