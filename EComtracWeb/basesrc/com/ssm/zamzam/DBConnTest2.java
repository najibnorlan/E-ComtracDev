package com.ssm.zamzam;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnTest2 {
	
	
	public static Connection getEzBizProdConnection(String u, String p) throws Exception {
		
 		String url = "jdbc:informix-sqli://10.7.34.145:2001/ezbiz:INFORMIXSERVER=llp";//llp prodution
 	   
 	    try {
 	    	Class.forName("com.informix.jdbc.IfxDriver");
 	    	Connection con = DriverManager.getConnection(url, u, p);
 	    	
 	    	return con;
 			
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 		return null;
	}
	public static Connection getEzBizDevConnection() throws Exception {
		
 		String url = "jdbc:informix-sqli://10.7.34.129:2001/ezbiz:INFORMIXSERVER=llp";//roc staging
 	   
 	    try {
 	    	
 	    	Class clasz =Class.forName("com.informix.jdbc.IfxDriver");//com.informix.jdbc.IfxDriver");
 	    	System.out.println(clasz);
 	    	
// 	    	DriverManager.setLogWriter(new PrintWriter(System.out));
 	    	
 	    	Connection con = DriverManager.getConnection(url,"informix", "P@ssw0rd");
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

	public static Connection getCBSROBProdConnection(String u, String p) {
		String url = "jdbc:informix-sqli://10.7.34.82:2001/ssmcbs:INFORMIXSERVER=rob2";
	 	   
 	    try {
 	    	
 	    	Class clasz =Class.forName("com.informix.jdbc.IfxDriver");//com.informix.jdbc.IfxDriver");
 	    	
 	    	
 	    	Connection con = DriverManager.getConnection(url, u, p);
 	    	return con;
 			
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 		return null;
	}
}
