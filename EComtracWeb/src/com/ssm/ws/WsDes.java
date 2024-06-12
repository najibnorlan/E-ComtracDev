package com.ssm.ws;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class WsDes {
    static final byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03 };

    public static void main(String[] args) throws Exception {
	Object sealObject = encrypt("abu", "ini ayam");
	System.out.println(sealObject);
	System.out.println(decrypt("abu", sealObject));
    }

    public static SealedObject encrypt(String pass, Object data) throws Exception {
	KeySpec keySpec = new PBEKeySpec(pass.toCharArray(), salt, 2);
	// create a secret (symmetric) key using PBE with MD5 and DES
	SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

	AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, 2);

	Cipher cipher = Cipher.getInstance(key.getAlgorithm());
	cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
	SealedObject sealedObject = new SealedObject((Serializable) data, cipher);
	return sealedObject;
    }

    public static Object decrypt(String pass, Object sealedObject) throws Exception {
	KeySpec keySpec = new PBEKeySpec(pass.toCharArray(), salt, 2);
	// create a secret (symmetric) key using PBE with MD5 and DES
	SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

	AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, 2);

	Cipher cipher = Cipher.getInstance(key.getAlgorithm());
	cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
	//
	return ((SealedObject) sealedObject).getObject(cipher);
    }

    public static byte[] objectToByte(Object obj) throws Exception{
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	ObjectOutputStream oos = new ObjectOutputStream(baos);
	oos.writeObject(obj);
	oos.close();
	return baos.toByteArray();
    }
    public static Object byteToObject(byte[] byteData) throws Exception{
	ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
	ObjectInputStream ois = new ObjectInputStream (bais);
	Object obj = ois.readObject();
	ois.close();
	return obj;
    }
}
