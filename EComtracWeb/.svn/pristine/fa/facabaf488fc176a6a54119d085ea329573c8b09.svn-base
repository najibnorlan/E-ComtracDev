package com.ssm.llp.base.utils;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class GetTemplate {
	public static void main(String[] args)throws Exception {
		Connection conn = DBConnTest.getLLPProdConnection();
		
		
		String fileCode= "LLP_CERT_BM";
		String filePath = "D:/workspaces/NewFramework/LLPWeb/tmp/";
		
		
		String sql = "select * from llp_file_upload where file_code=? ";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, fileCode);
		
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()){
			String fileName = rs.getString("file_name");
			byte byteData[] = rs.getBytes("file_data");
			
			FileOutputStream fos = new FileOutputStream(filePath+System.currentTimeMillis()+fileName);
			fos.write(byteData);
			fos.close();
		}
		
		conn.close();
		System.out.println("DONE!!");
		
	}
}
