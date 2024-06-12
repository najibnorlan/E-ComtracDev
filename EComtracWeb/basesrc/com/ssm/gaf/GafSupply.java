package com.ssm.gaf;

public class GafSupply {
	String recordIdentifier = "S";
	String customerName = "";
	String customerBRN = "";
	String invoiceDate = "";
	String invoiceNo = "";
	String lineNo = "1";
	String productDesc = "";
	String salesValue = "0";
	String salesGSTValue = "0";
	String taxCode = "OS";
	String country = "MALAYSIA";
	String foreignCurrencyCode = "MYR";
	String salesForeignCurrencyAmount = "0";
	String salesForeignCurrencyGSTAmount = "0";
	String paymentMode;

	public String getRecordIdentifier() {
		return recordIdentifier;
	}

	public void setRecordIdentifier(String recordIdentifier) {
		this.recordIdentifier = recordIdentifier;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerBRN() {
		return customerBRN;
	}

	public void setCustomerBRN(String customerBRN) {
		this.customerBRN = customerBRN;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public String getSalesValue() {
		return salesValue;
	}

	public void setSalesValue(String salesValue) {
		this.salesValue = salesValue;
	}

	public String getSalesGSTValue() {
		return salesGSTValue;
	}

	public void setSalesGSTValue(String salesGSTValue) {
		this.salesGSTValue = salesGSTValue;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getForeignCurrencyCode() {
		return foreignCurrencyCode;
	}

	public void setForeignCurrencyCode(String foreignCurrencyCode) {
		this.foreignCurrencyCode = foreignCurrencyCode;
	}

	public String getSalesForeignCurrencyAmount() {
		return salesForeignCurrencyAmount;
	}

	public void setSalesForeignCurrencyAmount(
			String salesForeignCurrencyAmount) {
		this.salesForeignCurrencyAmount = salesForeignCurrencyAmount;
	}

	public String getSalesForeignCurrencyGSTAmount() {
		return salesForeignCurrencyGSTAmount;
	}

	public void setSalesForeignCurrencyGSTAmount(
			String salesForeignCurrencyGSTAmount) {
		this.salesForeignCurrencyGSTAmount = salesForeignCurrencyGSTAmount;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
}
