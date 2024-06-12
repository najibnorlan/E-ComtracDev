package com.ssm.zamzam;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.ssm.llp.base.utils.DBConnTest;

public class BugFix {
	public static void main(String[] args)throws Exception {
		Connection conn = DBConnTest2.getEzBizProdConnection("zamzam", BugParam.password);
//		Connection conn = DBConnTest2.getEzBizDevConnection();

		
		double intNote[] = new double[]{100,50,20,10,5,1,0.5,0.2,0.1,0.05,0.01};
		int intNoteQuantity[] = new int[]{10,32,3,2,0,5,9,0,0,6,0};
		
		
		double amt = 2689.8;
		String user =  "SSM:norziah";
		int sessionId = 16;
		
		if(intNote.length!=intNoteQuantity.length){
			System.out.println("Not Bal");
			System.exit(0);
		}
		System.out.println("Balance");
		
		BigDecimal sum = new BigDecimal(0);
		for (int i = 0; i < intNote.length; i++) {
			 sum = sum.add(new BigDecimal( intNote[i]*intNoteQuantity[i]));
		}	
		if(sum.doubleValue()!=amt){
			System.out.println("Xsama total :"+sum.doubleValue()+" >> "+amt);
			System.exit(0);
		}
		
		
		String sql = "INSERT INTO rob_counter_balancing(bank_note, quantity, amount, counter_session_id, create_dt, create_by, update_dt, update_by) "
				+  " VALUES( ?, ? , ? , ?, current, ?, current, ?)";
				
		PreparedStatement ps = conn.prepareStatement(sql);
		
		for (int i = 0; i < intNote.length; i++) {
			ps.setDouble(1, intNote[i]);
			ps.setInt(2, intNoteQuantity[i]);
			ps.setDouble(3, intNote[i]*intNoteQuantity[i]);
			ps.setInt(4, sessionId);
			ps.setString(5, user);
			ps.setString(6, user);
			
//			ps.execute();
		}
		
		String sql2 = " update rob_counter_session set balancing_status='DE',balancing_by='"+user+"',balancing_dt=current where session_id="+sessionId;
		Statement st = conn.createStatement();
//		st.execute(sql2);
		
		
		
		conn.close();
	}
	
	public static void main2(String[] args) throws Exception{
		Connection connEzBiz = DBConnTest2.getEzBizProdConnection("zamzam",BugParam.password);
		System.out.println(connEzBiz);
		
		
		
		Connection connCBSROB = DBConnTest2.getCBSROBProdConnection("zamzam",BugParam.cbspassword);
		System.out.println(connCBSROB);
		

		
		String listQueryCBS = "select * from elodge_tray_rob where chrapplicationstatus='K' and vchcreateby='EZBIZ'";
		String queryStatusAEzBiz = " select * from rob_form_a where rob_form_a_code=? and status='IP' ";
		String queryStatusBEzBiz = " select * from rob_form_b where rob_form_b_code=? and status='IP' ";
		
		PreparedStatement psQueryAStatus = connEzBiz.prepareStatement(queryStatusAEzBiz);
		PreparedStatement psQueryBStatus = connEzBiz.prepareStatement(queryStatusBEzBiz);
		
		Statement st = connCBSROB.createStatement();
		ResultSet rs = st.executeQuery(listQueryCBS);
		while(rs.next()){
			String refNo = rs.getString("vchreferencenumber");
//			System.out.println(refNo);
			if(refNo.equals("EB-B2017032900006")){
//				System.out.println("s");
			}
			
			if(refNo.startsWith("EB-A")){
				psQueryAStatus.clearParameters();
				psQueryAStatus.setString(1, refNo);
				ResultSet rsQEB = psQueryAStatus.executeQuery();
				if(rsQEB.next()){
					String ezbizStatus = rsQEB.getString("status");
					System.out.println(ezbizStatus);
				}
			}else if(refNo.startsWith("EB-B")){
				psQueryBStatus.clearParameters();
				psQueryBStatus.setString(1, refNo);
				ResultSet rsQEB = psQueryBStatus.executeQuery();
				if(rsQEB.next()){
					String ezbizStatus = rsQEB.getString("status");
					System.out.println(refNo+":"+ezbizStatus);
				}
			}else{
				System.out.println(refNo);
			}
			
			
			
		}
		
		connCBSROB.close();
		connEzBiz.close();
	}
}
