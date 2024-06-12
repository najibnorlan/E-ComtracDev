package com.ssm.llp.base.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TokenGenerator {
	
	private static String AUTH_HASH_ALGO = "SHA-256";
	private static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static final char[] S_BASE64CHAR = { 'A', 'B', 'C', 'D', 'E', 'F',
	        'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
	        'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
	        'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
	        't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5',
	        '6', '7', '8', '9', '+', '/' };
	private static final char S_BASE64PAD = '=';
	
	public static void main(String[] args) {
		System.out.println(new TokenGenerator().generateTokenForAuthTest(args[0], args[1], args[2]));
//		System.out.println(new TokenGenerator().generateTokenForAuthTest("jayson", "2016-09-12 00:00:00", "secret"));
	}
	
	
	/**
	 * Pass the required parameter to generate token to be passed to integrasi.ssm.com.my
	 * @param loginId
	 * @param timestamp of current time
	 * @param systemPassword
	 */
	@Deprecated
	public String generateToken(String loginId, Date timestamp, String systemPassword) {
		String timestampStr = TokenGenerator.TIME_FORMAT.format(new Date());
		return generateToken(loginId, timestampStr, systemPassword);
	}

	/**
	 * Pass the required parameter to generate token to be passed to integrasi.ssm.com.my
	 * @param loginId
	 * @param timestamp of current time
	 * @param systemPassword
	 */
	@Deprecated
	public String generateToken(String loginId, String timestamp, String systemPassword ) {
		String timestampStr = TokenGenerator.TIME_FORMAT.format(new Date());
		String token = loginId + "|" + timestampStr + "|" + systemPassword;
		return createToken(token, loginId, timestampStr);
	}
	
	public String generateTokenForAuthTest(String loginId, String timestampStr, String systemPassword ) {
//		String timestampStr = TokenGenerator.TIME_FORMAT.format(new Date());
//		String timestampStr = "2016-10-18 13:00:00";
		String token = loginId + "|" + timestampStr + "|" + systemPassword;
		return createToken(token, loginId, timestampStr);
	}
	
	/**
	 * Pass the required parameter to generate token to be passed to SSM Middleware
	 * @param loginId
	 * @param timestamp of current time
	 * @param systemPassword
	 */
	public String generateTokenForAuth(String loginId, String systemPassword ) {
		String timestampStr = TokenGenerator.TIME_FORMAT.format(new Date());
//		timestampStr = "2016-11-15 12:00:00";
		String token = loginId + "|" + timestampStr + "|" + systemPassword;
		return createToken(token, loginId, timestampStr);
	}
	
	private static byte[] generateHash(byte[] message, String hashType) throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance(hashType);
		byte[] hash = messageDigest.digest(message);
		return hash;
	}
	
	private String createToken(String token, String username, String timestamp) {
		if ((token != null && token.length() > 0) && (username != null && username.length() > 0) 
				&& (timestamp != null && timestamp.length() > 0)) {
			try {
				byte[] hash = generateHash(token.getBytes(), TokenGenerator.AUTH_HASH_ALGO);
//				StringBuffer sb = new StringBuffer();
//		        for (int i = 0; i < hash.length; i++) {
//		         sb.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
//		        }
//		 
//		        System.out.println("After SHA-256 : " + sb.toString());
				
//				System.out.println("After hash : " + new String(hash));
				String digest = encode(hash);
//				System.out.println("After encode base64 : " + digest);
				String authString = username + "|" + timestamp + "|" + digest;
//				System.out.println("Concat with digest : " + authString);
				String tokenString = encode(authString.getBytes());
//				System.out.println("Final token after encode base64 : " + tokenString);
				//tokenString = Base64.encode(authString.getBytes());
				return tokenString;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	private String encode(byte[] data) {
        return encode(data, 0, data.length);
    }
	
	private String encode(byte[] data, int off, int len) {
        if (len <= 0)
            return "";
        char[] out = new char[len / 3 * 4 + 4];
        int rindex = off;
        int windex = 0;
        int rest = len - off;
        while (rest >= 3) {
            int i = ((data[rindex] & 0xff) << 16)
                    + ((data[rindex + 1] & 0xff) << 8)
                    + (data[rindex + 2] & 0xff);
            out[windex++] = S_BASE64CHAR[i >> 18];
            out[windex++] = S_BASE64CHAR[(i >> 12) & 0x3f];
            out[windex++] = S_BASE64CHAR[(i >> 6) & 0x3f];
            out[windex++] = S_BASE64CHAR[i & 0x3f];
            rindex += 3;
            rest -= 3;
        }
        if (rest == 1) {
            int i = data[rindex] & 0xff;
            out[windex++] = S_BASE64CHAR[i >> 2];
            out[windex++] = S_BASE64CHAR[(i << 4) & 0x3f];
            out[windex++] = S_BASE64PAD;
            out[windex++] = S_BASE64PAD;
        } else if (rest == 2) {
            int i = ((data[rindex] & 0xff) << 8) + (data[rindex + 1] & 0xff);
            out[windex++] = S_BASE64CHAR[i >> 10];
            out[windex++] = S_BASE64CHAR[(i >> 4) & 0x3f];
            out[windex++] = S_BASE64CHAR[(i << 2) & 0x3f];
            out[windex++] = S_BASE64PAD;
        }
        return new String(out, 0, windex);
    }
}
