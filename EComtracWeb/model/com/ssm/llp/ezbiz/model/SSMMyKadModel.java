package com.ssm.llp.ezbiz.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.ssm.llp.base.common.Parameter;
import com.ssm.mykad.ws.MyKadProfileParam;

@Entity
@Table(name = "ssm_my_kad")
public class SSMMyKadModel implements Serializable{
    
	private long ssmMykadId;
    private String mykadNo;
    private String gender;
    private String name;
    private String originalName;
    private String gmpcName;
    private String oldIc;
    private Date dob;
    private String birthPlace;
    private String citizenship;
    private String race;
    private String religion;
    private String address1;
    private String address2;
    private String address3;
    private String postcode;
    private String city;
    private String state;
    private byte[] mykadPicture;
    private String adUserRequester;
    private String thumbPrintSuccess = Parameter.YES_NO_no;
    private Date createDt;
	private String createBy;
	private String ipAddress;
    
	private String ezBizLoginId;
    private String ezBizAddress1;
    private String ezBizAddress2;
    private String ezBizAddress3;
    private String ezBizPostcode;
    private String ezBizCity;
    private String ezBizState;
    private String ezBizEmail;
    private String ezBizPhoneNo;
    private String ezBizPassword;
    
    private String mykadRefNo;
    
    @Id
	@Column(name = "ssm_mykad_id", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getSsmMykadId() {
		return ssmMykadId;
	}
	public void setSsmMykadId(long ssmMykadId) {
		this.ssmMykadId = ssmMykadId;
	}
	
	@Column(name = "thumb_Print_Success")
    public String getThumbPrintSuccess() {
        return thumbPrintSuccess;
    }
    public void setThumbPrintSuccess(String thumbPrintSuccess) {
        this.thumbPrintSuccess = thumbPrintSuccess;
    }
    @Column(name = "gender")
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    @Column(name = "name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name = "old_ic")
    public String getOldIc() {
        return oldIc;
    }
    public void setOldIc(String oldIc) {
        this.oldIc = oldIc;
    }
    
    @Column(name = "birth_place")
    public String getBirthPlace() {
        return birthPlace;
    }
    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }
    
    @Column(name = "citizenship")
    public String getCitizenship() {
        return citizenship;
    }
    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }
    
    @Column(name = "race")
    public String getRace() {
        return race;
    }
    public void setRace(String race) {
        this.race = race;
    }
    
    @Column(name = "religion")
    public String getReligion() {
        return religion;
    }
    public void setReligion(String religion) {
        this.religion = religion;
    }
    
    @Column(name = "address1")
    public String getAddress1() {
        return address1;
    }
    public void setAddress1(String address1) {
        this.address1 = address1;
    }
    
    @Column(name = "address2")
    public String getAddress2() {
        return address2;
    }
    public void setAddress2(String address2) {
        this.address2 = address2;
    }
    
    @Column(name = "address3")
    public String getAddress3() {
        return address3;
    }
    public void setAddress3(String address3) {
        this.address3 = address3;
    }
    
    @Column(name = "city")
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    
    @Column(name = "postcode")
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
    
    @Column(name = "state")
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    	
	@Column(name = "mykad_no")
	public String getMykadNo() {
		return mykadNo;
	}
	public void setMykadNo(String mykadNo) {
		this.mykadNo = mykadNo;
	}
	
	@Column(name = "my_kad_picture")
	public byte[] getMykadPicture() {
		return mykadPicture;
	}
	public void setMykadPicture(byte[] mykadPicture) {
		this.mykadPicture = mykadPicture;
	}
	
	@Column(name = "ad_user_requester")
	public String getAdUserRequester() {
		return adUserRequester;
	}
	public void setAdUserRequester(String adUserRequester) {
		this.adUserRequester = adUserRequester;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "dob")
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public Date getDob() {
		return dob;
	}
	
	@Column(name = "original_name")
	public String getOriginalName() {
		return originalName;
	}
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}
	
	@Column(name = "gmpc_name")
	public String getGmpcName() {
		return gmpcName;
	}
	public void setGmpcName(String gmpcName) {
		this.gmpcName = gmpcName;
	}

	
	public void copyFromMyKadWSParameter(MyKadProfileParam param){
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		setName(param.getName());
		setOriginalName(param.getOriginalName());
		setGmpcName(param.getGmpcName());
		if(param.isThumbPrintSuccess()){
			setThumbPrintSuccess(Parameter.YES_NO_yes);
		}else{
			setThumbPrintSuccess(Parameter.YES_NO_no);
		}
		
	    setMykadNo(param.getIc());
	    setGender(param.getGender());
	    setName(param.getName());
	    setOldIc(param.getOldIc());
	    try {
			setDob(sdf.parse(param.getDob()));
		} catch (ParseException e) {
			try {
				SimpleDateFormat sdfMalay = new SimpleDateFormat("dd-MMM-yyyy", new Locale("ms","MY"));
				setDob(sdfMalay.parse(param.getDob()));
			} catch (Exception e2) {
				e.printStackTrace();
			}
		}
	    setBirthPlace(param.getBirthPlace());
	    setCitizenship(param.getCitizenship());
	    setRace(param.getRace());
	    setReligion(param.getReligion());
	    setAddress1(param.getAddress1());
	    setAddress2(param.getAddress2());
	    setAddress3(param.getAddress3());
	    setPostcode(param.getPoscode());
	    setCity(param.getCity());
	    setState(param.getState());
	    setMykadPicture(param.getImage());
	    setAdUserRequester(param.getAdUser());

	    setEzBizLoginId(param.getEzBizLoginId());
	    setEzBizAddress1(param.getEzBizAddress1());
	    setEzBizAddress2(param.getEzBizAddress2());
	    setEzBizAddress3(param.getEzBizAddress3());
	    setEzBizPostcode(param.getEzBizPostcode());
	    setEzBizCity(param.getEzBizCity());
	    setEzBizState(param.getEzBizState());
	    setEzBizEmail(param.getEzBizEmail());
	    setEzBizPhoneNo(param.getEzBizPhoneNo());
	    setEzBizPassword(param.getEzBizPassword());
	    
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_dt", nullable = false, length = 3594)
	public Date getCreateDt() {
		return createDt;
	}
	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}
	
	@Column(name = "create_by", nullable = false)
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	
	@Column(name = "ezbiz_address1")
	public String getEzBizAddress1() {
		return ezBizAddress1;
	}
	public void setEzBizAddress1(String ezBizAddress1) {
		this.ezBizAddress1 = ezBizAddress1;
	}
	
	@Column(name = "ezbiz_address2")
	public String getEzBizAddress2() {
		return ezBizAddress2;
	}
	public void setEzBizAddress2(String ezBizAddress2) {
		this.ezBizAddress2 = ezBizAddress2;
	}
	
	@Column(name = "ezbiz_address3")
	public String getEzBizAddress3() {
		return ezBizAddress3;
	}
	public void setEzBizAddress3(String ezBizAddress3) {
		this.ezBizAddress3 = ezBizAddress3;
	}
	
	@Column(name = "ezbiz_city")
	public String getEzBizCity() {
		return ezBizCity;
	}
	public void setEzBizCity(String ezBizCity) {
		this.ezBizCity = ezBizCity;
	}
	
	@Column(name = "ezbiz_state")
	public String getEzBizState() {
		return ezBizState;
	}
	public void setEzBizState(String ezBizState) {
		this.ezBizState = ezBizState;
	}
	
	@Column(name = "ezbiz_email")
	public String getEzBizEmail() {
		return ezBizEmail;
	}
	public void setEzBizEmail(String ezBizEmail) {
		this.ezBizEmail = ezBizEmail;
	}
	
	@Column(name = "ezbiz_phone_no")
	public String getEzBizPhoneNo() {
		return ezBizPhoneNo;
	}
	public void setEzBizPhoneNo(String ezBizPhoneNo) {
		this.ezBizPhoneNo = ezBizPhoneNo;
	}
	
	@Transient
	public String getEzBizPassword() {
		return ezBizPassword;
	}
	public void setEzBizPassword(String ezBizPassword) {
		this.ezBizPassword = ezBizPassword;
	}
	
	@Column(name = "ezbiz_login_id")
	public String getEzBizLoginId() {
		return ezBizLoginId;
	}
	public void setEzBizLoginId(String ezBizLoginId) {
		this.ezBizLoginId = ezBizLoginId;
	}
	
	@Column(name = "ezbiz_postcode")
	public String getEzBizPostcode() {
		return ezBizPostcode;
	}
	public void setEzBizPostcode(String ezBizPostcode) {
		this.ezBizPostcode = ezBizPostcode;
	}
	
	@Column(name = "ip_address")
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	@Transient
	public String getMykadRefNo() {
		return mykadRefNo;
	}
	public void setMykadRefNo(String mykadRefNo) {
		this.mykadRefNo = mykadRefNo;
	}
	
	@Transient
	public String getAvailableName(){
		String availableName = getGmpcName();
		if (availableName==null || availableName.trim().length()==0) {
			// If mykad name empty use gmpc gov multipurpose card
			availableName= getName();
		}
		if (availableName==null || availableName.trim().length()==0) {
			// If name still empty use ori name
			availableName = getOriginalName();
		}
		return availableName;
	}
	
}
