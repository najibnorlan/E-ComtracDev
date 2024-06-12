package com.ssm.ezbiz.eghl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

public class EGHLQueryRequest implements Serializable{

	public String TransactionType ;//A	7	Y	SALE – Direct captured for credit card payment; Payment request for other payment methodsSALE – Direct captured for credit card payment; Payment request for other payment methodsAUTH – For credit card payment, authorize the availability of funds for a transaction but delay the capture of funds until a later time. This is often useful for merchants who have a delayed order fulfillment process. Authorize & Capture also enables merchants to modify the original authorization amount due to order changes occurring after the initial order is placed, such as taxes, shipping or item availabilityAUTH – For credit card payment, authorize the availability of funds for a transaction but delay the capture of funds until a later time. This is often useful for merchants who have a delayed order fulfillment process. Authorize & Capture also enables merchants to modify the original authorization amount due to order changes occurring after the initial order is placed, such as taxes, shipping or item availabilityRe: Section 2.5 for Transaction Type CAPTURE message formatRe: Section 2.5 for Transaction Type CAPTURE message format2.
	public String PymtMethod;
	public String ServiceID;//	AN	3	Y	Merchant Service ID given by eGHLMerchant Service ID given by eGHL4.
	public String PaymentID;//	AN	20	Y	Unique transaction ID/reference code assigned by merchant for this payment transactionUnique transaction ID/reference code assigned by merchant for this payment transaction(No duplicate PaymentID is allowed)(No duplicate PaymentID is allowed)5.
	public double Amount;//	N	12(2)	Y	Payment amount in 2 decimal places regardless whether the currency has decimal places or not.Payment amount in 2 decimal places regardless whether the currency has decimal places or not.Please exclude “,” sign.Please exclude “,” sign.e.g. 1000.00 for IDRe.g. 1000.00 for IDRInvalid format: 1,000.00 or 1000Invalid format: 1,000.00 or 10009.
	public String CurrencyCode;//	A	3	Y	3-letter ISO4217 of Payment Currency Code3-letter ISO4217 of Payment Currency CodeRe: Section 3.6Re: Section 3.610.
	public String HashValue;//	AN	100	Y	Message digest value calculated by Merchant System in hexadecimal public String using SHA256 hash algorithmMessage digest value calculated by Merchant System in hexadecimal public String using SHA256 hash algorithmRe: Section 2.7.1.1Re: Section 2.7.1.111.
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
	public String generateHashPaymentRequest(String password){
		String hash = password;
		DecimalFormat df = new DecimalFormat("#.00");
		hash += ((StringUtils.isBlank(ServiceID)) ? "" : ServiceID) ;
		hash += ((StringUtils.isBlank(PaymentID)) ? "" : PaymentID) ;
		hash += ((Amount==0) ? "" : df.format(Amount)) ;
		hash += ((StringUtils.isBlank(CurrencyCode)) ? "" : CurrencyCode) ;
		
//		System.out.println("BeforeHash:"+hash);
		return DigestUtils.sha256Hex(hash);
	}
	
	
	public static void main34(String[] args)throws Exception {
		String proxyHost = "proxy01.ssm.com.my"; //replace with your proxy server name or IP
		int proxyPort = 80; //your proxy server port
		SocketAddress addr = new InetSocketAddress(proxyHost, proxyPort);
		Proxy httpProxy = new Proxy(Proxy.Type.HTTP, addr);
		 
//		System.setProperty("java.net.useSystemProxies", "true");
		
		URLConnection urlConn = null;
		BufferedReader reader = null;
		String response = "";
		String output = "";
		
		String urlOverHttps = "https://test2pay.ghl.com/IPGSG/Payment.aspx";
		URL url = new URL(urlOverHttps);
		
		//Pass the Proxy instance defined above, to the openConnection() method
		urlConn = url.openConnection(httpProxy); 

		urlConn.connect();

		reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
		response = reader.readLine();
		while (response!=null) {
		    output+= response;
		    response = reader.readLine();
		    System.out.println(response);
		}   
	}
	public static void main3(String[] args)throws Exception {
		  

        	System.setProperty("java.net.useSystemProxies", "true");
//        	System.setProperty("https.proxyHost", "proxy.ssm.com.my");
//        	System.setProperty("https.proxyPort", "80");
        	
	        String urlOverHttps = "https://test2pay.ghl.com/IPGSG/Payment.aspx";
	        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
	            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                return null;
	            }
	            
				@Override
				public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws CertificateException {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws CertificateException {
					// TODO Auto-generated method stub
					
				}
	        } };
	        
	        // Install the all-trusting trust manager
	        final SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	        // Create all-trusting host name verifier
	        HostnameVerifier allHostsValid = new HostnameVerifier() {
	            public boolean verify(String hostname, SSLSession session) {
	                return true;
	            }
	        };

	        // Install the all-trusting host verifier
	        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

	        URL url = new URL(urlOverHttps);
	        URLConnection con = url.openConnection();
	        final Reader reader = new InputStreamReader(con.getInputStream());
	        final BufferedReader br = new BufferedReader(reader);        
	        String line = "";
	        while ((line = br.readLine()) != null) {
	            System.out.println(line);
	        }        
	        br.close();
	}
	
public static void main(String[] args)throws Exception {
	
	
	

	String url = "https://development.malaysiabiz.gov.my/auth/token/verify?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL2RldmVsb3BtZW50Lm1hbGF5c2lhYml6Lmdvdi5teSIsImlzc3VlIjoxNTU0MjYxMzczLCJleHBpcmVkIjoxNTg1ODg0MTYyLCJlbWFpbCI6ImhhenJ1bC5tYWhhc3NhbkB5b3BtYWlsLmNvbSIsIm5yaWMiOiI2NTA1MDgwMjUyNzEiLCJuYW1lIjoiSGF6cnVsIE1haGFzc2FuIiwibW9iaWxlTm8iOiI2MDE5MDg3NjU2MzMzMzMiLCJ2ZXJpZnlUb2tlbiI6IjVjYTQyNTdkYTExZjIiLCJ2ZXJpZnlUb2tlblVybCI6Imh0dHBzOi8vZGV2ZWxvcG1lbnQubWFsYXlzaWFiaXouZ292Lm15L2F1dGgvdG9rZW4vdmVyaWZ5IiwiYWN0aXZlIjp0cnVlfQ.YFl5SE0uM0jNJlx5Fhn3mBOrxWlOfiPT8_whePnGecDdi8mv5mVCQeR72imXMtyVvp2Kw5th986mCIlXEp4zjKtRLiodzu5BCqtsvOMndP-8E6mcNchmanYdSgV6zitxzUywVLtJze-jqkuQFciH9s7UMYJTGluC7X5qv4RsVEmOU3vMiNNkGa8_PhND-Mxl4UhaZt1wZ-ehK8LHEigE52KQgdJwtL7upkczOCE687hJ_X7AptFN6ZhWX1_KOoG6OYQPRmvPKrJx632l2DnwZrgwg4SHyU5DVZdRaEXRwSV03kWMDe_TxoGSHVra_qTD5HZtzTfwbGy-jkLcEybuZA";
	
	
		
		DecimalFormat df = new DecimalFormat("#.00");

		
//		EGHLQueryRequest queryRequest = new EGHLQueryRequest();
//		queryRequest.setTransactionType("QUERY");
//		queryRequest.setPymtMethod("ANY");
//		queryRequest.setServiceID("SKM");
//		queryRequest.setPaymentID("Z2017031600001");
//		queryRequest.setAmount(140.60);
//		queryRequest.setCurrencyCode("MYR");
//		queryRequest.setHashValue(queryRequest.generateHashPaymentRequest("skm12345"));
//		String url = "https://test2pay.ghl.com/IPGSG/Payment.aspx";
//		
		
		//SSLv3,TLSv1,TLSv1.1,TLSv1.2)
		System.setProperty("https.protocols", "SSLv3,TLSv1,TLSv1.1,TLSv1.2");
//		EGHLQueryRequest queryRequest = new EGHLQueryRequest();
//		queryRequest.setTransactionType("QUERY");
//		queryRequest.setPymtMethod("ANY");
//		queryRequest.setServiceID("SKM");
////		E2017092100745
////		E2017092100744
//		queryRequest.setPaymentID("E2019081400427");//73 76
//		
//		queryRequest.setAmount(310);
//		queryRequest.setCurrencyCode("MYR");
//		queryRequest.setHashValue(queryRequest.generateHashPaymentRequest("ZwjEfo5y"));//password kena set
//		
		
		Properties systemProperties = System.getProperties();
//		System.setProperty("java.net.useSystemProxies", "true");

		
		SSLContext sc = SSLContext.getInstance("TLSv1.2"); //$NON-NLS-1$
//		sc.init(null, null, new java.security.SecureRandom());
		sc.init(null, null, null);

		
		
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		con.setSSLSocketFactory(sc.getSocketFactory());

		//add reuqest header
		con.setRequestMethod("POST");
//		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		String urlParameters = "token=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL2RldmVsb3BtZW50Lm1hbGF5c2lhYml6Lmdvdi5teSIsImlzc3VlIjoxNTU0MjYxMzczLCJleHBpcmVkIjoxNTg1ODg0MTYyLCJlbWFpbCI6ImhhenJ1bC5tYWhhc3NhbkB5b3BtYWlsLmNvbSIsIm5yaWMiOiI2NTA1MDgwMjUyNzEiLCJuYW1lIjoiSGF6cnVsIE1haGFzc2FuIiwibW9iaWxlTm8iOiI2MDE5MDg3NjU2MzMzMzMiLCJ2ZXJpZnlUb2tlbiI6IjVjYTQyNTdkYTExZjIiLCJ2ZXJpZnlUb2tlblVybCI6Imh0dHBzOi8vZGV2ZWxvcG1lbnQubWFsYXlzaWFiaXouZ292Lm15L2F1dGgvdG9rZW4vdmVyaWZ5IiwiYWN0aXZlIjp0cnVlfQ.YFl5SE0uM0jNJlx5Fhn3mBOrxWlOfiPT8_whePnGecDdi8mv5mVCQeR72imXMtyVvp2Kw5th986mCIlXEp4zjKtRLiodzu5BCqtsvOMndP-8E6mcNchmanYdSgV6zitxzUywVLtJze-jqkuQFciH9s7UMYJTGluC7X5qv4RsVEmOU3vMiNNkGa8_PhND-Mxl4UhaZt1wZ-ehK8LHEigE52KQgdJwtL7upkczOCE687hJ_X7AptFN6ZhWX1_KOoG6OYQPRmvPKrJx632l2DnwZrgwg4SHyU5DVZdRaEXRwSV03kWMDe_TxoGSHVra_qTD5HZtzTfwbGy-jkLcEybuZA";
//		urlParameters += "&PymtMethod="+URLEncoder.encode(queryRequest.getPymtMethod(), "UTF-8");
//		urlParameters += "&ServiceID="+URLEncoder.encode(queryRequest.getServiceID(), "UTF-8");
//		urlParameters += "&PaymentID="+URLEncoder.encode(queryRequest.getPaymentID(), "UTF-8");
//		urlParameters += "&Amount="+URLEncoder.encode(df.format(queryRequest.getAmount()), "UTF-8");
//		urlParameters += "&CurrencyCode="+URLEncoder.encode(queryRequest.getCurrencyCode(), "UTF-8");
//		urlParameters += "&HashValue="+URLEncoder.encode(queryRequest.getHashValue(), "UTF-8");
//
//		System.out.println(url+"?"+urlParameters);
		// Send post request
//		con.setDoOutput(true);
//		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//		wr.writeBytes(urlParameters);
//		wr.flush();
//		wr.close();

		int responseCode = con.getResponseCode();
		if(responseCode==200){
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			System.out.println(response.toString());
			
//			String paramResponse[] = StringUtils.splitPreserveAllTokens(response.toString(),"&");
//			Map<String, String> mapParamData = new HashMap<String, String>();
//			for (int i = 0; i < paramResponse.length; i++) {
//				int idxOfSeperator = paramResponse[i].indexOf("=");
//				String param = paramResponse[i].substring(0, idxOfSeperator);
//				String paramValue = paramResponse[i].substring(idxOfSeperator+1);
//				mapParamData.put(param, paramValue);
//			}
//			System.out.println("ReqHASH : "+queryRequest.getHashValue());
//			EGHLQueryResponse queryResponse = new EGHLQueryResponse(mapParamData);
//			System.out.println("ResponseHASH : "+queryResponse.getHashValue());
//			System.out.println(queryResponse.getTxnStatus());
		}
		
		
	}
	
	public static void mainLatest(String[] args)throws Exception {
		
		DecimalFormat df = new DecimalFormat("#.00");

		
//		EGHLQueryRequest queryRequest = new EGHLQueryRequest();
//		queryRequest.setTransactionType("QUERY");
//		queryRequest.setPymtMethod("ANY");
//		queryRequest.setServiceID("SKM");
//		queryRequest.setPaymentID("Z2017031600001");
//		queryRequest.setAmount(140.60);
//		queryRequest.setCurrencyCode("MYR");
//		queryRequest.setHashValue(queryRequest.generateHashPaymentRequest("skm12345"));
//		String url = "https://test2pay.ghl.com/IPGSG/Payment.aspx";
//		
		
		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
		EGHLQueryRequest queryRequest = new EGHLQueryRequest();
		queryRequest.setTransactionType("QUERY");
		queryRequest.setPymtMethod("ANY");
		queryRequest.setServiceID("SKM");
//		E2017092100745
//		E2017092100744
		queryRequest.setPaymentID("E2019081400427");//73 76
		
		queryRequest.setAmount(310);
		queryRequest.setCurrencyCode("MYR");
		queryRequest.setHashValue(queryRequest.generateHashPaymentRequest("ZwjEfo5y"));//password kena set
		String url = "https://securepay.e-ghl.com/IPG/Payment.aspx";
//		String url = "https://test2pay.ghl.com/IPGSG/Payment.aspx";
		
		
		
		
		Properties systemProperties = System.getProperties();
		System.setProperty("java.net.useSystemProxies", "true");

		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
//		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		String urlParameters = "TransactionType="+URLEncoder.encode(queryRequest.getTransactionType(), "UTF-8");
		urlParameters += "&PymtMethod="+URLEncoder.encode(queryRequest.getPymtMethod(), "UTF-8");
		urlParameters += "&ServiceID="+URLEncoder.encode(queryRequest.getServiceID(), "UTF-8");
		urlParameters += "&PaymentID="+URLEncoder.encode(queryRequest.getPaymentID(), "UTF-8");
		urlParameters += "&Amount="+URLEncoder.encode(df.format(queryRequest.getAmount()), "UTF-8");
		urlParameters += "&CurrencyCode="+URLEncoder.encode(queryRequest.getCurrencyCode(), "UTF-8");
		urlParameters += "&HashValue="+URLEncoder.encode(queryRequest.getHashValue(), "UTF-8");

		System.out.println(url+"?"+urlParameters);
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		if(responseCode==200){
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			System.out.println(response.toString());
			
			String paramResponse[] = StringUtils.splitPreserveAllTokens(response.toString(),"&");
			Map<String, String> mapParamData = new HashMap<String, String>();
			for (int i = 0; i < paramResponse.length; i++) {
				int idxOfSeperator = paramResponse[i].indexOf("=");
				String param = paramResponse[i].substring(0, idxOfSeperator);
				String paramValue = paramResponse[i].substring(idxOfSeperator+1);
				mapParamData.put(param, paramValue);
			}
			System.out.println("ReqHASH : "+queryRequest.getHashValue());
			EGHLQueryResponse queryResponse = new EGHLQueryResponse(mapParamData);
			System.out.println("ResponseHASH : "+queryResponse.getHashValue());
			System.out.println(queryResponse.getTxnStatus());
		}
		
//		
//		EGHLServiceImpl eghlServiceImpl = new EGHLServiceImpl();
//		eghlServiceImpl.getPaymentStatus("1482074198218", 100.60);
		
	}
	
	
	
	
}
