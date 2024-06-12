package com.ssm.gaf;

public class GafCompany {
	String recordIdentifier = "C";
	String companyName = "SURUHANJAYA SYARIKAT MALAYSIA";
	String companyBRN = "";
	String companyGSTNo = "000640557056";
	String periodStart = "";
	String periodEnd = "";
	String fileCreationDt = "";
	String productVersion = "EZBIZ";
	String gafVersion = "EZBIZGAFv1.0.0";

	public String getRecordIdentifier() {
		return recordIdentifier;
	}

	public void setRecordIdentifier(String recordIdentifier) {
		this.recordIdentifier = recordIdentifier;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyBRN() {
		return companyBRN;
	}

	public void setCompanyBRN(String companyBRN) {
		this.companyBRN = companyBRN;
	}

	public String getCompanyGSTNo() {
		return companyGSTNo;
	}

	public void setCompanyGSTNo(String companyGSTNo) {
		this.companyGSTNo = companyGSTNo;
	}

	public String getPeriodStart() {
		return periodStart;
	}

	public void setPeriodStart(String periodStart) {
		this.periodStart = periodStart;
	}

	public String getPeriodEnd() {
		return periodEnd;
	}

	public void setPeriodEnd(String periodEnd) {
		this.periodEnd = periodEnd;
	}

	public String getFileCreationDt() {
		return fileCreationDt;
	}

	public void setFileCreationDt(String fileCreationDt) {
		this.fileCreationDt = fileCreationDt;
	}

	public String getProductVersion() {
		return productVersion;
	}

	public void setProductVersion(String productVersion) {
		this.productVersion = productVersion;
	}

	public String getGafVersion() {
		return gafVersion;
	}

	public void setGafVersion(String gafVersion) {
		this.gafVersion = gafVersion;
	}
}