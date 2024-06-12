package com.ssm.llp.jaxws;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;


@WebService
@SOAPBinding(style=Style.RPC)
public interface LlpClusterService {
	@WebMethod
	public String processClustered(long callingId, String serviceId, String cmd, byte[] byteObjectData);
}
