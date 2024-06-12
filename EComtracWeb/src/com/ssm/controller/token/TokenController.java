package com.ssm.controller.token;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.security.KeyFactory;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;
import java.text.DecimalFormat;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.crypto.util.PrivateKeyInfoFactory;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.ssm.llp.base.common.Parameter;
import com.ssm.llp.base.common.service.LlpParametersService;
import com.ssm.llp.base.page.SignInSession;
import com.ssm.llp.base.page.WicketApplication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

public class TokenController {

	public String getPing() {
		return "SSM Oauth Token API: active";
	}

	public String generateToken(String ezbizId, String extRefNo) {

		PrivateKey privateKey = null;

		String privateKeyString = "-----BEGIN RSA PRIVATE KEY-----\r\n" + "MIIEpAIBAAKCAQEA1z7bgsPK8VYZC/yr3TrNjzUWz1GTrKGcrDAt7JTkmXE/ZBam"
				+ "Gw9J+jxNEOBsQLCzTV3rejANp9r11m/GPAUWpvdxkv59Ozw9okn9dui6haHrZiWS"
				+ "LfwVfYKsnheTVZk2NOQBKQ8OzIym3Ia/wAvHiJRaCgg9SLzN4amnJO3AXE/srTTE"
				+ "o0YmQhOrdlqvZFkwKJLkGyyQ8M0shLdRn1U7U7yGxFy0JWZXI4FLxx3Eg3zuuAhY"
				+ "hDfegulU6zQjqObLMHxZ5tEDSQsEN48FYvH3D+/fpz9FRga0i756GMa04f5yZPW+"
				+ "5Yy8Wk1vCnMhL+8PiS+x9RRWSq6mzXHhVKHoawIDAQABAoIBAQCI7w89MIxCXpDO"
				+ "pG897fPqIE8jk3VMRQBkNr5wnQSVbYtzhEkmO+LHVGd+zhdwTDm1hYOSF1IIsG7n"
				+ "ffJyKpaPoCnb5xUKgzd+sG2UoVhQPYOBV3OY159gWqqwZLUxpqfqnAb09QRoMy7Q"
				+ "atVAOv40sJRbf4Rlat/bqU+RWcOftXpCVFcIUMxLH2MV10fVLtSXFAhRyhGgNg4y"
				+ "D+nbGSwWFSLjN6L8TRd80E7s7VFuqY0Sko5yLmkm+XPWHxJG5Nv5dnwHJDm7grJd"
				+ "JQVSfeQ4TelztpN+gqRkugzHQsVhi9wXSpYbR2m8FbzUGVrHh5DxIunShAuda9A7"
				+ "UQemFYTJAoGBAPGjX1CWVK/Ybr0+5lIIgQVTnlOADKtJIjj6cNkaJ6bH2vnf7XIT"
				+ "4SYL0a48wDpSQ1EhXumwhpgZOxPEmksPLOmI6KAo/nvZpIJ4EjX+J/nq9sQD9TU6"
				+ "uzjcomBqqpLz37KVErSnJERzidt37gqUB967J0bSmLmsuTcMzEJHRKq9AoGBAOQJ"
				+ "6PIrtMzyJB2d7+Gex6httid8+vlYN3JxgvIAnmwX/0mCK8oJHsBWANnMVVWbwZim"
				+ "rwbFnTNxkShafEwiGOmzywbO9TizuAk+NHhz3irDqeVRiiIwCVelOLEWYMZvLYS7"
				+ "HFZQhj3hYE20dhndLTQamEzXxUxuKuUA4h6/YKZHAoGAHgYNnsa/CQZcZBk/puu1"
				+ "lELahkd/y3yPywKbKO6OBLQKake/ihAcRozrZbnYrKZyBM+fqmurnZRs+bzQ6wic"
				+ "rPu848GUNTFKtodSw/CraOsf9CUFv7Jndt47Mr2uKgoxPlA1fcrSv8qXU3SoWBoi"
				+ "jDM24x7hqRhj/woRCx8t0gUCgYEAs1l39L+bO3VWIlUNTY3ryfQ/x17spR9UvJW6"
				+ "P0ttTeaxycavWf9CSFNb5/BlTSdrY/v0vIa/1K4FVI+WIurOXUAi9f/frfDrVZmu"
				+ "+u1mVov9G3a+BZVyQ5SWufC3vFnEmxfueZRrgE+5xmOgX0ctPkWMcyNg8XPUGLhR"
				+ "H421HhECgYBRD99hhooqxxhLflxUhSLUn//1DUeicMDhzegV/HmDiCVau+IlUR3r"
				+ "3h+yjERCD0WtQ74G93/1kiWXySQxi3hweEpGhCnDOOnXiJcveCQ7JZLldi9xGYRX" + "s358JlNsYZmhN+H3NRRI+R5KMgwHxNoKtK8/2wRXU+F8wNHVET367g=="
				+ "\r\n-----END RSA PRIVATE KEY-----";

		PemObject privateKeyObject;

		try {
			PemReader pemReader = new PemReader(new InputStreamReader(new ByteArrayInputStream(privateKeyString.getBytes())));

			privateKeyObject = pemReader.readPemObject();

//			System.out.println(privateKeyObject.toString());
		} catch (Exception ex) {
			privateKeyObject = null;
			System.out.println(privateKeyString);
			ex.printStackTrace();
		}

		RSAPrivateCrtKeyParameters privateKeyParameter = null;
		if (privateKeyObject.getType().endsWith("RSA PRIVATE KEY")) {
			// PKCS#1 key
			RSAPrivateKey rsa = RSAPrivateKey.getInstance(privateKeyObject.getContent());
			privateKeyParameter = new RSAPrivateCrtKeyParameters(rsa.getModulus(), rsa.getPublicExponent(), rsa.getPrivateExponent(), rsa.getPrime1(),
					rsa.getPrime2(), rsa.getExponent1(), rsa.getExponent2(), rsa.getCoefficient());
		} else if (privateKeyObject.getType().endsWith("PRIVATE KEY")) {
			// PKCS#8 key
			try {
				privateKeyParameter = (RSAPrivateCrtKeyParameters) PrivateKeyFactory.createKey(privateKeyObject.getContent());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			throw new RuntimeException("Unsupported key type: " + privateKeyObject.getType());
		}

		try {
			privateKey = new JcaPEMKeyConverter().getPrivateKey(PrivateKeyInfoFactory.createPrivateKeyInfo(privateKeyParameter));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;

		long nowMillis = System.currentTimeMillis();
		Date iat = new Date(nowMillis); // iat
		long exp = nowMillis + (60 * 60 * 1000); // exp
		String issuer = "ssm.gov.my";
		String subject = "malaysiabiz.gov.my";

		JwtBuilder builder = Jwts.builder().setIssuedAt(iat).setExpiration(new Date(exp)).setSubject(subject).setIssuer(issuer).claim("vli", ezbizId).claim("nric", extRefNo)
				.signWith(signatureAlgorithm, privateKey);

		return builder.compact();
	}

	public String validateToken(String token) {

		PublicKey pubKey = null;
		Resource pubKeyRes = new ClassPathResource("public2048.der");

		try (InputStream in = TokenController.class.getResourceAsStream("public2048.der")) {
			X509EncodedKeySpec spec = new X509EncodedKeySpec(IOUtils.toByteArray(in));
			KeyFactory kf = KeyFactory.getInstance("RSA");
			pubKey = kf.generatePublic(spec);
		} catch (Exception ioE) {
			ioE.printStackTrace();
		}

		int errorCode = 0;
		Jws<Claims> payload = null;
		String result = "invalid";

		try {
			// parse & claims
			payload = Jwts.parser().setSigningKey(pubKey).parseClaimsJws(token);
			String name = (String) payload.getBody().get("name");
			String verifyToken = (String) payload.getBody().get("verifyToken");
			System.out.println("Name: " + name);
			System.out.println("VerifyToken: " + verifyToken);
			result = "valid";
		} catch (JwtException | IllegalArgumentException e) {
			errorCode = 400;
		}

		return result;
	}

	public String validateTokenExtractVli(String token) throws MB_UnauthorizedException{

		PublicKey pubKey = null;
		Resource pubKeyRes = new ClassPathResource("/keys/rsa/public2048.der");

		try (InputStream in = TokenController.class.getResourceAsStream("public2048.der")) {
			X509EncodedKeySpec spec = new X509EncodedKeySpec(IOUtils.toByteArray(in));
			KeyFactory kf = KeyFactory.getInstance("RSA");
			pubKey = kf.generatePublic(spec);
		} catch (Exception ioE) {
			ioE.printStackTrace();
		}

		System.out.println(1);
		int errorCode = 0;
		Jws<Claims> payload = null;
		String vli = "";

		try {
			// parse & claims
			payload = Jwts.parser().setSigningKey(pubKey).parseClaimsJws(token);
			vli = (String) payload.getBody().get("vli");
		} catch (JwtException | IllegalArgumentException e) {
			e.printStackTrace();
			throw new MB_UnauthorizedException(e.getMessage(), MB_ErrorCode.E_401);
		}

		return vli;
	}
	public static void main(String[] args)throws Exception {
		TokenController tc = new TokenController();
//		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJ3d3cubWFsYXlzaWFiaXoubWFtcHUuZ292Lm15IiwiaWF0IjoxNTcxNzk4MjAzLCJleHAiOjE1OTc3MTgyMDMsImV4cGlyZWQiOjE1OTc3MTgyMDMsIm5yaWMiOiI4NTA0MDIwODU5NzYiLCJ2bGkiOiJGQUhNSTQifQ.GiFkFtMmwP1CyH-M6-zMTIru9mOdb_2EiVfcLcz0NR0wxWg6_DjvJf44KSlEnMFSpnKa0aofgcYX3sDHm4K9S9p76xzJL2u_dXf7-pw2cFCYDTX2CtOksZqz3ZDGFyHueufw6leXYQC2gENAs__ci5qQq2WzMEwcRYBLwsPXa3f9wZmBgjfcBHxLcd1hVMOUpXFoeAn-MOYTnp18PK1e82TewhptQC0-xBFZg4xHcyXxYAxMWSc3oTR6YJRzhijhoYOSnE79-I8jGuk3GYb-tXqKfr5uTw9zxb_1NA5KuL6B0Z_AWzhzwnc1H7ZYkuSIpmMkdfgMYuJ0W5XOFA-MGA";
//		TokenDataModel tdm = tc.validateTokenAndExtractData(token);
//		System.out.println(tdm.getVli());
//		System.out.println(tdm.getIss());
//		System.out.println(tdm.getExp());
		
//		String userid = "ZAMZAM";
//		String ic = "821217055087";
//		

		String userid = "FAHMI4";
		String ic = "850402085976";
				
		System.out.println("********PAIR********");		
		
		String s1 = tc.generatePairingSuccessJS(userid,ic);
		System.out.println(s1);
		
		
		System.out.println("********FORGOT********");
		
		String s = tc.generateResetPairingJS(userid,ic);
		System.out.println(s);
	}
	
	public TokenDataModel validateTokenAndExtractData(String token) throws MB_UnauthorizedException{

		PublicKey pubKey = null;
		Resource pubKeyRes = new ClassPathResource("/keys/rsa/public2048.der");

		try (InputStream in = TokenController.class.getResourceAsStream("public2048.der")) {
			X509EncodedKeySpec spec = new X509EncodedKeySpec(IOUtils.toByteArray(in));
			KeyFactory kf = KeyFactory.getInstance("RSA");
			pubKey = kf.generatePublic(spec);
		} catch (Exception ioE) {
			ioE.printStackTrace();
		}

//		System.out.println(1);
		int errorCode = 0;
		Jws<Claims> payload = null;
		
		TokenDataModel tdm = null;
		try {
			// parse & claims
			payload = Jwts.parser().setSigningKey(pubKey).parseClaimsJws(token);
			tdm = new TokenDataModel();
			Claims payloadBody = payload.getBody();
			
			Field field[] = TokenDataModel.class.getDeclaredFields();
			for (int i = 0; i < field.length; i++) {
				try {
					Object objValue = payloadBody.get(field[i].getName());
					if(objValue!=null) {
						if(field[i].getType().newInstance() instanceof Date) {
							objValue = new Date((int)objValue);
						}
						BeanUtils.setProperty(tdm, field[i].getName(), objValue);
					}
				} catch (Exception e) {
//					e.printStackTrace();
				}
			}
			
		} catch (MalformedJwtException | SignatureException | IllegalArgumentException e) {
			e.printStackTrace();
			throw new MB_UnauthorizedException(e.getMessage(), MB_ErrorCode.E_400);
		}catch (JwtException e) {
			e.printStackTrace();
			throw new MB_UnauthorizedException(e.getMessage(), MB_ErrorCode.E_401);
		}

		return tdm;
	}

//public String validateTokenExtractVli(String token) throws MB_UnauthorizedException {
//	    
//	    PublicKey pubKey = null;
//	    Resource pubKeyRes = new ClassPathResource("/keys/rsa/public2048.der");
//	    
//	    try (InputStream in = pubKeyRes.getInputStream()) {
//	      X509EncodedKeySpec spec = new X509EncodedKeySpec(IOUtils.toByteArray(in));
//	      KeyFactory kf = KeyFactory.getInstance("RSA");
//	      pubKey = kf.generatePublic(spec);
//	    } catch (Exception ioE) {
//	    	ioE.printStackTrace();
////	      log.error("FAILED TO LOAD PUBLIC KEY: " + ioE.getMessage());
//	    }
//
//	    System.out.println(1);  
//	    int errorCode = 0;
//	    Jws<Claims> payload = null;  
//	    String vli = "";
//	    
//	    try {
//	      Jws<Claims> result = Jwts.parser().setSigningKey(pubKey).parseClaimsJws(token);
//
//	                //check expires
//	                Long expired = new BigDecimal((Integer) result.getBody().get("expired")).longValue() * 1000;
//	                Long current = Calendar.getInstance().getTimeInMillis();
//	                System.out.println("expired date : " + new Date(expired));
//	                System.out.println("current date : " + new Date(current));
//	                if (expired < current) {
//	                	System.out.println("Opps token expired!");
//	                	throw new MB_UnauthorizedException("Token Expired", MB_ErrorCode.E_401);
//	                } else {
//	        // parse & claims
//	        payload = Jwts.parser().setSigningKey(pubKey).parseClaimsJws(token);
//	        vli = (String) payload.getBody().get("vli");
//	      }
//	    } catch (JwtException | IllegalArgumentException e) {
//	    	System.out.println("Error: Token invalid - " + e.getMessage());
//	                throw new MB_UnauthorizedException(e.getMessage(), MB_ErrorCode.E_400);
//	    }
//	    
//	    return vli;
//	  }

	public String verifyRedis(String token) {

		PublicKey pubKey = null;
		Resource pubKeyRes = new ClassPathResource("/keys/rsa/public2048.der");

		try (InputStream in = TokenController.class.getResourceAsStream("public2048.der")) {
			X509EncodedKeySpec spec = new X509EncodedKeySpec(IOUtils.toByteArray(in));
			KeyFactory kf = KeyFactory.getInstance("RSA");
			pubKey = kf.generatePublic(spec);
		} catch (Exception ioE) {
			ioE.printStackTrace();
		}

		int errorCode = 0;
		Jws<Claims> payload = null;
		String result = "";

		try {
			// parse & claims
			payload = Jwts.parser().setSigningKey(pubKey).parseClaimsJws(token);
			String verifyTokenUrl = (String) payload.getBody().get("verifyTokenUrl");

			HttpHost proxy = new HttpHost("proxy01.ssm.com.my", 80, "http");

			// call home - do REDIS check
			try (CloseableHttpClient httpClient = createAcceptSelfSignedCertificateClient()) {
				RequestConfig config = RequestConfig.custom().setProxy(proxy).build();

				String remoteUrl = verifyTokenUrl + "?token=" + token;
				System.out.println(remoteUrl);
				HttpGet httpGet = new HttpGet(remoteUrl);
				httpGet.setConfig(config);
				HttpResponse httpResponse = httpClient.execute(httpGet);
				String authResult = EntityUtils.toString(httpResponse.getEntity());

				// result from Authentication Server
				if (authResult.contains("false")) {
					result = "inactive";
				} else {
					result = "active";
				}
			} catch (Exception e) {
				e.printStackTrace();
				result = "inactive";
			}

		} catch (JwtException | IllegalArgumentException e) {
			result = "inactive";
		}

		return result;
	}

	/**
	 * create httpclient ignore ssl
	 * 
	 * @return
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 */
	private static CloseableHttpClient createAcceptSelfSignedCertificateClient()
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}
		} };
		// System.setProperty("https.protocols",
		// "TLSv1,TLSv1.1,TLSv1.1,SSLv3,SSLv2Hello");
		SSLContext context = SSLContext.getInstance("SSL");
		context.init(null, trustAllCerts, null);

		HttpClientBuilder builder = HttpClientBuilder.create();
		@SuppressWarnings("deprecation")
		// SSLConnectionSocketFactory sslConnectionFactory = new
		// SSLConnectionSocketFactory(context,
		// SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(context, new String[] { "TLSv1", "TLSv1.1", "TLSv1.2" },
				null, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		builder.setSSLSocketFactory(sslConnectionFactory);

		PlainConnectionSocketFactory plainConnectionSocketFactory = new PlainConnectionSocketFactory();
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create().register("https", sslConnectionFactory)
				.register("http", plainConnectionSocketFactory).build();

		HttpClientConnectionManager ccm = new BasicHttpClientConnectionManager(registry);

		builder.setConnectionManager(ccm);

		// SSLSocketFactory factory =
		// (SSLSocketFactory)context.getSocketFactory();
		// SSLSocket socket;
		// try {
		// socket = (SSLSocket)factory.createSocket();
		//
		//// socket.setEnabledProtocols(new String[] { "TLSv1" , "TLSv1.1",
		// "TLSv1.2" });
		// String[] protocols = socket.getSupportedProtocols();
		//
		// System.out.println("Supported Protocols: " + protocols.length);
		// for(int i = 0; i < protocols.length; i++)
		// {
		// System.out.println(" " + protocols[i]);
		// }
		//
		// protocols = socket.getEnabledProtocols();
		// System.out.println("Enabled Protocols: " + protocols.length);
		// for(int i = 0; i < protocols.length; i++)
		// {
		// System.out.println(" " + protocols[i]);
		// }
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		return builder.build();
	}
	
	
	public String generateResetPairingJS(String ezbizUsername, String idNo) throws Exception {
		DecimalFormat df = new DecimalFormat("#.00");
		LlpParametersService llpParametersService = (LlpParametersService) WicketApplication
				.getServiceNew(LlpParametersService.class.getSimpleName());
		String url = llpParametersService.findByCodeTypeValue(Parameter.EXT_CONFIG_MAMPU, Parameter.EXT_CONFIG_MAMPU_RESET_PAIR_REDIRECT_URL);

//		String url = "https://smehip1-devl-r200.gates.edu.my/lidverify/resetpassword?agencyAppsId=11&status=true";
//		String url = "https://training.malaysiabiz.gov.my/en/api/lidverify/reset-password?agencyAppsId=11&status=true";
		
		String urlParameters = "&token="+generateToken(ezbizUsername,idNo);
		url += urlParameters;
		
		String jsPGScript = generatePostJS(url);
		return jsPGScript;
	}
	
	public String generatePostJS(String url) {
		String urlParameters = StringUtils.split(url,"?")[1];
		String action = StringUtils.split(url,"?")[0];
		
		String jsPGScript = "";
		
		jsPGScript += "my_form=document.createElement('FORM');";
		jsPGScript += "my_form.name='myForm';";
		jsPGScript += "my_form.method='POST';";
		jsPGScript += "my_form.action='" + action + "';";
		
		String paramsWValues[] =  StringUtils.split(urlParameters,"&");
		
		for (int i = 0; i < paramsWValues.length; i++) {
			String fieldName = StringUtils.split(paramsWValues[i],"=")[0];
			String fieldValue = StringUtils.split(paramsWValues[i],"=")[1];
			jsPGScript += "\nmy_tb=document.createElement('INPUT');\n";
			jsPGScript += "my_tb.type='HIDDEN';\n";
			jsPGScript += "my_tb.name='" + fieldName + "';\n";
			jsPGScript += "my_tb.value='" + fieldValue + "';\n";
			jsPGScript += "my_form.appendChild(my_tb);\n";
			
		}
		
		jsPGScript += "document.body.appendChild(my_form);\n";
		jsPGScript += "my_form.submit();\n";
		
		return jsPGScript;
	}
	public String generateGetJS(String url) {
		String jsPGScript = "window.location.href = '"+url+"';";
		
		return jsPGScript;
	}
	
	public String generatePairingSuccessJS(String ezbizUsername, String idNo) throws Exception {
//		disableCertificateValidation();

		DecimalFormat df = new DecimalFormat("#.00");

//		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
		
		LlpParametersService llpParametersService = (LlpParametersService) WicketApplication
				.getServiceNew(LlpParametersService.class.getSimpleName());
		String url = llpParametersService.findByCodeTypeValue(Parameter.EXT_CONFIG_MAMPU, Parameter.EXT_CONFIG_MAMPU_PAIR_REDIRECT_URL);
		
//		String url = "https://training.malaysiabiz.gov.my/en/lidverify/response?agencyAppsId=11&status=true";
		String urlParameters = "&token="+generateToken(ezbizUsername,idNo);

		url += urlParameters;
//		if(1==1) {
//			return url;
//		}
		return generateGetJS(url);
		
		
	}

	public static void disableCertificateValidation() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new TrustAllManager() };

		// Ignore differences between given hostname and certificate hostname
		HostnameVerifier hv = new TrustAllHostnameVerifier();

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(hv);
		} catch (Exception e) {
		}
	}
}
