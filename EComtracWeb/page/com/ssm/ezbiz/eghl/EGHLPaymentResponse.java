package com.ssm.ezbiz.eghl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.DecimalFormat;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class EGHLPaymentResponse implements Serializable{
	public String TransactionType; //A|7|Y|Follows requestFollows request2.
	public String PymtMethod; //A|3|Y|Payment MethodPayment MethodCC – Credit Card (Online 3D/Non3D)CC – Credit Card (Online 3D/Non3D)MO – Credit Card (MOTO – Mail Order Telephone Order)MO – Credit Card (MOTO – Mail Order Telephone Order)DD – Direct DebitDD – Direct DebitWA – e-WalletWA – e-WalletCC – Credit CardCC – Credit Card3.
	public String ServiceID; //AN|3|Y|Follows requestFollows request4.
	public String PaymentID; //AN|20|Y|Follows requestFollows request5.
	public String OrderNumber; //AN|20|Y|Follows requestFollows request6.
	public String Amount; //N|12(2)|Y|Follows request for transaction not entitled for promotionFollows request for transaction not entitled for promotionFor transaction entitled for promotion, Amount will not be the same as the Amount provided in Payment Request. Amount will be the actual payment amount which is the final amount after deducting promotion discount. The original Amount will be available in PromoOriAmtFor transaction entitled for promotion, Amount will not be the same as the Amount provided in Payment Request. Amount will be the actual payment amount which is the final amount after deducting promotion discount. The original Amount will be available in PromoOriAmt7.
	public String CurrencyCode; //N|3|Y|Follows requestFollows request8.
	public String HashValue; //AN|100|Y|Message digest value calculated by Payment Gateway in hexadecimal string using SHA256 hash algorithmMessage digest value calculated by Payment Gateway in hexadecimal string using SHA256 hash algorithmRe: Section 2.13.1.2Re: Section 2.13.1.29.
	public String HashValue2; //AN|100|Y|HIGHLY RECOMMENDED to verify this message digest value calculated by Payment Gateway in hexadecimal string using SHA256 hash algorithmHIGHLY RECOMMENDED to verify this message digest value calculated by Payment Gateway in hexadecimal string using SHA256 hash algorithmRe: Section 2.13.1.2Re: Section 2.13.1.2eGHL API Version 2.8geGHL API Version 2.8gMerchant Integration Guide GHL ePaymentsMerchant Integration Guide GHL ePaymentsWarning! This document is strictly private and confidential. This document is not intended for public circulation. DuplicationWarning! This document is strictly private and confidential. This document is not intended for public circulation. Duplicationof this document is prohibited and will be considered as a infringement of intellectual property laws.of this document is prohibited and will be considered as a infringement of intellectual property laws.Document Ref: eGHL_API_v2.8g.docDocument Ref: eGHL_API_v2.8g.docDocument Written by: GHL ePayments Page 16Document Written by: GHL ePayments Page 1610.
	public String TxnID; //AN|30|Y|Unique Transaction ID or Reference Code assigned by Payment Gateway for this transactionUnique Transaction ID or Reference Code assigned by Payment Gateway for this transaction11.
	public String IssuingBank; //AN|30|Y|Follows request if this field is provided in requestFollows request if this field is provided in requestIf not provided in request,If not provided in request,For PymtMethod “CC”, this field is the Bank Name keyed in by customer on eGHL Payment Info Collection PageFor PymtMethod “CC”, this field is the Bank Name keyed in by customer on eGHL Payment Info Collection PageFor PymtMethod “DD”, this field is the Bank Name chosen by customer to perform Direct Debit transactionFor PymtMethod “DD”, this field is the Bank Name chosen by customer to perform Direct Debit transaction12.
	public String TxnStatus; //N|4|Y|Re: Section 3.2Re: Section 3.213.
	
	public String AuthCode; //AN|12|N|Authorization Code returned by bankAuthorization Code returned by bank14.
	public String TxnMessage; //AN|255|N|Message that briefly explains the responseMessage that briefly explains the response15.
	public String TokenType; //A|3|N|Token TypeToken TypeIf merchant is subscribed to eGHL One-Click Payment feature, upon payment approved, TokenType will be “OCP”, together with token value in Token fieldIf merchant is subscribed to eGHL One-Click Payment feature, upon payment approved, TokenType will be “OCP”, together with token value in Token field16.
	public String Token; //ANS|50|C|Token ValueToken ValueIf Token Type is “OCP”, Token field will hold the Token Value for One-Click Payment purposesIf Token Type is “OCP”, Token field will hold the Token Value for One-Click Payment purposes17.
	public String Param6; //ANS|50|C|Follows requestFollows request18.
	public String Param7; //ANS|50|C|Follows requestFollows request19.
	public String CardHolder; //AN|30|C|Cardholder’s NameCardholder’s NameOnly available as per requestOnly available as per request20.
	public String CardNoMask; //AN|19|C|Credit Card Number used for payment authorization, first 6 and last 4 digits are in clear, the rests are masked with “X”.Credit Card Number used for payment authorization, first 6 and last 4 digits are in clear, the rests are masked with “X”.e.g. 444433XXXXXX1111e.g. 444433XXXXXX1111Only available as per requestOnly available as per requesteGHL API Version 2.8geGHL API Version 2.8gMerchant Integration Guide GHL ePaymentsMerchant Integration Guide GHL ePaymentsWarning! This document is strictly private and confidential. This document is not intended for public circulation. DuplicationWarning! This document is strictly private and confidential. This document is not intended for public circulation. Duplicationof this document is prohibited and will be considered as a infringement of intellectual property laws.of this document is prohibited and will be considered as a infringement of intellectual property laws.Document Ref: eGHL_API_v2.8g.docDocument Ref: eGHL_API_v2.8g.docDocument Written by: GHL ePayments Page 17Document Written by: GHL ePayments Page 1721.
	public String CardExp; //N|6|C|Expiry date of credit card. Date format is YYYYMM, e.g. 201312 for year 2013 DecemberExpiry date of credit card. Date format is YYYYMM, e.g. 201312 for year 2013 DecemberOnly available as per requestOnly available as per request22.
	public String CardType; //A|10|C|Credit Card TypeCredit Card Typee.g.e.g.VISA/MASTERCARD/AMEX/VISA/MASTERCARD/AMEX/JCB/DINERSJCB/DINERSOnly available as per requestOnly available as per request23.
	public String SettleTAID; //N|10|C|Terminal Account ID identifying the respective TID used to process the transaction and to be used for TxnType SETTLE.Terminal Account ID identifying the respective TID used to process the transaction and to be used for TxnType SETTLE.Only available for MOTO transaction (PymtMethod MO)Only available for MOTO transaction (PymtMethod MO)24.
	public String TID; //N|8|C|Actual terminal ID assigned by the bank to perform the transactionActual terminal ID assigned by the bank to perform the transactionOnly available for MOTO transaction (PymtMethod MO)Only available for MOTO transaction (PymtMethod MO)25.
	public String EPPMonth; //N|2|C|Follows requestFollows request26.
	public String EPP_YN; //N|1|C|Identifier for installment entitlementIdentifier for installment entitlement0 – Not entitled for installment0 – Not entitled for installment1 – Entitled for installment1 – Entitled for installment27.
	public String PromoCode; //AN|10|C|Promotion CodePromotion CodeFollows request if PromoCode is provided in Payment RequestFollows request if PromoCode is provided in Payment RequestIf PromoCode is not provided in Payment Request but the transaction is entitled for promotion, PromoCode will be available in Payment ResponseIf PromoCode is not provided in Payment Request but the transaction is entitled for promotion, PromoCode will be available in Payment Response28.
	public String PromoOriAmt; //N|12(2)|C|Follows request’s Amount for transaction entitled for promotionFollows request’s Amount for transaction entitled for promotionOriginal amount before deducting promotion discount. Only available for transaction entitled forOriginal amount before deducting promotion discount. Only available for transaction entitled for
	
	
	public EGHLPaymentResponse(IRequestParameters req) {
		Field fields[] = this.getClass().getFields();
		for (int i = 0; i < fields.length; i++) {
			try {
				String fieldName =fields[i].getName();
				if(req.getParameterValue(fieldName)!=null){
					String type = fields[i].getType().getName();
					fields[i].set(this, req.getParameterValue(fieldName).toString());
//					System.out.println(fieldName+":"+req.getParameterValue(fieldName).toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public String getTransactionType() {
		return TransactionType;
	}
	public void setTransactionType(String transactionType) {
		TransactionType = transactionType;
	}
	public String getPymtMethod() {
		return PymtMethod;
	}
	public void setPymtMethod(String pymtMethod) {
		PymtMethod = pymtMethod;
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
	public String getOrderNumber() {
		return OrderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		OrderNumber = orderNumber;
	}
	public String getAmount() {
		return Amount;
	}
	public void setAmount(String amount) {
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
	public String getHashValue2() {
		return HashValue2;
	}
	public void setHashValue2(String hashValue2) {
		HashValue2 = hashValue2;
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
	public String getTxnStatus() {
		return TxnStatus;
	}
	public void setTxnStatus(String txnStatus) {
		TxnStatus = txnStatus;
	}
	public String getAuthCode() {
		return AuthCode;
	}
	public void setAuthCode(String authCode) {
		AuthCode = authCode;
	}
	public String getTxnMessage() {
		return TxnMessage;
	}
	public void setTxnMessage(String txnMessage) {
		TxnMessage = txnMessage;
	}
	public String getTokenType() {
		return TokenType;
	}
	public void setTokenType(String tokenType) {
		TokenType = tokenType;
	}
	public String getToken() {
		return Token;
	}
	public void setToken(String token) {
		Token = token;
	}
	public String getParam6() {
		return Param6;
	}
	public void setParam6(String param6) {
		Param6 = param6;
	}
	public String getParam7() {
		return Param7;
	}
	public void setParam7(String param7) {
		Param7 = param7;
	}
	public String getCardHolder() {
		return CardHolder;
	}
	public void setCardHolder(String cardHolder) {
		CardHolder = cardHolder;
	}
	public String getCardNoMask() {
		return CardNoMask;
	}
	public void setCardNoMask(String cardNoMask) {
		CardNoMask = cardNoMask;
	}
	public String getCardExp() {
		return CardExp;
	}
	public void setCardExp(String cardExp) {
		CardExp = cardExp;
	}
	public String getCardType() {
		return CardType;
	}
	public void setCardType(String cardType) {
		CardType = cardType;
	}
	public String getSettleTAID() {
		return SettleTAID;
	}
	public void setSettleTAID(String settleTAID) {
		SettleTAID = settleTAID;
	}
	public String getTID() {
		return TID;
	}
	public void setTID(String tID) {
		TID = tID;
	}
	public String getEPPMonth() {
		return EPPMonth;
	}
	public void setEPPMonth(String ePPMonth) {
		EPPMonth = ePPMonth;
	}
	public String getEPP_YN() {
		return EPP_YN;
	}
	public void setEPP_YN(String ePP_YN) {
		EPP_YN = ePP_YN;
	}
	public String getPromoCode() {
		return PromoCode;
	}
	public void setPromoCode(String promoCode) {
		PromoCode = promoCode;
	}
	public String getPromoOriAmt() {
		return PromoOriAmt;
	}
	public void setPromoOriAmt(String promoOriAmt) {
		PromoOriAmt = promoOriAmt;
	}
	
	public boolean isHashingValid(String password){
		//Hash Key = Password + TxnID + ServiceID + PaymentID + TxnStatus + Amount + CurrencyCode + AuthCode
		
		String hashB4Gen = password;
		DecimalFormat df = new DecimalFormat("#.00");
		hashB4Gen += ((StringUtils.isBlank(TxnID)) ? "" : TxnID) ;
		hashB4Gen += ((StringUtils.isBlank(ServiceID)) ? "" : ServiceID) ;
		hashB4Gen += ((StringUtils.isBlank(PaymentID)) ? "" : PaymentID) ;
		hashB4Gen += ((StringUtils.isBlank(TxnStatus)) ? "" : TxnStatus) ;
		hashB4Gen += ((StringUtils.isBlank(Amount)) ? "" : Amount ) ;
		hashB4Gen += ((StringUtils.isBlank(CurrencyCode)) ? "" : CurrencyCode) ;
		
		String hashGenerate = DigestUtils.sha256Hex(hashB4Gen);
		
		return HashValue.equals(hashGenerate);
	}
	
}
