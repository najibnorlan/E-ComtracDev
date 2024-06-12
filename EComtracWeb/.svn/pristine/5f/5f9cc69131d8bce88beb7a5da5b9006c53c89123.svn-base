





<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%
	Class.forName("com.informix.jdbc.IfxDriver");
	String url = "jdbc:informix-sqli://10.7.34.95:2001/ssmcbs:INFORMIXSERVER=rob1;user=informix;password=informix";//rob staging
	//String url = "jdbc:informix-sqli://10.7.34.81:5001/ssmcbs:INFORMIXSERVER=rob1;user=elodge;password=m@mpu01live";//rob staging
	 try {
	 	Connection con = DriverManager.getConnection(url);
	 	
	 	
		PreparedStatement selectOwner = con.prepareStatement("select srlowneridkeycode from robowner where vchnewicnumber='750731016907'");
	 	ResultSet rsSelectOwner = selectOwner.executeQuery();
	 	if(!rsSelectOwner.next()){//Mean dh insert
	 	
		 	String insertOwner = " INSERT INTO robowner(intapplicationid, vchname, chrgender, vchidcardnumber, chridcardtype, chrcolor, vchothercolor, vchnewicnumber, dtdateofbirth, vchaddress, vchtown, vchpostcode, chrstate, vchemail, chremailnotify, chrrace, vchotherrace, vchnationality, vchorigincountry, chrownership, chrstatus, vchownerctr, chrownerblacklistedind, vchcreateby, dtcreatedate, vchupdateby, dtupdatedate, vchtelno, vchmobileno, vchaddress1, vchaddress2)"+ 
		 		    			 " VALUES(null, 'AZAWAN BIN SAHARI', 'L', '', 'K', 'B', null, '750731016907',  '1975-07-31', 'A-10-6 SELAYANG PARIT', 'BATU CAVES', '68100', 'B', '', 'T', 'M', '', 'MAL', '', null, 'A', null, null, 'Admin', '2018-12-24 10:02:20', 'Admin', '2018-12-24 10:02:20', '', '', 'JALAN SPI, LEBUHRAYA SELAYANG KEPONG', '')";
	
		 	
		 	PreparedStatement stInserOwner = con.prepareStatement(insertOwner);
		 	stInserOwner.executeUpdate();
		 	
		 	
		 	
		 	String deleteAud = " delete from robowner_audit"+ 
	    			 			 " where vchnewicnumber_new='750731016907' and date(dtcreatedate_new)='2018-12-24'";
	
		 	
		 	
		 	PreparedStatement stDelOwnerAud = con.prepareStatement(deleteAud);
		 	stDelOwnerAud.executeUpdate();
		 	out.println("Dh Insert Owner<br>");
	 	}else{
	 		out.println("Owner Already Exist<br>");
	 	}
	 	
	 	rsSelectOwner = selectOwner.executeQuery();
	 	if(rsSelectOwner.next()){
	 		String regNoToAdd = "001473794";
	 		
	 		long ownerId = rsSelectOwner.getLong(1);
	 		out.println("OwnerId:"+ownerId+"<br>");
	 		
	 		
	 		String selectOwnerAddrSql = " select * from robaddress where intownerid="+ownerId+" and vchregistrationnumber='"+regNoToAdd+"' ";
	 		PreparedStatement selectOwnerAddr = con.prepareStatement(selectOwnerAddrSql);
	 		if(selectOwnerAddr.executeQuery().next()){
	 			out.println("Link Already Exist<br>");
	 		}else{
	 			String insertAddressSql = " INSERT INTO robaddress(intownerid, vchregistrationnumber, vchreferencenumber, vchaddress, vchtown, vchpostcode, chrstate, chraddresstype, chrbranchcode, chrbranchstatus, vchcreateby, dtcreatedate, vchupdateby, dtupdatedate, dtamendmentdate, vchaddress1, vchaddress2)"+
	 								      "  VALUES( "+ownerId+", '"+regNoToAdd+"', '"+regNoToAdd+"', 'A-10-6 SELAYANG PARIT', 'BATU CAVES', '68100', 'B', 'P', null, null, 'Admin', '2018-12-24 10:02:20', 'Admin', '2018-12-24 10:02:20', null, 'JALAN SPI, LEBUHRAYA SELAYANG KEPONG', '')";	
				PreparedStatement insertAddress = con.prepareStatement(insertAddressSql);
				insertAddress.executeUpdate();					   
	 			
				
				//Delete Audit
		 		String deleLinkAudSql = " delete from robaddress_audit where vchregistrationnumber_new='"+regNoToAdd+"' and date(dtcreatedate_new)='2018-12-24'";
		 		PreparedStatement deleLinkAud  = con.prepareStatement(deleLinkAudSql);
		 		deleLinkAud.executeUpdate();
		 		out.println("Owner Address Success Insert<br>");
	 		}
	 				
	 				 
	 			   
	 		
	 		
	 		String selLinkSql = " select * from robbusinessownerlink where intownerid="+ownerId+" and vchregistrationnumber='"+regNoToAdd+"'";
	 		PreparedStatement selLink = con.prepareStatement(selLinkSql);
	 		if(selLink.executeQuery().next()){
	 			out.println("Link Already Exist<br>");
	 		}else{
	 		
		 		//Insert OwnerLink   001473794-D
		 		String insertLinkSql = " INSERT INTO robbusinessownerlink(srlkeycode, vchregistrationnumber, vchreferencenumber, intownerid, chrstatus, chramendmenttype, chrownershiplink, vchcreateby, dtcreatedate, vchupdateby, dtupdatedate)"+ 
		 		    						   " VALUES(9371223, '"+regNoToAdd+"', '"+regNoToAdd+"', "+ownerId+", 'A', 'P', 'T', 'Admin', '2018-12-24', 'Admin', '2018-12-24 10:24:00')";
		 		
		 		PreparedStatement insertLink = con.prepareStatement(insertLinkSql);
		 		insertLink.executeUpdate();
		 		
		 		
		 		//Delete Audit
		 		String deleLinkAudSql = " delete from robbusinessownerlink_audit where vchregistrationnumber_new='"+regNoToAdd+"' and date(dtcreatedate_new)='2018-12-24'";
		 		PreparedStatement deleLinkAud  = con.prepareStatement(deleLinkAudSql);
		 		deleLinkAud.executeUpdate();
		 		out.println("Link Success Insert<br>");
	 		}
	 	}
	 	
	 	
	 	
	 	
	 	out.println("done<br>");
	 	con.close();
		
	} catch (Exception e) {
		e.printStackTrace();
		out.print(e.toString());
	}
	
 %>