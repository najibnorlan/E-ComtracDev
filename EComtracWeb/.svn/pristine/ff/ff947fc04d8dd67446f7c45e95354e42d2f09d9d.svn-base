package com.ssm.llp.jaxws;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.ssm.llp.base.common.service.SSMDistributedService;
import com.ssm.llp.base.page.WicketApplication;


@WebService(endpointInterface="com.ssm.llp.jaxws.LlpClusterService")
public class LlpClusterServiceImpl implements LlpClusterService {
	
	public static long callingId;
	
	@Override
	@WebMethod
	public String processClustered(long callingId, String serviceId, String cmd, byte[] byteObjectData) {
		if(this.callingId==callingId){
			return "no need to process";
		}
		try {
			SSMDistributedService ssmDistributedService =  (SSMDistributedService)WicketApplication.getServiceObject(serviceId);
			
			Object objectData = null;
			if(byteObjectData!=null){
				ByteArrayInputStream bais = new ByteArrayInputStream(byteObjectData);
				ObjectInputStream ois = new ObjectInputStream(bais);
				objectData = ois.readObject();
				ois.close();
				bais.close();
			}
        	ssmDistributedService.processDistributed(cmd, objectData);
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		return "success";
	}
	
	public static long generateCallingId(){
		callingId = System.currentTimeMillis();
		return callingId;
	}
	
}
