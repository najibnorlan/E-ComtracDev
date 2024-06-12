package com.ssm.llp.base.utils;

import com.ssm.llp.base.exception.SSMException;
import com.ssm.llp.base.exception.SSMExceptionParam;
import com.ssm.llp.base.pg.model.ReturnStatus;
import com.ssm.llp.base.pg.model.ReturnStatusResponse;
import com.ssm.llp.base.pg.ws.SsmPgWSStub;
import com.ssm.llp.base.pg.xsd.Payment;

public class TestCheckPayment {
	public static void main(String[] args) throws Exception{
			ReturnStatusResponse returnStatusResponse = null;
			String ids[] = new String[]{"L2013061900004"};/*,"L2013052800001","L2013052800002","L2013052800003","L2013052800004",
					"L2013052800005","L2013052800006","L2013052800007","L2013052800008",
					"L2013052800009","L2013052800010"}   ; */
     
			System.out.println("Start");
			for (int i = 0; i < ids.length; i++) {
				String id = ids[i];
				
				try{
					SsmPgWSStub ssmPgWSStub = new SsmPgWSStub("http://192.168.3.84/axis2/services/SsmPgServices?wsdl");//URL PRODUCTION

					ReturnStatus returnStatus = new ReturnStatus();
					returnStatus.setId(id);

					returnStatusResponse = ssmPgWSStub.returnStatus(returnStatus);
					System.out.println(id+"\t"+returnStatusResponse.get_return().getTransStatus());
					if (returnStatusResponse.get_return() != null) {
						Payment payment = returnStatusResponse.get_return();

						System.out.println(payment.getTransMode());
						System.out.println(payment.getTransDetail());
						System.out.println(payment.getTransApprovalCode());
					}
				}catch(Exception e){
					throw new SSMException(SSMExceptionParam.PAYMENT_RESPONSE_FAIL);
				}	
			}
	}
}
