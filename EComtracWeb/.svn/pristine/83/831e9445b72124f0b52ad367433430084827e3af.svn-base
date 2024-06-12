/*
 * This software is the confidential and proprietary information of Ssm
 * ("Confidential Information").
 * You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the
 * license agreement you entered into
 * with Ssm.
 */
package com.ssm.llp.base.common.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.namespace.QName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssm.llp.base.common.dao.BaseDao;
import com.ssm.llp.base.common.dao.LlpAppsvrNodeDao;
import com.ssm.llp.base.common.db.SearchCriteria;
import com.ssm.llp.base.common.model.LlpAppsvrNode;
import com.ssm.llp.base.common.service.LlpAppsvrNodeService;
import com.ssm.llp.base.page.WicketApplication;
import com.ssm.llp.jaxws.LlpClusterService;
import com.ssm.llp.jaxws.LlpClusterServiceImpl;

@Service
public class LlpAppsvrNodeServiceImpl extends BaseServiceImpl<LlpAppsvrNode,  Long> implements LlpAppsvrNodeService{
	@Autowired 
	LlpAppsvrNodeDao llpAppsvrNodeDao;

	@Override
	public BaseDao getDefaultDao() {
		return llpAppsvrNodeDao;
	}

	@Override
	public void distributeToAllNode(String serviceId, String cmd, Object objectData) {
		if(1==1){// urn Off distribution
			return ;
		}
		
		List<LlpAppsvrNode> llpNode = findByCriteria(new SearchCriteria()).getList();
		
		for (int i = 0; i < llpNode.size(); i++) {
    		LlpAppsvrNode appsvrNode = llpNode.get(i);
    		
    		
    		try {
    			String urlPath = appsvrNode.getNodeSvrUrl()+"llpClusterWs?wsdl";
    			System.out.println("Calling:"+urlPath+"\tCMD:"+cmd);
    			 // Specify the URL
    		      final URL url = new URL(urlPath);
    		      // Specify the qualified name
    		      final QName name = new QName("http://jaxws.llp.ssm.com/", LlpClusterServiceImpl.class.getSimpleName()+"Service");
    		      // Create the service
    		      final javax.xml.ws.Service service = javax.xml.ws.Service.create(url, name);
    		      // Get the port
    		      final LlpClusterService simple = service.getPort(LlpClusterService.class);
    		      // Call an operation
    		      byte byteData[]  = null;
    		      if(objectData!=null){
	    		      ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    		      ObjectOutputStream oos = new ObjectOutputStream(baos);
	    		      oos.writeObject(objectData);
	    		      byteData = baos.toByteArray();
	    		      oos.close();
	    		      baos.close();
    		      }
    		      
    		      
    		      
    		      
    		      System.out.println(simple.processClustered(LlpClusterServiceImpl.generateCallingId(), serviceId, cmd, byteData));
//    			String urlParameters = "serviceId="+serviceId+"&serviceParam="+param;
//    			String urlPath = appsvrNode.getNodeSvrUrl()+"LlpCluster?";
//    			System.out.println(urlPath);
//    			URL url = new URL(urlPath);
//    			
//                URLConnection conn = url.openConnection();
//                conn.setDoOutput(true);
//                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
//                
//                //write parameters
//                writer.write(urlParameters);
//                writer.flush();
//                
//                // Get the response
//                StringBuffer answer = new StringBuffer();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    answer.append(line);
//                }
//                writer.close();
//                reader.close();
    			
			} catch (Exception e) {
				e.printStackTrace(System.err);
//				System.err.print(e.getMessage());
			}
    	}
	}

}
