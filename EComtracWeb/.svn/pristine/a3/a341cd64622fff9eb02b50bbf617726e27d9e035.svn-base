/*
 * This software is the confidential and proprietary information of ThetaSP S/B
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with ThetaSP S/B.
 */
package com.ssm.llp.base.common.sec;

import java.io.Serializable;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.SerializationUtils;

/**
 * Encrypt using hash value
 *
 * @author hhf
 * @version 1.0
 */
public class MD5DigestUtils{
    
    public static String encrypt(String input) {
        byte[] b = DigestUtils.md5(input.getBytes());

        return new String(Hex.encodeHex(b));
    }

    public static boolean isPasswordValid(String md5String, String password) {
    	
    	String md5Tmp = encrypt(password);
    	if(md5String.equalsIgnoreCase(md5Tmp)){
    		return true;
    	}
        return false;
    }
    

    
    public static String hashObject(Serializable... obj) {
    	
    	String hashCombine = "";
    	for (int i = 0; i < obj.length; i++) {
    		byte[] objByte = SerializationUtils.serialize(obj[i]);
    		byte[] objByteHash = DigestUtils.md5(objByte);
    		hashCombine += new String(Hex.encodeHex(objByteHash));
		}
    	
        byte[] b = DigestUtils.md5(hashCombine.getBytes());

        return new String(Hex.encodeHex(b));
    }

}
