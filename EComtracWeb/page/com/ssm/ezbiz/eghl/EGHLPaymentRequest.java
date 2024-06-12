package com.ssm.ezbiz.eghl;

import java.io.Serializable;
import java.text.DecimalFormat;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import com.informix.lang.Decimal;

public class EGHLPaymentRequest implements Serializable{
	public String TransactionType ;//A	7	Y	SALE – Direct captured for credit card payment; Payment request for other payment methodsSALE – Direct captured for credit card payment; Payment request for other payment methodsAUTH – For credit card payment, authorize the availability of funds for a transaction but delay the capture of funds until a later time. This is often useful for merchants who have a delayed order fulfillment process. Authorize & Capture also enables merchants to modify the original authorization amount due to order changes occurring after the initial order is placed, such as taxes, shipping or item availabilityAUTH – For credit card payment, authorize the availability of funds for a transaction but delay the capture of funds until a later time. This is often useful for merchants who have a delayed order fulfillment process. Authorize & Capture also enables merchants to modify the original authorization amount due to order changes occurring after the initial order is placed, such as taxes, shipping or item availabilityRe: Section 2.5 for Transaction Type CAPTURE message formatRe: Section 2.5 for Transaction Type CAPTURE message format2.
	public String PymtMethod;//	A	3	Y	Payment MethodPayment MethodCC – Credit Card (Online 3D/Non3D)CC – Credit Card (Online 3D/Non3D)MO – Credit Card (MOTO – Mail Order Telephone Order)MO – Credit Card (MOTO – Mail Order Telephone Order)DD – Direct Debit (not applicable to TransactionType AUTH)DD – Direct Debit (not applicable to TransactionType AUTH)WA – e-Wallet (not applicable to Transaction Type AUTH)WA – e-Wallet (not applicable to Transaction Type AUTH)ANY – All payment method(s) registered with eGHLANY – All payment method(s) registered with eGHL3.
	public String ServiceID;//	AN	3	Y	Merchant Service ID given by eGHLMerchant Service ID given by eGHL4.
	public String PaymentID;//	AN	20	Y	Unique transaction ID/reference code assigned by merchant for this payment transactionUnique transaction ID/reference code assigned by merchant for this payment transaction(No duplicate PaymentID is allowed)(No duplicate PaymentID is allowed)5.
	public String OrderNumber;//	AN	20	Y	Reference number / Invoice number for this orderReference number / Invoice number for this orderPaymentID must be unique but OrderNumber can be the same under different PaymentID, indicating multiplePaymentID must be unique but OrderNumber can be the same under different PaymentID, indicating multipleeGHL API Version 2.8geGHL API Version 2.8gMerchant Integration Guide GHL ePaymentsMerchant Integration Guide GHL ePaymentsWarning! This document is strictly private and confidential. This document is not intended for public circulation. DuplicationWarning! This document is strictly private and confidential. This document is not intended for public circulation. Duplicationof this document is prohibited and will be considered as a infringement of intellectual property laws.of this document is prohibited and will be considered as a infringement of intellectual property laws.Document Ref: eGHL_API_v2.8g.docDocument Ref: eGHL_API_v2.8g.docDocument Written by: GHL ePayments Page 8Document Written by: GHL ePayments Page 8payment attempts are made on a particular orderpayment attempts are made on a particular orderIf Order Number is not applicable, please provide the same value as PaymentIDIf Order Number is not applicable, please provide the same value as PaymentID6.
	public String PaymentDesc;//	AN	100	Y	Order’s descriptionsOrder’s descriptions7.
	public String MerchantReturnURL;//	AN	255	Y	Merchant system’s browser redirect URL which receives payment response from eGHL when transaction is completed (approved/declined/system error/Merchant system’s browser redirect URL which receives payment response from eGHL when transaction is completed (approved/declined/system error/cancelled by buyer on eGHL Payment Page)cancelled by buyer on eGHL Payment Page)If MerchantApprovalURL is provided, when payment is approved, MerchantApprovalURL will be used instead of MerchantReturnURLIf MerchantApprovalURL is provided, when payment is approved, MerchantApprovalURL will be used instead of MerchantReturnURLIf MerchantUnApprovalURL is provided, when payment is declined, MerchantUnApprovalURL will be used instead of MerchantReturnURLIf MerchantUnApprovalURL is provided, when payment is declined, MerchantUnApprovalURL will be used instead of MerchantReturnURL8.
	public double Amount;//	N	12(2)	Y	Payment amount in 2 decimal places regardless whether the currency has decimal places or not.Payment amount in 2 decimal places regardless whether the currency has decimal places or not.Please exclude “,” sign.Please exclude “,” sign.e.g. 1000.00 for IDRe.g. 1000.00 for IDRInvalid format: 1,000.00 or 1000Invalid format: 1,000.00 or 10009.
	public String CurrencyCode;//	A	3	Y	3-letter ISO4217 of Payment Currency Code3-letter ISO4217 of Payment Currency CodeRe: Section 3.6Re: Section 3.610.
	public String HashValue;//	AN	100	Y	Message digest value calculated by Merchant System in hexadecimal public String using SHA256 hash algorithmMessage digest value calculated by Merchant System in hexadecimal public String using SHA256 hash algorithmRe: Section 2.7.1.1Re: Section 2.7.1.111.
	public String CustIP;//	AN	20	Y	Customer’s IP address captured by merchant systemCustomer’s IP address captured by merchant system12.
	public String CustName;//	AN	50	Y	Customer NameCustomer NameeGHL API Version 2.8geGHL API Version 2.8gMerchant Integration Guide GHL ePaymentsMerchant Integration Guide GHL ePaymentsWarning! This document is strictly private and confidential. This document is not intended for public circulation. DuplicationWarning! This document is strictly private and confidential. This document is not intended for public circulation. Duplicationof this document is prohibited and will be considered as a infringement of intellectual property laws.of this document is prohibited and will be considered as a infringement of intellectual property laws.Document Ref: eGHL_API_v2.8g.docDocument Ref: eGHL_API_v2.8g.docDocument Written by: GHL ePayments Page 9Document Written by: GHL ePayments Page 913.
	public String CustEmail;//	AN	60	Y	Customer’s Email AddressCustomer’s Email Address14.
	public String CustPhone;//	AN	25	Y	Customer’s Contact NumberCustomer’s Contact Number15.
	public double B4TaxAmt;//	N	12(2)	N	Original amount before tax is incurred, in 2 decimal places regardless whether the currency has decimal places or not.Original amount before tax is incurred, in 2 decimal places regardless whether the currency has decimal places or not.As for final payment amount after tax is incurred, is to be specified in Amount fieldAs for final payment amount after tax is incurred, is to be specified in Amount fieldPlease exclude “,” sign.Please exclude “,” sign.e.g. 1000.00 for IDRe.g. 1000.00 for IDRInvalid format: 1,000.00 or 1000Invalid format: 1,000.00 or 100016.
	public double TaxAmt;//	N	12(2)	N	Tax amount incurredTax amount incurredPlease exclude “,” sign.Please exclude “,” sign.e.g. 1000.00 for IDRe.g. 1000.00 for IDRInvalid format: 1,000.00 or 1000Invalid format: 1,000.00 or 100017.
	public String MerchantName;//	AN	25	N	Merchant’s business nameMerchant’s business name18.
	public String CustMAC;//	AN	50	N	Machine ID (MAC Address) of customer’s computer/device which was used to make paymentMachine ID (MAC Address) of customer’s computer/device which was used to make payment19.
	public String MerchantApprovalURL;//	AN	255	N	URL to link to merchant’s website when payment is approvedURL to link to merchant’s website when payment is approvedIf not provided, MerchantReturnURL will be usedIf not provided, MerchantReturnURL will be used20.
	public String MerchantUnApprovalURL;//	AN	255	N	URL to link to merchant’s website when payment is declinedURL to link to merchant’s website when payment is declinedIf not provided, MerchantReturnURL will be usedIf not provided, MerchantReturnURL will be used21.
	public String MerchantCallBackURL;//	AN	255	N	Server-to-server URL as an additional link to merchant’s website to be informed of transaction statusServer-to-server URL as an additional link to merchant’s website to be informed of transaction statusThis is useful when browser redirect URLs (MerchantReturnURL/MerchantApprovalThis is useful when browser redirect URLs (MerchantReturnURL/MerchantApprovaleGHL API Version 2.8geGHL API Version 2.8gMerchant Integration Guide GHL ePaymentsMerchant Integration Guide GHL ePaymentsWarning! This document is strictly private and confidential. This document is not intended for public circulation. DuplicationWarning! This document is strictly private and confidential. This document is not intended for public circulation. Duplicationof this document is prohibited and will be considered as a infringement of intellectual property laws.of this document is prohibited and will be considered as a infringement of intellectual property laws.Document Ref: eGHL_API_v2.8g.docDocument Ref: eGHL_API_v2.8g.docDocument Written by: GHL ePayments Page 10Document Written by: GHL ePayments Page 10URL/MerchantUnApprovalURL) were not able to receive payment response due to buyer’s Internet connectivity problem or buyer closed browserURL/MerchantUnApprovalURL) were not able to receive payment response due to buyer’s Internet connectivity problem or buyer closed browserUpon receiving response from Gateway, MerchantCallBackURL is to return an acknowledgement message “OK” to the Gateway or else Gateway will continue to send response to this URL for a maximum of 3 timesUpon receiving response from Gateway, MerchantCallBackURL is to return an acknowledgement message “OK” to the Gateway or else Gateway will continue to send response to this URL for a maximum of 3 times22.
	public String languageCode;//	A	2	N	Language Code for eGHL Payment Info Collection PageLanguage Code for eGHL Payment Info Collection PageRe: Section 3.7Re: Section 3.723.
	public int PageTimeout;//	N	4	N	eGHL Payment Info Collection Page timeout in secondseGHL Payment Info Collection Page timeout in secondsApplicable for merchant system which would like to bring forward to Payment Gateway, the time remaining before product/order is releasedApplicable for merchant system which would like to bring forward to Payment Gateway, the time remaining before product/order is releasedFor example, a movie ticket sales page shows time remaining countdown from 15 minutes till 5 minutes. Upon customer’s clicking “checkout / proceed / pay” button, merchant system can then pass the value of (5 minutes x 60 seconds=300) seconds in this field to Gateway which will then continue the countdown from 5 minutes. Upon timeout, all entry fields and buttons on the Collection Page will be disabledFor example, a movie ticket sales page shows time remaining countdown from 15 minutes till 5 minutes. Upon customer’s clicking “checkout / proceed / pay” button, merchant system can then pass the value of (5 minutes x 60 seconds=300) seconds in this field to Gateway which will then continue the countdown from 5 minutes. Upon timeout, all entry fields and buttons on the Collection Page will be disabled24.
	public String CardHolder;//	AN	30	N	Cardholder’s NameCardholder’s NameFor PymtMethod “CC”, if not provided, Payment Gateway will prompt this field on eGHL Payment Info Collection PageFor PymtMethod “CC”, if not provided, Payment Gateway will prompt this field on eGHL Payment Info Collection Page25.
	public String CardNo;//	N	19	N	Credit Card Number used for payment authorizationCredit Card Number used for payment authorizationFor PymtMethod “CC”, if not provided, Payment Gateway will prompt this field on eGHL Payment Info Collection PageFor PymtMethod “CC”, if not provided, Payment Gateway will prompt this field on eGHL Payment Info Collection PageIf merchant has own payment page to collect card information, merchant system is required to be PCI compliantIf merchant has own payment page to collect card information, merchant system is required to be PCI complianteGHL API Version 2.8geGHL API Version 2.8gMerchant Integration Guide GHL ePaymentsMerchant Integration Guide GHL ePaymentsWarning! This document is strictly private and confidential. This document is not intended for public circulation. DuplicationWarning! This document is strictly private and confidential. This document is not intended for public circulation. Duplicationof this document is prohibited and will be considered as a infringement of intellectual property laws.of this document is prohibited and will be considered as a infringement of intellectual property laws.Document Ref: eGHL_API_v2.8g.docDocument Ref: eGHL_API_v2.8g.docDocument Written by: GHL ePayments Page 11Document Written by: GHL ePayments Page 1126.
	public String CardExp;//	N	6	N	Expiry date of credit card. Date format is YYYYMM, e.g. 201312 for year 2013 DecemberExpiry date of credit card. Date format is YYYYMM, e.g. 201312 for year 2013 DecemberFor PymtMethod “CC”, if not provided, Payment Gateway will prompt this field on eGHL Payment Info Collection PageFor PymtMethod “CC”, if not provided, Payment Gateway will prompt this field on eGHL Payment Info Collection Page27.
	public String CardCVV2;//	N	4	N	3-4 digits Card Verification Value available on the back of credit card3-4 digits Card Verification Value available on the back of credit cardFor PymtMethod “CC”, if not provided, Payment Gateway will prompt this field on eGHL Payment Info Collection PageFor PymtMethod “CC”, if not provided, Payment Gateway will prompt this field on eGHL Payment Info Collection Page28.
	public String IssuingBank;//	AN	30	N	For PymtMethod “CC”, this field indicates Bank which issued the credit card used for this transactionFor PymtMethod “CC”, this field indicates Bank which issued the credit card used for this transactionIf not provided, Payment Gateway will prompt this field on eGHL Payment Info Collection PageIf not provided, Payment Gateway will prompt this field on eGHL Payment Info Collection PageFor PymtMethod “DD”, this field indicates Direct Debit banks/payment switches.For PymtMethod “DD”, this field indicates Direct Debit banks/payment switches.If not provided, the list of Direct Debit/payment switches supported will be shown on eGHL Payment Info Collection PageIf not provided, the list of Direct Debit/payment switches supported will be shown on eGHL Payment Info Collection Page29.
	public String BillAddr;//	AN	100	N	Billing Address (excludes postcode, town/city, state and country)Billing Address (excludes postcode, town/city, state and country)For PymtMethod “CC”, if not provided, Payment Gateway will prompt this field on eGHL Payment Info Collection PageFor PymtMethod “CC”, if not provided, Payment Gateway will prompt this field on eGHL Payment Info Collection Page30.
	public String BillPostal;//	AN	15	N	Billing PostcodeBilling PostcodeFor PymtMethod “CC”, if not provided, Payment Gateway will prompt this field on eGHL Payment Info Collection PageFor PymtMethod “CC”, if not provided, Payment Gateway will prompt this field on eGHL Payment Info Collection Page31.
	public String BillCity;//	A	30	N	Billing Town/CityBilling Town/CityFor PymtMethod “CC”, if not provided, Payment Gateway will prompt this field on eGHL Payment Info Collection PageFor PymtMethod “CC”, if not provided, Payment Gateway will prompt this field on eGHL Payment Info Collection Page32.
	public String BillRegion;//	A	30	N	Billing Region/StateBilling Region/StateeGHL API Version 2.8geGHL API Version 2.8gMerchant Integration Guide GHL ePaymentsMerchant Integration Guide GHL ePaymentsWarning! This document is strictly private and confidential. This document is not intended for public circulation. DuplicationWarning! This document is strictly private and confidential. This document is not intended for public circulation. Duplicationof this document is prohibited and will be considered as a infringement of intellectual property laws.of this document is prohibited and will be considered as a infringement of intellectual property laws.Document Ref: eGHL_API_v2.8g.docDocument Ref: eGHL_API_v2.8g.docDocument Written by: GHL ePayments Page 12Document Written by: GHL ePayments Page 12For PymtMethod “CC”, if not provided, Payment Gateway will prompt this field on eGHL Payment Info Collection PageFor PymtMethod “CC”, if not provided, Payment Gateway will prompt this field on eGHL Payment Info Collection Page33.
	public String BillCountry;//	A	2	N	Billing Country CodeBilling Country CodeRe: Section 3.8Re: Section 3.8For PymtMethod “CC”, if not provided, Payment Gateway will prompt this field on eGHL Payment Info Collection PageFor PymtMethod “CC”, if not provided, Payment Gateway will prompt this field on eGHL Payment Info Collection Page34.
	public String ShipAddr;//	AN	100	N	Shipping Address (excludes postcode, town/city, state and country)Shipping Address (excludes postcode, town/city, state and country)35.
	public String ShipPostal;//	AN	15	N	Shipping PostcodeShipping Postcode36.
	public String ShipCity;//	A	30	N	Shipping Town/CityShipping Town/City37.
	public String ShipRegion;//	A	30	N	Shipping Region/StateShipping Region/State38.
	public String ShipCountry;//	A	2	N	Shipping Country CodeShipping Country CodeRe: Section 3.8Re: Section 3.839.
	public String SessionID;//	AN	100	N	Session IDSession ID40.
	public String TokenType;//	A	3	N	Token TypeToken TypeOCP – One-click PaymentOCP – One-click Payment41.
	public String Token;//	ANS	50	C	Token ValueToken ValueIf TokenType is specified, Token is expected to have valueIf TokenType is specified, Token is expected to have value42.
	public String Param6;//	ANS	50	N	Additional data from merchant system that will be passed back to merchant in payment responseAdditional data from merchant system that will be passed back to merchant in payment response43.
	public String Param7;//	ANS	50	N	Additional data from merchant system that will be passed back to merchant in payment responseAdditional data from merchant system that will be passed back to merchant in payment response44.
	public int EPPMonth;//	N	2	N	Number of months for the installmentNumber of months for the installmenteGHL API Version 2.8geGHL API Version 2.8gMerchant Integration Guide GHL ePaymentsMerchant Integration Guide GHL ePaymentsWarning! This document is strictly private and confidential. This document is not intended for public circulation. DuplicationWarning! This document is strictly private and confidential. This document is not intended for public circulation. Duplicationof this document is prohibited and will be considered as a infringement of intellectual property laws.of this document is prohibited and will be considered as a infringement of intellectual property laws.Document Ref: eGHL_API_v2.8g.docDocument Ref: eGHL_API_v2.8g.docDocument Written by: GHL ePayments Page 13Document Written by: GHL ePayments Page 1345.
	public String PromoCode;//	AN	10	N	Promotion Code registered in eGHL.Promotion Code registered in eGHL.If provided, the transaction will be entitled for the specific promotionIf provided, the transaction will be entitled for the specific promotion
	
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
	public String getPaymentDesc() {
		return PaymentDesc;
	}
	public void setPaymentDesc(String paymentDesc) {
		PaymentDesc = paymentDesc;
	}
	public String getMerchantReturnURL() {
		return MerchantReturnURL;
	}
	public void setMerchantReturnURL(String merchantReturnURL) {
		MerchantReturnURL = merchantReturnURL;
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
	public String getCustIP() {
		return CustIP;
	}
	public void setCustIP(String custIP) {
		CustIP = custIP;
	}
	public String getCustName() {
		return CustName;
	}
	public void setCustName(String custName) {
		CustName = custName;
	}
	public String getCustEmail() {
		return CustEmail;
	}
	public void setCustEmail(String custEmail) {
		CustEmail = custEmail;
	}
	public String getCustPhone() {
		return CustPhone;
	}
	public void setCustPhone(String custPhone) {
		CustPhone = custPhone;
	}
	public double getB4TaxAmt() {
		return B4TaxAmt;
	}
	public void setB4TaxAmt(double b4TaxAmt) {
		B4TaxAmt = b4TaxAmt;
	}
	public double getTaxAmt() {
		return TaxAmt;
	}
	public void setTaxAmt(double taxAmt) {
		TaxAmt = taxAmt;
	}
	public String getMerchantName() {
		return MerchantName;
	}
	public void setMerchantName(String merchantName) {
		MerchantName = merchantName;
	}
	public String getCustMAC() {
		return CustMAC;
	}
	public void setCustMAC(String custMAC) {
		CustMAC = custMAC;
	}
	public String getMerchantApprovalURL() {
		return MerchantApprovalURL;
	}
	public void setMerchantApprovalURL(String merchantApprovalURL) {
		MerchantApprovalURL = merchantApprovalURL;
	}
	public String getMerchantUnApprovalURL() {
		return MerchantUnApprovalURL;
	}
	public void setMerchantUnApprovalURL(String merchantUnApprovalURL) {
		MerchantUnApprovalURL = merchantUnApprovalURL;
	}
	public String getMerchantCallBackURL() {
		return MerchantCallBackURL;
	}
	public void setMerchantCallBackURL(String merchantCallBackURL) {
		MerchantCallBackURL = merchantCallBackURL;
	}
	public String getLanguageCode() {
		return languageCode;
	}
	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}
	public int getPageTimeout() {
		return PageTimeout;
	}
	public void setPageTimeout(int pageTimeout) {
		PageTimeout = pageTimeout;
	}
	public String getCardHolder() {
		return CardHolder;
	}
	public void setCardHolder(String cardHolder) {
		CardHolder = cardHolder;
	}
	public String getCardNo() {
		return CardNo;
	}
	public void setCardNo(String cardNo) {
		CardNo = cardNo;
	}
	public String getCardExp() {
		return CardExp;
	}
	public void setCardExp(String cardExp) {
		CardExp = cardExp;
	}
	public String getCardCVV2() {
		return CardCVV2;
	}
	public void setCardCVV2(String cardCVV2) {
		CardCVV2 = cardCVV2;
	}
	public String getIssuingBank() {
		return IssuingBank;
	}
	public void setIssuingBank(String issuingBank) {
		IssuingBank = issuingBank;
	}
	public String getBillAddr() {
		return BillAddr;
	}
	public void setBillAddr(String billAddr) {
		BillAddr = billAddr;
	}
	public String getBillPostal() {
		return BillPostal;
	}
	public void setBillPostal(String billPostal) {
		BillPostal = billPostal;
	}
	public String getBillCity() {
		return BillCity;
	}
	public void setBillCity(String billCity) {
		BillCity = billCity;
	}
	public String getBillRegion() {
		return BillRegion;
	}
	public void setBillRegion(String billRegion) {
		BillRegion = billRegion;
	}
	public String getBillCountry() {
		return BillCountry;
	}
	public void setBillCountry(String billCountry) {
		BillCountry = billCountry;
	}
	public String getShipAddr() {
		return ShipAddr;
	}
	public void setShipAddr(String shipAddr) {
		ShipAddr = shipAddr;
	}
	public String getShipPostal() {
		return ShipPostal;
	}
	public void setShipPostal(String shipPostal) {
		ShipPostal = shipPostal;
	}
	public String getShipCity() {
		return ShipCity;
	}
	public void setShipCity(String shipCity) {
		ShipCity = shipCity;
	}
	public String getShipRegion() {
		return ShipRegion;
	}
	public void setShipRegion(String shipRegion) {
		ShipRegion = shipRegion;
	}
	public String getShipCountry() {
		return ShipCountry;
	}
	public void setShipCountry(String shipCountry) {
		ShipCountry = shipCountry;
	}
	public String getSessionID() {
		return SessionID;
	}
	public void setSessionID(String sessionID) {
		SessionID = sessionID;
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
	public int getEPPMonth() {
		return EPPMonth;
	}
	public void setEPPMonth(int ePPMonth) {
		EPPMonth = ePPMonth;
	}
	public String getPromoCode() {
		return PromoCode;
	}
	public void setPromoCode(String promoCode) {
		PromoCode = promoCode;
	}
	
	public String generateHashPaymentRequest(String password){
		String hash = password;
		DecimalFormat df = new DecimalFormat("#.00");
		hash += ((StringUtils.isBlank(ServiceID)) ? "" : ServiceID) ;
		hash += ((StringUtils.isBlank(PaymentID)) ? "" : PaymentID) ;
		hash += ((StringUtils.isBlank(MerchantReturnURL)) ? "" : MerchantReturnURL) ;
		hash += ((StringUtils.isBlank(MerchantApprovalURL)) ? "" : MerchantApprovalURL) ;
		hash += ((StringUtils.isBlank(MerchantUnApprovalURL)) ? "" : MerchantUnApprovalURL) ;
		hash += ((StringUtils.isBlank(MerchantCallBackURL)) ? "" : MerchantCallBackURL) ;
		hash += ((Amount==0) ? "" : df.format(Amount)) ;
		hash += ((StringUtils.isBlank(CurrencyCode)) ? "" : CurrencyCode) ;
		hash += ((StringUtils.isBlank(CustIP)) ? "" : CustIP) ;
		hash += ((PageTimeout==0) ? "" : PageTimeout) ;
		hash += ((StringUtils.isBlank(CardNo)) ? "" : CardNo) ;
		hash += ((StringUtils.isBlank(Token)) ? "" : Token) ;
		
//		System.out.println("BeforeHash:"+hash);
		return DigestUtils.sha256Hex(hash);
	}
	
	
	
	public static void main(String[] args) {
		
		EGHLPaymentRequest eghlPaymentRequest = new EGHLPaymentRequest();
        eghlPaymentRequest.setTransactionType("SALE");//A	7	Y	SALE – Direct captured for credit card payment; Payment request for other payment methodsSALE – Direct captured for credit card payment; Payment request for other payment methodsAUTH – For credit card payment, authorize the availability of funds for a transaction but delay the capture of funds until a later time. This is often useful for merchants who have a delayed order fulfillment process. Authorize & Capture also enables merchants to modify the original authorization amount due to order changes occurring after the initial order is placed, such as taxes, shipping or item availabilityAUTH – For credit card payment, authorize the availability of funds for a transaction but delay the capture of funds until a later time. This is often useful for merchants who have a delayed order fulfillment process. Authorize & Capture also enables merchants to modify the original authorization amount due to order changes occurring after the initial order is placed, such as taxes, shipping or item availabilityRe: Section 2.5 for Transaction Type CAPTURE message formatRe: Section 2.5 for Transaction Type CAPTURE message format2.
        eghlPaymentRequest.setPymtMethod("ANY");//	A	3	Y	Payment MethodPayment MethodCC – Credit Card (Online 3D/Non3D)CC – Credit Card (Online 3D/Non3D)MO – Credit Card (MOTO – Mail Order Telephone Order)MO – Credit Card (MOTO – Mail Order Telephone Order)DD – Direct Debit (not applicable to TransactionType AUTH)DD – Direct Debit (not applicable to TransactionType AUTH)WA – e-Wallet (not applicable to Transaction Type AUTH)WA – e-Wallet (not applicable to Transaction Type AUTH)ANY – All payment method(s) registered with eGHLANY – All payment method(s) registered with eGHL3.
    	eghlPaymentRequest.setServiceID("sit");//	AN	3	Y	Merchant Service ID given by eGHLMerchant Service ID given by eGHL4.
    	eghlPaymentRequest.setPaymentID(System.currentTimeMillis()+"");//	AN	20	Y	Unique transaction ID/reference code assigned by merchant for this payment transactionUnique transaction ID/reference code assigned by merchant for this payment transaction(No duplicate PaymentID is allowed)(No duplicate PaymentID is allowed)5.
    	eghlPaymentRequest.setOrderNumber("IJKLMN");//	AN	20	Y	Reference number / Invoice number for this orderReference number / Invoice number for this orderPaymentID must be unique but OrderNumber can be the same under different PaymentID, indicating multiplePaymentID must be unique but OrderNumber can be the same under different PaymentID, indicating multipleeGHL API Version 2.8geGHL API Version 2.8gMerchant Integration Guide GHL ePaymentsMerchant Integration Guide GHL ePaymentsWarning! This document is strictly private and confidential. This document is not intended for public circulation. DuplicationWarning! This document is strictly private and confidential. This document is not intended for public circulation. Duplicationof this document is prohibited and will be considered as a infringement of intellectual property laws.of this document is prohibited and will be considered as a infringement of intellectual property laws.Document Ref: eGHL_API_v2.8g.docDocument Ref: eGHL_API_v2.8g.docDocument Written by: GHL ePayments Page 8Document Written by: GHL ePayments Page 8payment attempts are made on a particular orderpayment attempts are made on a particular orderIf Order Number is not applicable, please provide the same value as PaymentIDIf Order Number is not applicable, please provide the same value as PaymentID6.
    	eghlPaymentRequest.setPaymentDesc("Booking No: IJKLMN, Sector: KUL-BKI, First Flight Date: 26 Sep 2012");//	AN	100	Y	Order’s descriptionsOrder’s descriptions7.
    	eghlPaymentRequest.setMerchantName("SSM");
    	eghlPaymentRequest.setMerchantReturnURL("https://merchA.merchdomain.com/pymtresp.aspx");//	AN	255	Y	Merchant system’s browser redirect URL which receives payment response from eGHL when transaction is completed (approved/declined/system error/Merchant system’s browser redirect URL which receives payment response from eGHL when transaction is completed (approved/declined/system error/cancelled by buyer on eGHL Payment Page)cancelled by buyer on eGHL Payment Page)If MerchantApprovalURL is provided, when payment is approved, MerchantApprovalURL will be used instead of MerchantReturnURLIf MerchantApprovalURL is provided, when payment is approved, MerchantApprovalURL will be used instead of MerchantReturnURLIf MerchantUnApprovalURL is provided, when payment is declined, MerchantUnApprovalURL will be used instead of MerchantReturnURLIf MerchantUnApprovalURL is provided, when payment is declined, MerchantUnApprovalURL will be used instead of MerchantReturnURL8.
    	eghlPaymentRequest.setAmount(228.00);//	N	12(2)	Y	Payment amount in 2 decimal places regardless whether the currency has decimal places or not.Payment amount in 2 decimal places regardless whether the currency has decimal places or not.Please exclude “,” sign.Please exclude “,” sign.e.g. 1000.00 for IDRe.g. 1000.00 for IDRInvalid format: 1,000.00 or 1000Invalid format: 1,000.00 or 10009.
    	eghlPaymentRequest.setCurrencyCode("MYR");//	A	3	Y	3-letter ISO4217 of Payment Currency Code3-letter ISO4217 of Payment Currency CodeRe: Section 3.6Re: Section 3.610.
    	
    	eghlPaymentRequest.setCustIP("192.168.2.35");//	AN	20	Y	Customer’s IP address captured by merchant systemCustomer’s IP address captured by merchant system12.
    	eghlPaymentRequest.setCustName("Jason");//	AN	50	Y	Customer NameCustomer NameeGHL API Version 2.8geGHL API Version 2.8gMerchant Integration Guide GHL ePaymentsMerchant Integration Guide GHL ePaymentsWarning! This document is strictly private and confidential. This document is not intended for public circulation. DuplicationWarning! This document is strictly private and confidential. This document is not intended for public circulation. Duplicationof this document is prohibited and will be considered as a infringement of intellectual property laws.of this document is prohibited and will be considered as a infringement of intellectual property laws.Document Ref: eGHL_API_v2.8g.docDocument Ref: eGHL_API_v2.8g.docDocument Written by: GHL ePayments Page 9Document Written by: GHL ePayments Page 913.
    	eghlPaymentRequest.setCustEmail("Jason@gmail.com");//	AN	60	Y	Customer’s Email AddressCustomer’s Email Address14.
    	eghlPaymentRequest.setCustPhone("60126902328");
		
    	eghlPaymentRequest.setLanguageCode("en");
//    	eghlPaymentRequest.setPageTimeout(780);
//    	eghlPaymentRequest.setEPPMonth(6);

    	eghlPaymentRequest.setHashValue(eghlPaymentRequest.generateHashPaymentRequest("sit12345"));
    	
		System.out.println("PaymentId:"+eghlPaymentRequest.getPaymentID());
		System.out.println("HashValue:"+eghlPaymentRequest.getHashValue());
	}
	
	
}
