package com.ssm.llp.base.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnTest {
public static Connection getLLPProdConnection() throws Exception {
		
 		String url = "jdbc:informix-sqli://10.7.34.128:2001/llp:INFORMIXSERVER=llp";//llp prodution
 	   
 	    try {
 	    	Class.forName("com.informix.jdbc.IfxDriver");
// 	    	DriverManager.setLogWriter(new PrintWriter("yourFileName.log"));
 	    	Connection con = DriverManager.getConnection(url,"zamzam", "ss");
 	    	
 	    	
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
	
	public static Connection getROBDevConnection() throws Exception {
		
 		String url = "jdbc:informix-sqli://10.7.34.95:2001/ssmcbs:INFORMIXSERVER=rob1";//roc staging
 	   
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
