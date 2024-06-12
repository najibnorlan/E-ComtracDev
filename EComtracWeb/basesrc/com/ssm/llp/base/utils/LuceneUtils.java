package com.ssm.llp.base.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LuceneUtils {
	public static void main(String[] args) throws Exception{
		
		Connection conn = DBConnTest.getROBDevConnection();
		System.out.println(conn);
		String sql = "select vchregistrationnumber, vchname from robbusiness";
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);
		System.out.println("start");
		while (rs.next()) {
//		    Document doc = new Document();
//		    doc.add(new Field("id", rs,getString("id"), Field.Store.YES, Field.Index.UN_TOKENIZED));
//		    doc.add(new Field("firstname", rs,getString("firstname"), Field.Store.YES, Field.Index.TOKENIZED));  
//		    // ... repeat for each column in result set
//		    writer.addDocument(doc);
		}
		System.out.println("Done");
		rs.close();
		st.close();
		conn.close();
	}
}
