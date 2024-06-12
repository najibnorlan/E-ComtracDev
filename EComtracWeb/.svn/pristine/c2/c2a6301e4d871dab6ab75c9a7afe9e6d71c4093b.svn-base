package com.ssm.ssmlocalsvr;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.Arrays;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;

public class POSTerminalUtils {
	
	public static void main(String[] args) {
//		prepareSale(1.23,8877);
		
		String txnAmt = new String("000000001123");
		txnAmt = txnAmt.substring(0, txnAmt.length()-2)+"."+txnAmt.substring(txnAmt.length()-2);
    	System.out.println(txnAmt);
	}
	public static String prepareSale(String deviceName[], double saleAmt, int sessionId) throws Exception{
		
		String cmdN3 = "020";// Sale

		DecimalFormat txnAmtDF = new DecimalFormat("0000000000.00");
		String txnAmtN12 = StringUtils.remove(txnAmtDF.format(saleAmt),".");
		
		
		String invoiceN6 = "000000";
		
		
		DecimalFormat casherNoDF = new DecimalFormat("0000");// SessionID
		String casherNoANS4 = StringUtils.remove(casherNoDF.format(sessionId),".");
//		String casherNoANS4 = "8877";
		
//		String msgStr2 = cmdN3 +"|"+ txnAmtN12 +"|"+ invoiceN6 +"|"+ casherNoANS4;
//		System.out.println(msgStr2);
		
		String msgStr = cmdN3 + txnAmtN12+ invoiceN6 + casherNoANS4;
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(msgStr.getBytes());

		for (int i = 0; i < 7; i++) {
			baos.write(0xFF);
		}

		byte[] byteAll = baos.toByteArray();
		baos.close();

		byte[] first8 = Arrays.copyOfRange(byteAll, 0, 8);
		byte[] sec8 = Arrays.copyOfRange(byteAll, 0 + 8, 8 + 8);
		byte[] third8 = Arrays.copyOfRange(byteAll, 0 + 8 + 8, 8 + 8 + 8);
		byte[] forth8 = Arrays.copyOfRange(byteAll, 0 + 8 + 8 + 8, 8 + 8 + 8 + 8);

		byte[] byteXOR = first8;
		// XOR dgn sec
		for (int i = 0; i < 8; i++) {
			byteXOR[i] = (byte) (byteXOR[i] ^ sec8[i]);
			byteXOR[i] = (byte) (byteXOR[i] ^ third8[i]);
			byteXOR[i] = (byte) (byteXOR[i] ^ forth8[i]);
		}

		//
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		os.write(0x02);// Start Msj
		os.write(cmdN3.getBytes());
		os.write(txnAmtN12.getBytes());
		os.write(invoiceN6.getBytes());
		os.write(casherNoANS4.getBytes());
		os.write(byteXOR);
		os.write(0x03);// Eng Msj
		//

		byte byteData[] = os.toByteArray();
		os.close();
		

		ByteArrayOutputStream baosT = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baosT);
		
		oos.writeObject("POS:SALE");
		oos.writeObject(deviceName);
		oos.writeObject(byteData);
		
		
		oos.close();
		byte byteDataLatest[] = baosT.toByteArray();		
		baosT.close();
		
		String posCmd = Hex.encodeHexString(byteDataLatest).toUpperCase();
		return posCmd+"<END>";
	}
	public static void processResponse(String returnMsg) throws Exception{
		returnMsg = StringUtils.remove(returnMsg, "<END>");
		
		byte byteResponse[] = Hex.decodeHex(returnMsg.toCharArray()); 
		
		int start = 0;
    	byte byteStartN1[] = Arrays.copyOfRange(byteResponse, start, start+1);
    	start = start+1;
    	byte byteCommandN3[] = Arrays.copyOfRange(byteResponse, start, 1+3);
    	start = start+3;
    	byte byteStatusANS2[] = Arrays.copyOfRange(byteResponse, start, start+2);
    	start = start+2;
    	byte byteCNumberN22[] = Arrays.copyOfRange(byteResponse, start, start+22);
    	start = start+22;
    	byte byteExDtN4[] = Arrays.copyOfRange(byteResponse, start, start+4);
    	start = start+4;
    	byte byteCardTypeN2[] = Arrays.copyOfRange(byteResponse, start, start+2);
    	start = start+2;
    	byte aprovalCodeANS8[] = Arrays.copyOfRange(byteResponse, start, start+8);
    	start = start+8;
    	byte tranxAmtN12[] = Arrays.copyOfRange(byteResponse, start, start+12);
    	start = start+12;
    	byte netAmtN12[] = Arrays.copyOfRange(byteResponse, start, start+12);
    	start = start+12;
    	byte traceNoN6[] = Arrays.copyOfRange(byteResponse, start, start+6);
    	start = start+6;
    	byte invoiceNoN6[] = Arrays.copyOfRange(byteResponse, start, start+6);
    	start = start+6;
    	byte cashierNoN4[] = Arrays.copyOfRange(byteResponse, start, start+4);
    	start = start+4;
    	byte chkDigitNoN8[] = Arrays.copyOfRange(byteResponse, start, start+8);
    	start = start+8;
//    	
//    	System.out.println("byteCommandN3: "+Hex.encodeHexString(byteCommandN3) +" >> " +new String(byteCommandN3));
//    	System.out.println("byteStatusANS2: "+Hex.encodeHexString(byteStatusANS2)+" >> " +new String(byteStatusANS2));
//    	System.out.println("byteCNumberN22: "+Hex.encodeHexString(byteCNumberN22)+" >> " +new String(byteCNumberN22));
//    	System.out.println("byteExDtN4: "+Hex.encodeHexString(byteExDtN4)+" >> " +new String(byteExDtN4));
//    	System.out.println("byteCardTypeN2: "+Hex.encodeHexString(byteCardTypeN2)+" >> " +new String(byteCardTypeN2));
//    	System.out.println("aprovalCodeANS8: "+Hex.encodeHexString(aprovalCodeANS8)+" >> " +new String(aprovalCodeANS8));
//    	System.out.println("tranxAmtN12: "+Hex.encodeHexString(tranxAmtN12)+" >> " +new String(tranxAmtN12));
//    	System.out.println("netAmtN12: "+Hex.encodeHexString(netAmtN12)+" >> " +new String(netAmtN12));
//    	System.out.println("traceNoN6: "+Hex.encodeHexString(traceNoN6)+" >> " +new String(traceNoN6));
//    	System.out.println("invoiceNoN6: "+Hex.encodeHexString(invoiceNoN6)+" >> " +new String(invoiceNoN6));
//    	System.out.println("cashierNoN4: "+Hex.encodeHexString(cashierNoN4)+" >> " +new String(cashierNoN4));
//    	System.out.println("chkDigitNoN8: "+Hex.encodeHexString(chkDigitNoN8)+" >> " +new String(chkDigitNoN8));
    	
		
	}
}
