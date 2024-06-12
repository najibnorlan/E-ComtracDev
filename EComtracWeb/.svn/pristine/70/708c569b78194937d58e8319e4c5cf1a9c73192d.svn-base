package com.ssm.llp.base.utils;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.informix.jdbc.IfxConnection;
import com.ssm.llp.mod1.model.LlpPartnerLink;
import com.ssm.llp.mod1.model.LlpRegistration;

public class TestReadSessionData {
	public static void main(String[] args) throws Exception{
		
		Connection conPROD = getLLPProdConnection();
//		Connection conPROD = getLLPDevConnection();
		long id[] = new long[]{7552};//6  48

		for (int i = 0; i < id.length; i++) {
			String sql = "select  ns_ref_no, payment_transaction_id, status, create_dt, create_by, update_dt, update_by, reg_data  from llp_reg_transaction where reg_transaction_id="+id[i];
		    Statement ps = conPROD.createStatement();
//			ps.setLong(1, id[i]);
			
			ResultSet rs = ps.executeQuery(sql);
			if(rs.next()){

				
				byte[] obj = (byte[])rs.getObject("reg_data");
				FileOutputStream oos = new FileOutputStream("D:/workspaces/NewFramework/Test/datapostman"+System.currentTimeMillis()+".txt");
				oos.write(obj);
				oos.close();
				
				
				ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(obj));
				LlpRegistration llpRegistration = (LlpRegistration) ois.readObject();
				System.out.println(llpRegistration.getLlpName());
				
				System.out.println(llpRegistration.getLlpNo());
				System.out.println(StringUtils.isBlank(llpRegistration.getLlpNo()));
				
				
				System.out.println("****LLPRESERV NAME");
				ObjectUtils.printObject(llpRegistration.getLlpReservedName());
				System.out.println("****LLPRESERV NAME END");		
						
				List<LlpPartnerLink> list = llpRegistration.getLlpPartnerLinks();
				System.out.println("PARTNER:");
				for (int j = 0; j < list.size(); j++) {
					LlpPartnerLink llpPartnerLink = list.get(j);
					
					System.out.println("ParknerType:"+llpPartnerLink.getType());
					System.out.println("UserRefNo:"+llpPartnerLink.getLlpUserProfile().getUserRefNo()+"<-- Jika Null mean baru");
					
					System.out.println("Name:"+llpPartnerLink.getLlpUserProfile().getName());
					
					System.out.println("ParknerEmail:"+llpPartnerLink.getLlpUserProfile().getEmail());
					System.out.println("UserEmail:"+llpPartnerLink.getEmail());
					System.out.println();
					
					ObjectUtils.printObject(llpPartnerLink.getLlpUserProfile());
					
					System.out.println();
					System.out.println("**********************************");
				}
			}
			ps.close();
			
			
		}
		
		conPROD.close();
		
		
	}
	public static Connection getLLPProdConnection() throws Exception {
		
 		String url = "jdbc:informix-sqli://10.7.34.128:2001/llp:INFORMIXSERVER=llp";//llp prodution
 	   
 	    try {
 	    	Class.forName("com.informix.jdbc.IfxDriver");
// 	    	DriverManager.setLogWriter(new PrintWriter("yourFileName.log"));
 	    	Connection con = DriverManager.getConnection(url,"zamzam", "12");
 	    	
 	    	
 	    	return con;
 			
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 		return null;
	}
	
public static Connection getLLPDevConnection() throws Exception {
		
 		String url = "jdbc:informix-sqli://10.7.34.95:2002/llp:INFORMIXSERVER=roc1";//roc staging
 	   
 	    try {
 	    	
 	    	Class clasz =Class.forName("com.informix.jdbc.IfxDriver");//com.informix.jdbc.IfxDriver");
 	    	System.out.println(clasz);
 	    	
// 	    	DriverManager.setLogWriter(new PrintWriter(System.out));
 	    	
 	    	Connection con = DriverManager.getConnection(url,"informix", "informix");
 	    	return con;
 			
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 		return null;
	}
}
