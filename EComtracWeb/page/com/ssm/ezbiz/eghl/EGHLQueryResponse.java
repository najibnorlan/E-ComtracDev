package com.ssm.ezbiz.eghl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

public class EGHLQueryResponse implements Serializable{

	public String TransactionType ;//A	7	Y	SALE – Direct captured for credit card payment; Payment request for other payment methodsSALE – Direct captured for credit card payment; Payment request for other payment methodsAUTH – For credit card payment, authorize the availability of funds for a transaction but delay the capture of funds until a later time. This is often useful for merchants who have a delayed order fulfillment process. Authorize & Capture also enables merchants to modify the original authorization amount due to order changes occurring after the initial order is placed, such as taxes, shipping or item availabilityAUTH – For credit card payment, authorize the availability of funds for a transaction but delay the capture of funds until a later time. This is often useful for merchants who have a delayed order fulfillment process. Authorize & Capture also enables merchants to modify the original authorization amount due to order changes occurring after the initial order is placed, such as taxes, shipping or item availabilityRe: Section 2.5 for Transaction Type CAPTURE message formatRe: Section 2.5 for Transaction Type CAPTURE message format2.
	public String PymtMethod;
	public String ServiceID;//	AN	3	Y	Merchant Service ID given by eGHLMerchant Service ID given by eGHL4.
	public String PaymentID;//	AN	20	Y	Unique transaction ID/reference code assigned by merchant for this payment transactionUnique transaction ID/reference code assigned by merchant for this payment transaction(No duplicate PaymentID is allowed)(No duplicate PaymentID is allowed)5.
	public double Amount;//	N	12(2)	Y	Payment amount in 2 decimal places regardless whether the currency has decimal places or not.Payment amount in 2 decimal places regardless whether the currency has decimal places or not.Please exclude “,” sign.Please exclude “,” sign.e.g. 1000.00 for IDRe.g. 1000.00 for IDRInvalid format: 1,000.00 or 1000Invalid format: 1,000.00 or 10009.
	public String CurrencyCode;//	A	3	Y	3-letter ISO4217 of Payment Currency Code3-letter ISO4217 of Payment Currency CodeRe: Section 3.6Re: Section 3.610.
	public String HashValue;//	AN	100	Y	Message digest value calculated by Merchant System in hexadecimal public String using SHA256 hash algorithmMessage digest value calculated by Merchant System in hexadecimal public String using SHA256 hash algorithmRe: Section 2.7.1.1Re: Section 2.7.1.111.
	public String TxnStatus;
	public String TxnExists;
	public String TxnMessage;
	public String QueryDesc;
	public String TotalRefundAmount;
	public String TxnID;
	public String IssuingBank;
	public String AuthCode;
	public String BankRefNo;
	public String SessionID;
	public String HashValue2;
	
	public static void main(String[] args) {
		Map<String, String> mapParamData = new HashMap<String, String>();
		mapParamData.put("TransactionType", "SALE");
		mapParamData.put("Amount", "10.2");
		EGHLQueryResponse r = new EGHLQueryResponse(mapParamData);
		System.out.println(r.getAmount()+":"+r.getTransactionType());
	}
	public EGHLQueryResponse(Map<String, String> mapParamData) {
    	Field fields[] = this.getClass().getFields();

    	for (int i = 0; i < fields.length; i++) {
			try {
				String fieldName =fields[i].getName();
//				System.out.print(fieldName+":");
				if(mapParamData.get(fieldName)!=null){
					String type = fields[i].getType().getName();
					if( "double".equals(type)){
						fields[i].set(this, Double.valueOf(mapParamData.get(fieldName)));
					}else{
						fields[i].set(this,mapParamData.get(fieldName));
					}
//					System.out.println(mapParamData.get(fieldName));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public EGHLQueryResponse(EGHLPaymentResponse eghlPaymentResponse) {
		DecimalFormat df = new DecimalFormat("#.00");
		TransactionType = eghlPaymentResponse.getTransactionType();
		PymtMethod = eghlPaymentResponse.getPymtMethod();
		ServiceID = eghlPaymentResponse.getServiceID();
		PaymentID= eghlPaymentResponse.getPaymentID();
		try {
			Amount= df.parse(eghlPaymentResponse.getAmount()).doubleValue();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		CurrencyCode= eghlPaymentResponse.getCurrencyCode();
		HashValue= eghlPaymentResponse.getHashValue();
		TxnStatus= eghlPaymentResponse.getTxnStatus();
		TxnExists= "0";//becoz eghlPaymentResponse is not null
		TxnMessage= eghlPaymentResponse.getTxnMessage();
//		QueryDesc= ::;
		TxnID= eghlPaymentResponse.getTxnID();
		IssuingBank= eghlPaymentResponse.getIssuingBank();
		AuthCode= eghlPaymentResponse.getAuthCode();
//		BankRefNo= eghlPaymentResponse.get;
//		SessionID= eghlPaymentResponse.gets;
		HashValue2= eghlPaymentResponse.getHashValue2();
	}
	public String getTransactionType() {
		return TransactionType;
	}
	public void setTransactionType(String transactionType) {
		TransactionType = transactionType;
	}
	public String getServiceID() {
		return ServiceID;
	}
	public void setServiceID(String serviceID) {
		ServiceID = serviceID;
	}
	public String getPaymentID() {
		return PaymentID;
	}
	public void setPaymentID(String paymentID) {
		PaymentID = paymentID;
	}
	public double getAmount() {
		return Amount;
	}
	public void setAmount(double amount) {
		Amount = amount;
	}
	public String getCurrencyCode() {
		return CurrencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		CurrencyCode = currencyCode;
	}
	public String getHashValue() {
		return HashValue;
	}
	public void setHashValue(String hashValue) {
		HashValue = hashValue;
	}
	public String getPymtMethod() {
		return PymtMethod;
	}
	public void setPymtMethod(String pymtMethod) {
		PymtMethod = pymtMethod;
	}
	public String getTxnStatus() {
		return TxnStatus;
	}
	public void setTxnStatus(String txnStatus) {
		TxnStatus = txnStatus;
	}
	public String getTxnExists() {
		return TxnExists;
	}
	public void setTxnExists(String txnExists) {
		TxnExists = txnExists;
	}
	public String getTxnMessage() {
		return TxnMessage;
	}
	public void setTxnMessage(String txnMessage) {
		TxnMessage = txnMessage;
	}
	public String getQueryDesc() {
		return QueryDesc;
	}
	public void setQueryDesc(String queryDesc) {
		QueryDesc = queryDesc;
	}
	public String getTotalRefundAmount() {
		return TotalRefundAmount;
	}
	public void setTotalRefundAmount(String totalRefundAmount) {
		TotalRefundAmount = totalRefundAmount;
	}
	public String getTxnID() {
		return TxnID;
	}
	public void setTxnID(String txnID) {
		TxnID = txnID;
	}
	public String getIssuingBank() {
		return IssuingBank;
	}
	public void setIssuingBank(String issuingBank) {
		IssuingBank = issuingBank;
	}
	public String getAuthCode() {
		return AuthCode;
	}
	public void setAuthCode(String authCode) {
		AuthCode = authCode;
	}
	public String getBankRefNo() {
		return BankRefNo;
	}
	public void setBankRefNo(String bankRefNo) {
		BankRefNo = bankRefNo;
	}
	public String getSessionID() {
		return SessionID;
	}
	public void setSessionID(String sessionID) {
		SessionID = sessionID;
	}
	public String getHashValue2() {
		return HashValue2;
	}
	public void setHashValue2(String hashValue2) {
		HashValue2 = hashValue2;
	}
	
	public String generateHashPaymentResponse(String password){
		String hash = password;
//		 Password + ServiceID + PaymentID + Amount + CurrencyCode
		DecimalFormat df = new DecimalFormat("#.00");
		hash += ((StringUtils.isBlank(ServiceID)) ? "" : ServiceID) ;
		hash += ((StringUtils.isBlank(PaymentID)) ? "" : PaymentID) ;
		hash += ((Amount==0) ? "" : df.format(Amount)) ;
		hash += ((StringUtils.isBlank(CurrencyCode)) ? "" : CurrencyCode) ;
		
		System.out.println("BeforeHash:"+hash);
		return DigestUtils.sha256Hex(hash);
	}
	
	boolean isValidHasFromServer(String password){
		String clientHash = generateHashPaymentResponse(password);
		return (getHashValue().equals(clientHash));
	}
	
	
}
