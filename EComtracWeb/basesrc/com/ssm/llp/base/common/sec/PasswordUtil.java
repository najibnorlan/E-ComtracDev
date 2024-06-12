package com.ssm.llp.base.common.sec;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PasswordUtil {
	// the logger for this class
	private static final Logger logger = LoggerFactory.getLogger(PasswordUtil.class);

	public static boolean authenticatePassword(String plainPassword, String encodedPassword)
	{
		boolean isauth = false;
		byte[] whole, salt = new byte[8], compare = new byte[16], dgx;
		whole = decode(encodedPassword.substring(5)); 

		if (whole.length == 24) {

			System.arraycopy(whole, 0, salt, 0, 8);
			System.arraycopy(whole, 8, compare, 0, 16);
			MessageDigest md;
			try {
				md = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
			md.update(salt);

			dgx = md.digest(plainPassword.getBytes());

			int i;
			for (i = 0; i < dgx.length; i++) {
				if (dgx[i] != compare[i]) {
					isauth = false;
					break;
				} // if

				isauth = true;
			} // for
		}
		
		logger.debug("Password: {} authenticated? {}",  plainPassword, isauth);
		return isauth;
	}


	public static String encrptdPassword(String password, boolean lock) {
		// Create a password for this user
		byte[] hash;
		final byte[] rnd = new byte[8];
		byte[] fin = new byte[24];
		final Long date = Long.valueOf((new java.util.Date()).getTime());
		final SecureRandom r = new SecureRandom((date.toString()).getBytes());
		try {
			final MessageDigest md = MessageDigest.getInstance("MD5");
			if (!lock) {
				r.nextBytes(rnd);
				md.update(rnd);
				hash = md.digest(password.getBytes());
				System.arraycopy(rnd, 0, fin, 0, 8);
				System.arraycopy(hash, 0, fin, 8, 16);

			} else {
				fin = "*LCK*".getBytes();
			}
		} catch (NoSuchAlgorithmException e) {
			logger.debug(e.getMessage());
		}
		String encrptdPswd = "(MD5)" + encode(fin);
		return encrptdPswd;
	}

	private static byte[] decode(String base64) {
		int pad = 0;
		for (int i = base64.length() - 1; base64.charAt(i) == '='; i--)
			pad++;
		final int length = base64.length() * 6 / 8 - pad;
		final byte[] raw = new byte[length];
		int rawIndex = 0;
		for (int i = 0; i < base64.length(); i += 4) {
			final int block = (getValue(base64.charAt(i)) << 18) + (getValue(base64.charAt(i + 1)) << 12)
					+ (getValue(base64.charAt(i + 2)) << 6) + (getValue(base64.charAt(i + 3)));
			for (int j = 0; j < 3 && rawIndex + j < raw.length; j++)
				raw[rawIndex + j] = (byte) ((block >> (8 * (2 - j))) & 0xff);
			rawIndex += 3;
		}
		return raw;
	}

	protected static int getValue(char c) {
		if (c >= 'A' && c <= 'Z')
			return c - 'A';
		if (c >= 'a' && c <= 'z')
			return c - 'a' + 26;
		if (c >= '0' && c <= '9')
			return c - '0' + 52;
		if (c == '+')
			return 62;
		if (c == '/')
			return 63;
		if (c == '=')
			return 0;
		return -1;
	}

	//
	// This was originally Jonathan B. Knudsen's Example from his book
	// Java Cryptography published by O'Reilly Associates (1st Edition 1998)
	//

	private static String encode(byte[] raw) {
		StringBuffer encoded = new StringBuffer();
		for (int i = 0; i < raw.length; i += 3) {
			encoded.append(encodeBlock(raw, i));
		}
		return encoded.toString();
	}

	private static char[] encodeBlock(byte[] raw, int offset) {
		int block = 0;
		final int slack = raw.length - offset - 1;
		final int end = (slack >= 2) ? 2 : slack;
		for (int i = 0; i <= end; i++) {
			final byte b = raw[offset + i];
			final int neuter = (b < 0) ? b + 256 : b;
			block += neuter << (8 * (2 - i));
		}
		final char[] base64 = new char[4];
		for (int i = 0; i < 4; i++) {
			int sixbit = (block >>> (6 * (3 - i))) & 0x3f;
			base64[i] = getChar(sixbit);
		}
		if (slack < 1) {
			base64[2] = '=';
		}
		if (slack < 2) {
			base64[3] = '=';
		}
		return base64;
	}

	private static char getChar(int sixBit) {
		if (sixBit >= 0 && sixBit <= 25) {
			return (char) ('A' + sixBit);
		}
		if (sixBit >= 26 && sixBit <= 51) {
			return (char) ('a' + (sixBit - 26));
		}
		if (sixBit >= 52 && sixBit <= 61) {
			return (char) ('0' + (sixBit - 52));
		}
		if (sixBit == 62) {
			return '+';
		}
		if (sixBit == 63) {
			return '/';
		}
		return '?';
	}
	
}
