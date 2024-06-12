package com.ssm.ezbiz.service.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;

import org.apache.axis2.databinding.types.soapencoding.Array;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.ezbiz.eghl.EGHLPaymentRequest;
import com.ssm.ezbiz.eghl.EGHLQueryRequest;
import com.ssm.ezbiz.eghl.EGHLQueryResponse;
import com.ssm.ezbiz.service.EGHLService;
import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpPaymentTransaction;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.common.service.LlpPaymentTransactionService;
import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.page.WicketApplication;

@Service
public class EGHLServiceImpl implements EGHLService {

	private static final String TX_TYPE_SALE = "SALE";
	private static final String TX_TYPE_QUERY = "QUERY";

	private static final String PAY_METHOD_ANY = "ANY";
	private static final String PAY_METHOD_CC = "CC";

	@Autowired
	LlpParametersService llpParametersService;
	
	@Autowired
	LlpPaymentTransactionService llpPaymentTransactionService;

	@Override
	public EGHLPaymentRequest prepareSaleRequest(String paymentId, String transactionNo, String paymentDesc, double amount, String custIp,
			String custName, String custEmail, String custPhone) {

		String serviceId = llpParametersService.findByCodeTypeValue(Parameter.EGHL_CONFIG, Parameter.EGHL_CONFIG_SERVICE_ID);
		String merchName = llpParametersService.findByCodeTypeValue(Parameter.EGHL_CONFIG, Parameter.EGHL_CONFIG_MERCH_NAME);

//		String returnUrl = "http://localhost:8080/EComtracWeb/PaymentDetail?id=";
		
		String returnUrl = llpParametersService.findByCodeTypeValue(Parameter.EGHL_CONFIG, Parameter.EGHL_CONFIG_RETURN_URL_COMTRAC);
		String hashId = llpParametersService.findByCodeTypeValue(Parameter.EGHL_CONFIG, Parameter.EGHL_CONFIG_HASH_ID);

		System.out.println("Forwarded IP"+custIp);
		if(custIp != null) {
			custIp = custIp.split(",")[0];
		}
		returnUrl += paymentId;
		
		EGHLPaymentRequest eghlPaymentRequest = new EGHLPaymentRequest();
		eghlPaymentRequest.setTransactionType(TX_TYPE_SALE);
		eghlPaymentRequest.setPymtMethod(PAY_METHOD_ANY);
		eghlPaymentRequest.setServiceID(serviceId);
		eghlPaymentRequest.setPaymentID(paymentId);
		eghlPaymentRequest.setOrderNumber(transactionNo);
		eghlPaymentRequest.setPaymentDesc(paymentDesc);
		eghlPaymentRequest.setMerchantName(merchName);
		eghlPaymentRequest.setMerchantReturnURL(returnUrl);
		eghlPaymentRequest.setAmount(amount);
		eghlPaymentRequest.setCurrencyCode("MYR");
		eghlPaymentRequest.setCustIP(custIp);
		eghlPaymentRequest.setCustName(custName);
		eghlPaymentRequest.setCustEmail(custEmail);
		eghlPaymentRequest.setCustPhone(custPhone);
		eghlPaymentRequest.setHashValue(eghlPaymentRequest.generateHashPaymentRequest(hashId));

		return eghlPaymentRequest;
	}

	@Override
	public EGHLQueryResponse getPaymentStatus(String paymentId, double amount) throws SSMException {

		String serviceId = llpParametersService.findByCodeTypeValue(Parameter.EGHL_CONFIG, Parameter.EGHL_CONFIG_SERVICE_ID);
		String hashId = llpParametersService.findByCodeTypeValue(Parameter.EGHL_CONFIG, Parameter.EGHL_CONFIG_HASH_ID);
		String pgURL = llpParametersService.findByCodeTypeValue(Parameter.EGHL_CONFIG, Parameter.EGHL_CONFIG_PG_URL);

		
		DecimalFormat df = new DecimalFormat("#.00");
		EGHLQueryRequest queryRequest = new EGHLQueryRequest();
		
		
		queryRequest.setTransactionType(TX_TYPE_QUERY);
		queryRequest.setPymtMethod(PAY_METHOD_ANY);
		queryRequest.setServiceID(serviceId);
		queryRequest.setPaymentID(paymentId);
		queryRequest.setAmount(amount);
		queryRequest.setCurrencyCode("MYR");
		queryRequest.setHashValue(queryRequest.generateHashPaymentRequest(hashId));

		try {
			String proxyHost = llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_PROXY_URL);
			int proxyPort = Integer.parseInt(llpParametersService.findByCodeTypeValue(Parameter.LLP_CONFIG, Parameter.LLP_CONFIG_PROXY_PORT)); 
			SocketAddress addr = new InetSocketAddress(proxyHost, proxyPort);
			Proxy httpProxy = new Proxy(Proxy.Type.HTTP, addr);
			URL obj = new URL(pgURL);
			
			HttpsURLConnection con = null;
			
			if(WicketApplication.PROXY_ON){
				con = (HttpsURLConnection) obj.openConnection(httpProxy);
			}else{
				con = (HttpsURLConnection) obj.openConnection();
			}

			// add reuqest header
			con.setRequestMethod("POST");
			// con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			String urlParameters = "TransactionType=" + URLEncoder.encode(queryRequest.getTransactionType(), "UTF-8");
			urlParameters += "&PymtMethod=" + URLEncoder.encode(queryRequest.getPymtMethod(), "UTF-8");
			urlParameters += "&ServiceID=" + URLEncoder.encode(queryRequest.getServiceID(), "UTF-8");
			urlParameters += "&PaymentID=" + URLEncoder.encode(queryRequest.getPaymentID(), "UTF-8");
			urlParameters += "&Amount=" + URLEncoder.encode(df.format(queryRequest.getAmount()), "UTF-8");
			urlParameters += "&CurrencyCode=" + URLEncoder.encode(queryRequest.getCurrencyCode(), "UTF-8");
			urlParameters += "&HashValue=" + URLEncoder.encode(queryRequest.getHashValue(), "UTF-8");

//			System.out.println("Param="+urlParameters);
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			if (responseCode == 200) {
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
//				System.out.println(response.toString());

				String paramResponse[] = StringUtils.splitPreserveAllTokens(response.toString(), "&");
				Map<String, String> mapParamData = new HashMap<String, String>();
				for (int i = 0; i < paramResponse.length; i++) {
					int idxOfSeperator = paramResponse[i].indexOf("=");
					String param = paramResponse[i].substring(0, idxOfSeperator);
					String paramValue = paramResponse[i].substring(idxOfSeperator + 1);
					mapParamData.put(param, paramValue);
				}

				EGHLQueryResponse queryResponse = new EGHLQueryResponse(mapParamData);
				
				
				return queryResponse;
			}
			return null;

		} catch (Exception e) {
			throw new SSMException(e);
		}
	}

	@Override
	public List<EGHLQueryResponse> getStatusByAppRefNo(String appRefNo) {
		List<EGHLQueryResponse> listEghlResponse = new ArrayList<EGHLQueryResponse>();
		
		SearchCriteria sc = new SearchCriteria("appRefNo",SearchCriteria.EQUAL,appRefNo);
		SearchCriteria sc2 = new SearchCriteria("paymentDetail", SearchCriteria.NOT_EQUAL, Parameter.PAYMENT_DETAIL_OTC);
		SearchCriteria sc3 = new SearchCriteria("paymentDetail", SearchCriteria.IS_NULL);
		SearchCriteria sc4 =  new SearchCriteria(sc2,SearchCriteria.OR,sc3);
		
		sc = new SearchCriteria(sc,SearchCriteria.AND,sc4);
		
		List<LlpPaymentTransaction> listPayment = llpPaymentTransactionService.findByCriteria(sc).getList();
		
		for (int i = 0; i < listPayment.size(); i++) {
			LlpPaymentTransaction transaction = listPayment.get(i);
			try {
				EGHLQueryResponse eghlQueryResponse = getPaymentStatus(transaction.getTransactionId(),transaction.getAmount());
				listEghlResponse.add(eghlQueryResponse);
			} catch (Exception e) {
			}
		}
		
		return listEghlResponse;
	}
}
