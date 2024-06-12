package com.ssm.llp.base.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ssm.llp.base.common.model.CommonHolidayConfig;

public class HolidayReaderUtils {
	
	public static void main(String[] args)throws Exception {
		//Loading an existing document
		
		  int holidayYear = 2021;
		  
	      File file = new File("D://hka_"+holidayYear+".pdf");
	      PDDocument document = PDDocument.load(file);
	      //Instantiate PDFTextStripper class
	      PDFTextStripper pdfStripper = new PDFTextStripper();
	      //Retrieving text from PDF document
	      String text = pdfStripper.getText(document);
//	      System.out.println(StringEscapeUtils.escapeJava(text));
//	      System.out.println("**********************");
	      
	      Locale malayLoc = new Locale("ms", "MY");
	      SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", malayLoc);
	      System.out.println(sdf.format(new Date()));
	      String branchCodeArray[] = new String[] {"14","15","16","01","02","03","04","05","06","08","09","07","12","13","10","11"};
	      Set<String> dayNameSet = new HashSet<String>(Arrays.asList("Isnin","Selasa","Rabu","Khamis","Jumaat","Sabtu","Ahad")); 
	      
	      
	      
	      String holidayLine[] = StringUtils.split(text,"\n");
	      
	      boolean isFirstBil= false;
	      boolean isKelepasanAmNegeri = false;
	      int countFederal = 0;
	      int countState = 0;
	      
	      List<CommonHolidayConfig> listHolidayConfig = new ArrayList<CommonHolidayConfig>();
	      
	      for (int i = 0; i < holidayLine.length; i++) {
	    	  String line = StringUtils.chomp(holidayLine[i]);
	    	  String[] lineItem = StringUtils.split(line," ");
	    	  String negeri="";
	    	  if(isKelepasanAmNegeri) {
	    		  negeri="Negeri";
	    	  }
	    	  System.out.println(negeri+">>"+line);
	    	  
	    	  if(dayNameSet.contains(lineItem[0].trim())) {
	    		  
	    		  CommonHolidayConfig holidayConfig = new CommonHolidayConfig();
	    		  holidayConfig.setEventYear(holidayYear);
	    		  
	    		  String listBranchCode = "";
		    	  for (int j = 0; j < branchCodeArray.length; j++) {
		    		  String branchCode = branchCodeArray[j];
		    		  boolean isHoliday = true;
		    		  
//		    		  new String()/
		    		  String isHolidayStr = lineItem[j+1].charAt(0)+"";
//		    		  System.out.println(StringEscapeUtils.escapeJava(isHolidayStr));
		    		  if( "-".equals(isHolidayStr)) {
		    			  isHoliday = false;
		    		  }else {
		    			  if(StringUtils.isNotBlank(listBranchCode)) {
		    				  listBranchCode+=",";
		    			  }
		    			  listBranchCode+=branchCode;
		    		  }
//		    		  System.out.println(branchCode+":"+lineItem[j+1].charAt(0)+":"+isHoliday);
		    	  }
		    	  
		    	  int holidayDate = Integer.parseInt(lineItem[branchCodeArray.length].substring(1));
		    	  String holidayMonth = lineItem[branchCodeArray.length+1].trim();
		    	  String holidayDateStr = holidayDate+" "+holidayMonth+" "+holidayYear;
		    	  
		    	  
		    	  holidayConfig.setEventDate(sdf.parse(holidayDateStr));
		    	  holidayConfig.setEventState(listBranchCode);
		    	  int currentCount;
		    	  
		    	  
		    	  if(isKelepasanAmNegeri) {
		    		  holidayConfig.setEventType("STATE");
		    		  countState++;
		    		  currentCount=countState;
		    	  }else {
		    		  countFederal++;
		    		  holidayConfig.setEventType("FEDERAL");
		    		  currentCount=countFederal;
		    	  }
		    	  
		    	  String eventName = "";
		    	  for (int j = branchCodeArray.length+1; j < lineItem.length; j++) {
		    		  String evenNameTmp = lineItem[j];
//		    		  if(j==lineItem.length-1) {
//		    			  System.out.println("ss");
//		    			  evenNameTmp+=">>"+currentCount;
//		    		  }
		    		  eventName+=evenNameTmp+" ";
		    	  }
		    	  
//		    	  String prevEventName = listHolidayConfig.get(listHolidayConfig.size()-1).getEventName();
//		    	  if(eventName.indexOf(prevEventName)!=-1) {
//		    		  
//		    	  }
		    	  System.out.println("eventName>:"+eventName);
		    	  listHolidayConfig.add(holidayConfig);
		    	  
		    	  
	    	  }
	    	  
	    	  
	    	  if(!isKelepasanAmNegeri && lineItem[0].equals("BIL") ) {
	    		  if(!isFirstBil) {
	    			  isFirstBil = true;
	    		  }else {
	    			  String nextLine = StringUtils.chomp(holidayLine[i+1]);
	    			  String[] nextLineItem = StringUtils.split(nextLine," ");
	    			  if(nextLineItem[nextLineItem.length-1].endsWith("1")) {
	    				  isKelepasanAmNegeri=true;
	    			  }
	    		  }
	    	  }
	      }
	      //Closing the document
	      document.close();
	}
	
	public static void main2(String[] args)throws Exception {
//		String url = "https://publicholidays.com.my/ms/2020-dates/";
//		
//		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
//		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy01.ssm.com.my", 80));
//		
//		Document doc = Jsoup.connect(url).proxy(proxy).get();
		
		
		String file = "d://2021.html";
		Document doc = Jsoup.parse(new File(file), "UTF-8");
		
		
		System.out.println(doc.title());
		Element table = doc.getElementsByClass("publicholidays phgtable ").get(0);
		
//		System.out.println(table);
		System.out.println();
		
//		String trCount = table.getElementsByTag("tr").size();
		Elements trElementList = table.getElementsByTag("tr");
		
		int count = 0;
		for (int i = 1; i < trElementList.size(); i++) {
			Element trElement = trElementList.get(i);
			if(trElement.hasClass("even") || trElement.hasClass("odd")) {
				count++;
//				System.out.println(trElementList.get(i));
				Elements tdElementList = trElement.getElementsByTag("td");
				String date = tdElementList.get(0).text();
				String dayName = tdElementList.get(1).text();
				String eventname = tdElementList.get(2).text();
				String eventState = tdElementList.get(3).text();
				
				System.out.println(date+":"+dayName+":"+eventname+":"+eventState);
				
				if (eventState.toLowerCase().indexOf("semua negeri kecuali")!=-1) {
					System.out.println("ALL EXCEPT");
				}
				else if (eventState.toLowerCase().indexOf("semua negeri")!=-1) {
					System.out.println("ALLLL");
				}
				else {
					System.out.println("Selected");
				}
//				for (int j = 0; j < tdElementList.size(); j++) {
//					Element tdElement = tdElementList.get(j);
//					System.out.println(tdElement);
//				}
			}
		}
		System.out.println(count);
		
//		 <td>26 Dis</td>
//		 <td>Ahad</td>
//		 <td><a href="https://publicholidays.com.my/ms/christmas/" class="summary url">Cuti Hari Krismas</a> </td>
//		 <td class="regionlist location">Kelantan &amp; Terengganu</td>
		
//		for (int i = 0; i < table.size(); i++) {
//			System.out.println(table.get(i));
//		}
//		InputStream in = new URL(url).openStream();
//		String content;
//
//		try {
//		   content = IOUtils.toString(in);
//		 } finally {
//		   IOUtils.closeQuietly(in);
//		 }
//		System.out.println(content);
	}
}
