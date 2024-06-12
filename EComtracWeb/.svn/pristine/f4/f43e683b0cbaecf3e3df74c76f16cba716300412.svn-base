package com.ssm.llp.base.common.service.impl;

import java.util.Arrays;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.dao.RobPosTerminalTransactionDao;
import com.ssm.llp.base.common.service.RobPosTerminalTransactionService;
import com.ssm.llp.ezbiz.model.RobPosTerminalTransaction;
import com.ssm.ssmlocalsvr.POSTerminalUtils;

@Service
public class RobPosTerminalTransactionServiceImpl extends BaseServiceImpl<RobPosTerminalTransaction,  Integer> implements RobPosTerminalTransactionService{

	@Autowired 
	RobPosTerminalTransactionDao robPosTerminalTransactionDao;
	
	@Override
	public BaseDao getDefaultDao() {
		return robPosTerminalTransactionDao;
	}

	@Override
	public void processAndUpdate(String cmd, byte byteResponse[], RobPosTerminalTransaction posTerminalTransaction) throws Exception{
		
//		byte byteResponse[] = Hex.decodeHex(hexResponseMsg.toCharArray()); 
		
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

//    	posTerminalTransaction.setCommand(new String(byteCommandN3));
    	
    	posTerminalTransaction.setStatus(new String(byteStatusANS2));
    	
    	
    	String strCardWithLength = new String(byteCNumberN22);
    	int lengthCard = new Integer(strCardWithLength.substring(0,2));
    	String cardNo = strCardWithLength.substring(2,lengthCard+2);
    	
    	posTerminalTransaction.setCreditCardNo(cardNo);
    	posTerminalTransaction.setCreditCardExpDt(new String(byteExDtN4));
    	posTerminalTransaction.setCreditCardType(new String(byteCardTypeN2));
    	
    	posTerminalTransaction.setApprovalCode(new String(aprovalCodeANS8));
    	
    	
    	String txnAmt = new String(tranxAmtN12);
    	txnAmt =  txnAmt.substring(0, txnAmt.length()-2)+"."+txnAmt.substring(txnAmt.length()-2);
    	posTerminalTransaction.setTransAmount(Double.parseDouble(txnAmt));
    	
    	String netAmt = new String(netAmtN12);
    	netAmt =  netAmt.substring(0, netAmt.length()-2)+"."+netAmt.substring(netAmt.length()-2);
    	posTerminalTransaction.setNetAmount(Double.parseDouble(netAmt));
    	
    	posTerminalTransaction.setTraceNo(new String(traceNoN6));
    	posTerminalTransaction.setInvoiceNo(new String(invoiceNoN6));
    	posTerminalTransaction.setCashierNo(new String(cashierNoN4));
    	
    	if(posTerminalTransaction.getStatus().startsWith("S")) {
    		byte balanceError[] = Arrays.copyOfRange(byteResponse, start+0, byteResponse.length-1);
        	System.out.println(new String(balanceError));
    	}
    	
		update(posTerminalTransaction);
	}

}
