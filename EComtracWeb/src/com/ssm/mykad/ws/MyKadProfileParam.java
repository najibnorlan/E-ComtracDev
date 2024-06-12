package com.ssm.mykad.ws;

import java.io.Serializable;

public class MyKadProfileParam implements Serializable{
    private String adUser;
    boolean thumbPrintSuccess =false;
    public String ic;
    private String gender;
    private String name;
    public String originalName;
    public String gmpcName;
    private String oldIc;
    private String dob;
    private String birthPlace;
    private String citizenship;
    private String race;
    private String religion;
    private String address1;
    private String address2;
    private String address3;
    private String poscode;
    private String city;
    private String state;
    private byte[] image;

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
    
    
    public String getAdUser() {
        return adUser;
    }
    public void setAdUser(String adUser) {
        this.adUser = adUser;
    }
    public boolean isThumbPrintSuccess() {
        return thumbPrintSuccess;
    }
    public void setThumbPrintSuccess(boolean thumbPrintSuccess) {
        this.thumbPrintSuccess = thumbPrintSuccess;
    }
    public String getIc() {
        return ic;
    }
    public void setIc(String ic) {
        this.ic = ic;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getOldIc() {
        return oldIc;
    }
    public void setOldIc(String oldIc) {
        this.oldIc = oldIc;
    }
    public String getDob() {
        return dob;
    }
    public void setDob(String dob) {
        this.dob = dob;
    }
    public String getBirthPlace() {
        return birthPlace;
    }
    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }
    public String getCitizenship() {
        return citizenship;
    }
    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }
    public String getRace() {
        return race;
    }
    public void setRace(String race) {
        this.race = race;
    }
    public String getReligion() {
        return religion;
    }
    public void setReligion(String religion) {
        this.religion = religion;
    }
    public String getAddress1() {
        return address1;
    }
    public void setAddress1(String address1) {
        this.address1 = address1;
    }
    public String getAddress2() {
        return address2;
    }
    public void setAddress2(String address2) {
        this.address2 = address2;
    }
    public String getAddress3() {
        return address3;
    }
    public void setAddress3(String address3) {
        this.address3 = address3;
    }
    public String getPoscode() {
        return poscode;
    }
    public void setPoscode(String poscode) {
        this.poscode = poscode;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public byte[] getImage() {
        return image;
    }
    public void setImage(byte[] image) {
        this.image = image;
    }
	public String getOriginalName() {
		return originalName;
	}
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}
	public String getGmpcName() {
		return gmpcName;
	}
	public void setGmpcName(String gmpcName) {
		this.gmpcName = gmpcName;
	}
	public String getEzBizAddress1() {
		return ezBizAddress1;
	}
	public void setEzBizAddress1(String ezBizAddress1) {
		this.ezBizAddress1 = ezBizAddress1;
	}
	public String getEzBizAddress2() {
		return ezBizAddress2;
	}
	public void setEzBizAddress2(String ezBizAddress2) {
		this.ezBizAddress2 = ezBizAddress2;
	}
	public String getEzBizAddress3() {
		return ezBizAddress3;
	}
	public void setEzBizAddress3(String ezBizAddress3) {
		this.ezBizAddress3 = ezBizAddress3;
	}
	public String getEzBizCity() {
		return ezBizCity;
	}
	public void setEzBizCity(String ezBizCity) {
		this.ezBizCity = ezBizCity;
	}
	public String getEzBizState() {
		return ezBizState;
	}
	public void setEzBizState(String ezBizState) {
		this.ezBizState = ezBizState;
	}
	public String getEzBizEmail() {
		return ezBizEmail;
	}
	public void setEzBizEmail(String ezBizEmail) {
		this.ezBizEmail = ezBizEmail;
	}
	public String getEzBizPhoneNo() {
		return ezBizPhoneNo;
	}
	public void setEzBizPhoneNo(String ezBizPhoneNo) {
		this.ezBizPhoneNo = ezBizPhoneNo;
	}
	public String getEzBizPassword() {
		return ezBizPassword;
	}
	public void setEzBizPassword(String ezBizPassword) {
		this.ezBizPassword = ezBizPassword;
	}
	public String getEzBizPostcode() {
		return ezBizPostcode;
	}
	public void setEzBizPostcode(String ezBizPostcode) {
		this.ezBizPostcode = ezBizPostcode;
	}
	public String getEzBizLoginId() {
		return ezBizLoginId;
	}
	public void setEzBizLoginId(String ezBizLoginId) {
		this.ezBizLoginId = ezBizLoginId;
	}
	
	
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
