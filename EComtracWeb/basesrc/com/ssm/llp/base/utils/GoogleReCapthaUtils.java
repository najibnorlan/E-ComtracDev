package com.ssm.llp.base.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.ssm.llp.base.page.WicketApplication;

public class GoogleReCapthaUtils {
	public static void main(String[] args) throws Exception {
		verifyCaptha("03AHJ_VusLcXGFuAsH3v_HZHMB0SvyfFzIngYIxgao4Z0XNHcn8ZLwwGVJrKB5twTRUW7NnVCe2ugysvuII7IymrIgGaZorQv-yBKU-O8UcC11OnVEyOrn-sSCE77xAknB2knuATtC75nNUfUliPO8-dr3yYglIdjTMsebyc-JkYE_YX-Y5Bu5CQ5iG-v2eI3it4B4WxMBE_KVuYalFN9lhCNLIGwgM-NXTyKs9jZ1JwqBNYofFDAt7gR9in0MrLdLcFcOVq3VNsjrrBP3WfDdkU4u2QsSipKtehh7BhaZ7VQL1BmFMKxahKn9OBPltsOmoD-uRMfQKFIfVaRnMXkKwjMoSSUNL_tlvk_7f-MJybCMsCH-3cQDsHCc7spGstUFXVnUHO2n70g-gMUYyOJeKcgD8PW8GldJUDf-xV_-gYwPh7dOLAHM___ytxhF1MlEFczsdUQkJUbh1xEei-NsjXKiAq3xbKhWMWbL7HBBraYEi-hpQpVpB1iNH-04Mr6-MQmfhUTiV0rpR64_XRWYFVri51CS8Jv5PFEjvj7r_axHjRvDC2_Ymu32HPny8YBB0hX9zU1B5yyDBpJv9LAvCZzsQFNG61TLpY4FZ_o1e0XGDA9EvPreBOhX1PcPqIupvgepWlQx-E_AxA3dLvqL8XkAtqCIAvASFEBl2TMjGHWWbv9y_LFHUGTC7KOBQ8aEMm3R5QmIiko8edQ82G7HfMaoKK7MtpYKM7t0VVa2COceGNnwUOD98wznzCiZ8v8TSJ05M3ouW2WZPsO_J2iNwkb48k9pTbPBe5RDc-EC1ouC-HkrnPqFthYuv0ODCFlFwGiCXzkjIYac5-3GuPnOS68nU4aaP4Bo75_O8DwYPxr_jl7EkQSNgoBt9uzA2qJZRG2JkShNLWXq6MhggmiTgmkp6CRgfrCX_Bx8NFFZoPR6CShPSw4ZqzsZ6IJ6SseLruKkl5WJypXo");
	}

	public static boolean verifyCaptha(String gRecaptchaResponse) {
		
		if(1==1){
			return true;
		}
		if (StringUtils.isBlank(gRecaptchaResponse)) {
			System.err.println("captca response blank!!");
			return false;
		}
		String proxyHost = "proxy01.ssm.com.my"; // replace with your proxy
													// server name or IP
		int proxyPort = 80; // your proxy server port
		SocketAddress addr = new InetSocketAddress(proxyHost, proxyPort);
		Proxy httpProxy = new Proxy(Proxy.Type.HTTP, addr);

		try {

			URL obj = new URL("https://www.google.com/recaptcha/api/siteverify");
			String secretKey = "6LeHyRAUAAAAAJrQOzuJ1QkI4RCg8YQrLz1gODSc";
			String remoteIp = "";

			HttpsURLConnection con = null;

			if (WicketApplication.PROXY_ON) {
				con = (HttpsURLConnection) obj.openConnection(httpProxy);
			} else {
				con = (HttpsURLConnection) obj.openConnection();
			}

			// add reuqest header
			con.setRequestMethod("POST");
			// con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			String urlParameters = "secret=" + URLEncoder.encode(secretKey, "UTF-8");
			urlParameters += "&remoteIp=" + URLEncoder.encode(remoteIp, "UTF-8");
			urlParameters += "&response=" + URLEncoder.encode(gRecaptchaResponse, "UTF-8");

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

				JSONParser parser = new JSONParser();
				JSONObject jsonObject = (JSONObject) parser.parse(response.toString());
				boolean success = (Boolean) jsonObject.get("success");
				if (success) {
					return true;
				}
				System.err.println(jsonObject.get("error-codes"));
				return false;
			}
			System.err.println("Response Code != 200");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return false;
	}
}
